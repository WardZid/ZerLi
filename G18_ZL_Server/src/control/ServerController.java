package control;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

import boundary.ServerView;
import boundary.fxmlControllers.ServerViewController;
import entity.BuildItem;
import entity.Complaint;
import entity.Customer;
import entity.Customer.CustomerStatus;
import entity.Item;
import entity.MyMessage;
import entity.MyMessage.MessageType;
import entity.Order;
import entity.Survey;
import entity.User;
import ocsf.server.ConnectionToClient;
import ocsf.server.ObservableServer;

public class ServerController extends ObservableServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	/**
	 * The server's IP Address
	 */
	private InetAddress ip;

	/**
	 * Server Host Name
	 */
	public static String host;

	private static boolean serverState;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public ServerController(int port) {
		super(port);

		try {
			ip = InetAddress.getLocalHost();
			host = ip.getHostName();
//			ServerView.print(getClass(), "Your current IP address : " + ip);
//			ServerView.print(getClass(), "Your current Hostname : " + host);

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	// Getters********************************

	public String getIP() {
		return ip.getHostAddress();
	}

	// Instance methods ************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 * 
	 * @throws Exception
	 */
	public void startServer() throws Exception {
		try {
			DBController.connectDB();
			listen(); // Start listening for connections
			serverState = true;
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception ex) {
			ServerView.printErr(getClass(), "ERROR - Could not listen for clients!\t Cause: " + ex.getMessage());

		}
	}

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 */
	public void stopServer() {
		if (!serverState)
			return;
		try {
			sendToAllClients(new MyMessage(null, 0, MessageType.INFO, "global/stop", null));
			close();
			serverState = false;
			DBController.disconnectDB();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		super.serverStarted();
		ServerView.print(ServerController.class, "Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		super.serverStopped();
		ServerView.print(ServerController.class, "Server has stopped listening for connections.");
	}

	@Override
	protected synchronized void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		ServerViewController.addClient(client);
		ServerView.print(getClass(), "New Client Connected: " + client.getInetAddress());
	}

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		super.clientDisconnected(client);
	}

	@Override
	public void sendToAllClients(Object msg) {
		ServerView.print(getClass(), "-> " + msg);
		super.sendToAllClients(msg);
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		super.handleMessageFromClient(msg, client);
		ServerView.print(getClass(), "<- " + msg + " from " + client);
		handleRequest(msg, client);
	}

	/**
	 * handleRequests handles requests from clients in HTTP style
	 */
	private void handleRequest(Object msg, ConnectionToClient client) {
		if (!(msg instanceof MyMessage))
			return;
		MyMessage clMsg = (MyMessage) msg;
		switch (clMsg.getType()) {
		case INFO:
			handleInfoMessage(clMsg, client);
			break;
		case GET:
			handleGetRequest(clMsg, client);
			break;
		case POST:
			handlePostRequest(clMsg, client);
			break;
		case UPDATE:
			handleUpdateRequest(clMsg);
			break;
		case DELETE:

			break;
		default:
			ServerView.printErr(getClass(), "Unhandled Client Request: " + clMsg.toString());
			break;
		}
		// finally reply to client
		try {
			client.sendToClient(clMsg);
		} catch (IOException e) {
			sendToAllClients(clMsg);
		} finally {
			ServerView.print(getClass(), "-> " + clMsg + " to " + client);
		}

	}

	private void handleInfoMessage(MyMessage clMsg, ConnectionToClient client) {

		String[] request = clMsg.getInfo().split("/");

		if (request[0].equals("disconnect")) {
			ServerView.print(getClass(), "Client Disconnected: " + client.toString());
			ServerViewController.removeClient(client);

		} else if (request[0].equals("connect")) {
			ServerView.print(getClass(), "Client Connected: " + client.toString());

		} else if (request[0].equals("log")) {
			User u = (User) clMsg.getContent();
			if (request[1].equals("in")) {
				clMsg.setContent(DBController.updateLogIn(u));
			} else if (request[1].equals("out")) {
				clMsg.setContent(DBController.updateLogOut(u));
			}
		} else {
			ServerView.printErr(getClass(), "Unhandled info Message: " + clMsg.getInfo());
		}
	}

	/**
	 * handles clients' GET requests
	 * 
	 * @param MyMessage Contains GET request
	 */

	private void handleGetRequest(MyMessage clMsg, ConnectionToClient client) {
		String[] request = clMsg.getInfo().split("/");

		if (request[0].equals("login")) {
			User user = (User) clMsg.getContent();
			if (request[1].equals("user")) {
				clMsg.setContent(DBController.getUser(user.getUsername(), user.getPassword()));

			} else if (request[1].equals("customer")) {
				clMsg.setContent(DBController.getCustomerBy("id_user", user.getIdUser() + ""));
			}
		} else if (request[0].equals("order")) {
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getOrdersAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getOrdersBy(request[2], request[3]));
			} else if (request[1].equals("fill")) {
				Order o = (Order) clMsg.getContent();
				clMsg.setContent(DBController.getOrderItemsFull(o));
			} else if (request[1].equals("byBranchMonth")) {
				clMsg.setContent(DBController.getOrdersByBranchMonthYear(request[2], request[3], request[4]));
			} else if (request[1].equals("report")) {
				if (request[2].equals("sale")) {
					if (request[3].equals("months")) {
						clMsg.setContent(DBController.getMonthsInBranch(request[4]));
					}
				} else if (request[2].equals("sum")) {
					if (request[3].equals("income")) {
						clMsg.setContent(DBController.getSumOfDailyIncome(request[4], request[5], request[6]));
					}
				} else if (request[2].equals("incomebycustomer")) {
					clMsg.setContent(DBController.getReceiptsOfMonth(request[3], request[4], request[5]));
				}
			}
		} else if (request[0].equals("item")) {
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getItemsAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getItemsBy(request[2], request[3]));
			} else if (request[1].equals("amount")) {
				clMsg.setContent(DBController.getAmountOfEveryItem(request[2], request[3], request[4]));
			}else if(request[1].equals("complete")) {
				clMsg.setContent(DBController.getItemsComplete());
			}
		} else if (request[0].equals("build_item")) {
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getBuildItemsAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getBuildItemsBy(request[2], request[3]));
			} else if (request[1].equals("full")) {
				if (request[2].equals("all")) {
					clMsg.setContent(DBController.getFullBuildItemsAll());
				} else if (request[2].equals("by")) {
					clMsg.setContent(DBController.getFullBuildItemsBy(request[3], request[4]));
				}
			}
		} else if (request[0].equals("item_in_build")) {
			clMsg.setContent(DBController.getItemInBuildAll((BuildItem) clMsg.getContent()));
		} else if (request[0].equals("store")) {
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getStoreAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getStoreBy(request[2], request[3]));
			}
		} else if (request[0].equals("category")) {
			if (request[1].equals("all"))
				clMsg.setContent(DBController.getCategoryAll());
			else if (request[1].equals("by")) {
				if (request[2].equals("type"))
					clMsg.setContent(DBController.getCategoryByType(request[3]));
			}
		} else if (request[0].equals("type")) {
			if (request[1].equals("all"))
				clMsg.setContent(DBController.getTypeAll());
		} else if (request[0].equals("customer")) {
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getCustomerAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getCustomerBy(request[2], request[3]));
			}
		} else if (request[0].equals("complaint")) {
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getComplaintsAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getComplaintsBy(request[2], request[3]));
			}
		} else if (request[0].equals("questions")) {
			if(request[1].equals("all")) 
				clMsg.setContent(DBController.getAllSurves());
		}
		else if(request[0].equals("survey")) {
			if(request[1].equals("date_survey"))
				clMsg.setContent(DBController.getAllSurvesYears());
		}
