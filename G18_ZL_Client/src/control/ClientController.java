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
	public void connectToServer(String host) throws Exception {
		setHost(host);
		try {
			if (!isConnected) {
				openConnection();
				send(MessageType.INFO, "connect", null);
			}
		} catch (IOException e) {
			ClientView.printErr(getClass(), "Connection to server unsuccessful");
			throw e;
		}
	}

	/**
	 * disconnect from server
	 */
	public boolean disconnectFromServer() {
		try {
			if (isConnected) {
				send(MessageType.INFO, "disconnect", null);
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
		ClientView.print(getClass(),
				"Connection established on [host:port]: [" + getHost() + "," + getPort() + "]");
	}

	@Override
	protected void connectionClosed() {
		super.connectionClosed();
		ClientView.print(getClass(), "Connection terminated");
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
			ClientView.print(getClass(), "-> " + msg.toString());

			// wait for response
			int timeoutCounter = 0;
			while (awaitResponse) {
				try {
					if (timeoutCounter >= 200) {
						awaitResponse=false;
						timeOut();
					}
					timeoutCounter++;
					
					
					Thread.sleep(100);
					
					ClientView.print(getClass(),
							"Awaiting Response -> [" + msg.getMsgID() + "] info=" + msg.getInfo());
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			ClientView.print(getClass(),
					"Response Received <- [" + msg.getMsgID() + "] info=" + msg.getInfo() + "\n");
			
		} catch (IOException e) {
			e.printStackTrace();
			ClientView.printErr(getClass(), "Could not send message to server: Terminating client." + e);
		}
		return replyContent;
	}

	private void timeOut() {
		ClientView.printErr(getClass(), "Client timed out. Server Unreachable.");
		disconnectNoMessage();

		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText(null);
		errorAlert.setContentText("Timed out, server Unreachable.");
		errorAlert.showAndWait();
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		super.handleMessageFromServer(msg);
		ClientView.print(getClass(), "<- " + msg.toString());
		handleServerMessage(msg);
	}

	private void handleServerMessage(Object msg) {
		if (!(msg instanceof MyMessage)) {
			return;
		}
		MyMessage svMsg = (MyMessage) msg;
		if (svMsg.getClientAddress() == null) {
			ClientView.print(getClass(), "Global Message from server: " + svMsg.getInfo());
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
		replyContent=svMsg.getContent();
		awaitResponse = false;
	}

	private void handleInfoMessage(MyMessage svMsg) {
		String[] reply=svMsg.getInfo().split("/");
		if (reply[0].equals("global")) {
			if (reply[1].equals("stop")) {

				/*
				 * any action that needs to be done before closing
				 */
				disconnectNoMessage();
			} else if(reply[1].equals("logout")) {
				javafx.application.Platform.runLater(() -> ClientView.setUpLogIn());
			}

		} else if (reply[0].equals("disconnect"))
			isConnected = false;
		else if (reply[0].equals("connect"))
			isConnected = true;
		else if (reply[0].equals("log")) {
//			replyContent=svMsg.getContent();
		}
		else
			ClientView.printErr(getClass(), "Unhandled info:" + svMsg.getInfo());
	}

	private void handleGetReply(MyMessage svMsg) {
//		replyContent=svMsg.getContent();
	}

	private void handleUpdateReply(MyMessage svMsg) {

	}

}
