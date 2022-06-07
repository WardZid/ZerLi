package control;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import boundary.ServerView;
import boundary.fxmlControllers.ServerViewController;
import entity.BuildItem;
import entity.Complaint;
import entity.Customer;
import entity.Email;
import entity.Item;
import entity.MyMessage;
import entity.MyMessage.MessageType;
import entity.Order;
import entity.Survey;
import entity.SurveyReport;
import entity.User;
import ocsf.server.ConnectionToClient;
import ocsf.server.ObservableServer;

/**
 * Server class that handles all communication with clients and fetches db data for them
 * @author wardz
 *
 */
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

	/**
	 * server state referring to if the server is active or no
	 */
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

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	// Getters********************************

	/**
	 * get my ipv4 address
	 * @return
	 */
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
			DBController.logOutAll();
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
			DBController.logOutAll();
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
		synchronized (ThreadController.lock) {
			handleRequest(msg, client);
		}
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
		case SEND:
			handleSendRequest(clMsg);
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

	/**
	 * handles all info type messages 
	 * @param clMsg mymessage
	 * @param client sender of message
	 */
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
	 * handles clients' GET requests in http style messages look loke:
	 * "order/by/id_order/xxx"
	 * 
	 * @param MyMessage Contains GET request
	 */

	private void handleGetRequest(MyMessage clMsg, ConnectionToClient client) {
		String[] request = clMsg.getInfo().split("/");

		switch (request[0]) {
		case "login":
			User user = (User) clMsg.getContent();
			if (request[1].equals("user")) {
				clMsg.setContent(DBController.getUser(user.getUsername(), user.getPassword()));

			} else if (request[1].equals("customer")) {
				clMsg.setContent(DBController.getCustomerBy("id_customer", user.getIdCustomer() + ""));
			}
			break;
		case "user":
			if (request[1].equals("all")) {

			} else if (request[1].equals("by"))
				clMsg.setContent(DBController.getUserBy(request[2], request[3]));
			break;
		case "order":
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getOrdersAll());
			} else if (request[1].equals("income")) {
				if (request[2].equals("quarter")) {
					clMsg.setContent(DBController.getIncomesInQuarter(request[3], request[4], request[5]));
				}
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
			break;
		case "item":
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getItemsAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getItemsBy(request[2], request[3]));
			} else if (request[1].equals("amount")) {
				clMsg.setContent(DBController.getAmountOfEveryItem(request[2], request[3], request[4]));
			} else if (request[1].equals("complete")) {
				clMsg.setContent(DBController.getItemsComplete());
			}
			break;
		case "build_item":
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
			break;
		case "item_in_build":
			clMsg.setContent(DBController.getItemInBuildAll((BuildItem) clMsg.getContent()));
			break;
		case "store":
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getStoreAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getStoreBy(request[2], request[3]));
			}
			break;
		case "category":
			if (request[1].equals("all"))
				clMsg.setContent(DBController.getCategoryAll());
			else if (request[1].equals("by")) {
				if (request[2].equals("type"))
					clMsg.setContent(DBController.getCategoryByType(request[3]));
			}
			break;
		case "type":
			if (request[1].equals("all"))
				clMsg.setContent(DBController.getTypeAll());
			break;
		case "customer":
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getCustomerAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getCustomerBy(request[2], request[3]));
			} else if (request[1].equals("point"))
				clMsg.setContent(DBController.getPoints(request[2]));
			break;
		case "worker":
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getWorkerAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getWorkerBy(request[2], request[3]));
			}
			break;
		case "complaint":
			if (request[1].equals("all")) {
				clMsg.setContent(DBController.getComplaintsAll());
			} else if (request[1].equals("by")) {
				clMsg.setContent(DBController.getComplaintsBy(request[2], request[3]));
			} else if (request[1].equals("years")) {
				System.out.println("here");
				clMsg.setContent(DBController.getComplaintYears());
			} else if (request[1].equals("count")) {
				if (request[2].equals("inQuarter")) {
					clMsg.setContent(DBController.getCountComplaintsInQuarter(request[3], request[4]));
				}
			} else if (request[1].equals("years")) {
				clMsg.setContent(DBController.getComplaintYears());
			} else if (request[1].equals("count")) {
				if (request[2].equals("inQuarter")) {
					clMsg.setContent(DBController.getCountComplaintsInQuarter(request[3], request[4]));
				}
			}
			break;
		case "questions":
			if (request[1].equals("all"))
				clMsg.setContent(DBController.getAllSurves());
			break;
		case "survey":
			if (request[1].equals("date_survey"))
				clMsg.setContent(DBController.getAllSurvesYears());
			else if (request[1].equals("by") && request[2].equals("date_survey && id_question_average"))
				clMsg.setContent(DBController.getAverage(request[3], request[4]));
			else if (request[1].equals("years")) {
				clMsg.setContent(DBController.getAllSurvesYears1());
			} else if (request[1].equals("idByYear")) {
				clMsg.setContent(DBController.getQuestionIDsByYear(request[2]));
			}
			break;
		case "report":
			clMsg.setContent(DBController.getReportOfYearAndQuestionID(request[1], request[2]));
			break;
		default:
			ServerView.printErr(getClass(), "Unhandled Get request: " + clMsg.getInfo());
			break;
		}
	}

	/**
	 * handle all post requests that insert into the database
	 * @param clMsg mymessage
	 * @param client sender
	 */
	private void handlePostRequest(MyMessage clMsg, ConnectionToClient client) {

		String[] request = clMsg.getInfo().split("/");

		if (request[0].equals("order")) {
			Order o = (Order) clMsg.getContent();
			clMsg.setContent(DBController.insertOrder(o));
		} else if (request[0].equals("complaint")) {
			Complaint complaint = (Complaint) clMsg.getContent();
			clMsg.setContent(DBController.insertComplaint(complaint));

		} else if (request[0].equals("survey")) {
			Survey s = (Survey) clMsg.getContent();
			clMsg.setContent(DBController.insertSurvey(s));
		} else if (request[0].equals("item")) {
			Item i = (Item) clMsg.getContent();
			clMsg.setContent(DBController.insertItem(i));
		} else if (request[0].equals("report")) {
			SurveyReport sr = (SurveyReport) clMsg.getContent();
			clMsg.setContent(DBController.insertReportPDF(sr));
		} else if (request[0].equals("customer")) {
			// "customer/`card_number`,content=user"
			User u = (User) clMsg.getContent();
			clMsg.setContent(DBController.insertCustomer(u, request[1]));
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

		if (request[0].equals("user")) {
			User u = (User) clMsg.getContent();
			if (request[1].equals("type"))
				clMsg.setContent(DBController.updateUserType(u));

		} else if (request[0].equals("order")) {
			Order order = (Order) clMsg.getContent();
			if (request[1].equals("status")) {
				clMsg.setContent(DBController.updateOrderStatus(order));
			} else if (request[1].equals("DeliveryDate")) {
				clMsg.setContent(DBController.updateOrderDeliveryDate(order));
			}
		} else if (request[0].equals("customer")) {
			Customer c = (Customer) clMsg.getContent();
			if (request[1].equals("status")) {
				clMsg.setContent(DBController.updateCustomerStatusOne(c));
			} else if (request[1].equals("point"))
				clMsg.setContent(
						DBController.updatePoint(Integer.parseInt(request[2]), Double.parseDouble(request[3])));
		} else if (request[0].equals("complaint")) {
			Complaint complaint = (Complaint) clMsg.getContent();
			clMsg.setContent(DBController.updateComplaint(complaint));
		} else if (request[0].equals("item")) {
			Item i = (Item) clMsg.getContent();
			if (request[1].equals("edit")) {
				clMsg.setContent(DBController.updateEditItem(i));
			} else if (request[1].equals("status")) {
				clMsg.setContent(DBController.updateItemStatus(i));
			}

		} else
			ServerView.printErr(getClass(), "Unhandled Update Request: " + clMsg.getInfo());
	}

	/**
	 * handles ending requests that send emails to users
	 * @param clMsg
	 */
	private void handleSendRequest(MyMessage clMsg) {
		String[] request = clMsg.getInfo().split("/");
		if (clMsg.getInfo().startsWith("email")) {
			Email email = (Email) clMsg.getContent();
			 EmailController.sendEmail(email);
		}
		if (clMsg.getInfo().startsWith("ReminderEmail")) {

			 ThreadController.ComplainTrackingfunction(request[1], request[2]);
		}
	}
}