//		else if (request[0].equals("questions")) {
//			if(request[1].equals("issue_date")&& request[2].equals("all")) 
//				clMsg.setContent(DBController.getAllDatesForQuestion());
//		} 
//		else if(request[0].equals("survey_question")) {
//			if(request[1].equals("all"))
//				clMsg.setContent(DBController.getAllSurviesQuestion());
//		}
		else {
			ServerView.printErr(getClass(), "Unhandled Get request: " + clMsg.getInfo());
		}
	}

	private void handlePostRequest(MyMessage clMsg, ConnectionToClient client) {

		String[] request = clMsg.getInfo().split("/");

		if (request[0].equals("complaint")) {
			Complaint complaint = (Complaint) clMsg.getContent();
			clMsg.setContent(DBController.insertComplaint(complaint));
			
		} else if (request[0].equals("survey")) {
			Survey s=(Survey) clMsg.getContent();
			clMsg.setContent(DBController.insertSurvey(s));
		} else if(request[0].equals("item")) {
			Item i=(Item)clMsg.getContent();
			clMsg.setContent(DBController.insertItem(i));
		}
		else {
			ServerView.printErr(getClass(), "Unhandled POST request: " + clMsg.getInfo());
		}

	}

	/**
	 * handles clients' UPDATE requests
	 * 
	 * @param MyMessage Contains Update request and updated object
	 */
	private void handleUpdateRequest(MyMessage clMsg) {

		String[] request = clMsg.getInfo().split("/");

		if (request[0].equals("order")) {
			Order order = (Order) clMsg.getContent();
			if (request[1].equals("status")) {
				clMsg.setContent(DBController.updateOrderStatus(order));
			}
		} else if (request[0].equals("customer")) {
			Customer c = (Customer) clMsg.getContent();
			if (request[1].equals("status")) {
				clMsg.setContent(DBController.updateCustomerStatusOne(c, CustomerStatus.getById(c.getIdCustomerStatus())));
			}
		} else if (request[0].equals("complaint")) {
			Complaint complaint = (Complaint) clMsg.getContent();
			clMsg.setContent(DBController.updateComplaint(complaint));
		} else if (request[0].equals("item")) {
			Item i=(Item)clMsg.getContent();
			if(request[1].equals("edit")) {
				clMsg.setContent(DBController.updateEditItem(i));
			} else if(request[1].equals("status")) {
				clMsg.setContent(DBController.updateItemStatus(i));
			}

		} else
			ServerView.printErr(getClass(),"Unhandled Update Request: " +clMsg.getInfo());
	}
}