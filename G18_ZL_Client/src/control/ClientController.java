package control;

import java.io.IOException;

import boundary.ClientView;
import entity.MyMessage;
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
		awaitResponse = false;
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
		replyContent=svMsg.getContent();
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
