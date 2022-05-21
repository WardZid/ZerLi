package control;

import java.io.IOException;
import java.util.ArrayList;

import boundary.ClientView;
import boundary.fxmlControllers.BranchManagerIncomeReportsController;
import boundary.fxmlControllers.ClientConsoleController;
import entity.Customer;
import entity.MyMessage;
import entity.User;
import entity.MyMessage.MessageType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ocsf.client.ObservableClient;

public class ClientController extends ObservableClient {

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	static boolean isConnected = false;

	private static short msgCnt = 0;

	private boolean awaitResponse = false;

	private Object replyContent = null;

	public ClientController(String host, int port) {
		super(host, port);
	}

	/**
	 * open connection to server
	 * 
	 * @return true if connection successful
	 */
	public boolean connectToServer(String host) {
		setHost(host);
		try {
			if (!isConnected) {
				openConnection();
				send(MessageType.INFO, "/connect", null);
			}
		} catch (IOException e) {
			MainController.printErr(getClass(), "Connection to server unsuccessful");
		}
		return isConnected;
	}

	/**
	 * disconnect from server
	 */
	public boolean disconnectFromServer() {
		try {
			if (isConnected) {
				send(MessageType.INFO, "/disconnect", null);
				closeConnection();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// should return the opposite of isConnected because we should not be connected
		// anymore
		return !isConnected;
	}

	private boolean disconnectNoMessage() {
		try {
			if (isConnected) {
				closeConnection();
				isConnected = false;
				ClientView.setUpConnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isConnected;
	}

	@Override
	protected void connectionEstablished() {
		super.connectionEstablished();
		MainController.print(getClass(),
				"Connection established on [host:port]: [" + getHost() + "," + getPort() + "]");
	}

	@Override
	protected void connectionClosed() {
		super.connectionClosed();
		MainController.print(getClass(), "Connection terminated");
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public Object send(MessageType type, String info, Object content) {
		this.replyContent = null;
		try {
			// openConnection();// in order to send more than one message
			awaitResponse = true;
			MyMessage msg = new MyMessage(getHost(), msgCnt++, type, info, content);
			sendToServer(msg);
			MainController.print(getClass(), "-> " + msg.toString());

			// wait for response
			int timeoutCounter = 0;
			while (awaitResponse) {
				try {
					if (timeoutCounter >= 200) {
						timeOut();
					}

					timeoutCounter++;
					Thread.sleep(100);
					MainController.print(getClass(),
							"Awaiting Response -> [" + msg.getMsgID() + "] info=" + msg.getInfo());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			MainController.print(getClass(),
					"Response Received <- [" + msg.getMsgID() + "] info=" + msg.getInfo() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			MainController.print(getClass(), "Could not send message to server: Terminating client." + e);
		}
		return replyContent;
	}

	private void timeOut() {
		MainController.printErr(getClass(), "Client timed out. Server Unreachable.");
		disconnectNoMessage();

		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText(null);
		errorAlert.setContentText("Timed out, server Unreachable.");
		errorAlert.showAndWait();
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		super.handleMessageFromServer(msg);
		MainController.print(getClass(), "<- " + msg.toString());
		handleServerMessage(msg);
	}

	private void handleServerMessage(Object msg) {
		if (!(msg instanceof MyMessage)) {
			return;
		}
		MyMessage svMsg = (MyMessage) msg;
		if (svMsg.getClientAddress() == null) {
			MainController.print(getClass(), "Global Message from server: " + svMsg.getInfo());
			handleInfoMessage(svMsg);
			return;
		}
		if (!svMsg.getClientAddress().equals(getHost())) {
			System.out.println("Message not for me");
			return;
		}
		awaitResponse = false;

		switch (svMsg.getType()) {
		case INFO:
			handleInfoMessage(svMsg);
			break;
		case GET:
			handleGetReply(svMsg);
			break;
		case POST:

			break;
		case UPDATE:
			handleUpdateReply(svMsg);
			break;
		case DELETE:

			break;

		default:
			break;
		}

	}

	private void handleInfoMessage(MyMessage svMsg) {
		if (svMsg.getInfo().startsWith("/global/stop")) {
			/*
			 * any action that needs to be done before closing
			 */
			disconnectNoMessage();

		} else if (svMsg.getInfo().startsWith("/disconnect"))
			isConnected = false;
		else if (svMsg.getInfo().startsWith("/connect"))
			isConnected = true;
		else
			MainController.print(getClass(), "Unhandled info:" + svMsg.getInfo());
	}

	private void handleGetReply(MyMessage svMsg) {

		//String[] reply = svMsg.getInfo().split("/");
		replyContent=svMsg.getContent();
		
//		
//		else if (request[0].equals("order")) {
//			
//			
//			if (request[1].equals("all")) {
//				clMsg.setContent(DBController.getOrdersAll());
//			} 
//			
//			else if (request[1].equals("by")) {
//				clMsg.setContent(DBController.getOrdersBy(request[2], request[3]));
//			} 
//			
//			
//			else if(request[1].equals("report")) { 
//
//				
//				if(request[2].equals("sale")) {
//					
//					
//					if(request[3].equals("months")) {
//						
//						
//						clMsg.setContent(DBController.getOrderReportMonths(request[4]));
//					}
//				}
//			}
//		}
//
//		String[] reply = svMsg.getInfo().split("/");
//		if (reply[0].equals("login")) {
//			if (reply[1].equals("user")) {
//				ClientConsoleController.setUser((User) svMsg.getContent());
//
//			}
//			if (reply[1].equals("customer")) {
//				ClientConsoleController.setCustomer((Customer) svMsg.getContent());
//			}
//		} else if (reply[0].equals("order")) {
//			
//			
//			if (reply[1].equals("all")) {
//				//do something with all orders
//			} 
//			
//			else if (reply[1].equals("by")) {
////				clMsg.setContent(DBController.getOrdersBy(request[2], request[3]));
//			} 
//			
//			
//			else if(reply[1].equals("report")) { 
//
//				
//				if(reply[2].equals("sale")) {
//					
//					
//					if(reply[3].equals("months")) {
//						BranchManagerIncomeReportsController.setMonthsYears((ArrayList<String>)svMsg.getContent());
////						clMsg.setContent(DBController.getOrderReportMonths(request[4]));
//					}
//				}
//			}
//		} 
//		else {
//			MainController.print(getClass(), "Unhandled Get:" + svMsg.getInfo());
//		}
//>>>>>>> branch 'master' of https://github.com/WardZid/Assignment3.git
	}

	private void handleUpdateReply(MyMessage svMsg) {
//		if (svMsg.getInfo().startsWith("/order"))
//			return;
////			ClientFXMLController.putOrders((ArrayList<Order>) svMsg.getContent());
//		else {
//			MainController.print(getClass(), "Unhandled Update:" + svMsg.getInfo());
//		}
	}

}
