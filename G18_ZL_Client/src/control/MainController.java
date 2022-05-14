package control;

import java.net.InetAddress;
import java.net.UnknownHostException;

import boundary.ClientView;
import entity.MyMessage.MessageType;

public class MainController {

	// main
	public static void main(String[] args) {
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			ipAddress = "10.10.8.135";
			e.printStackTrace();
		}
		myClient = ClientController.getInstance(ipAddress, ClientController.DEFAULT_PORT);
		ClientView.launchApplication(args);
	}

	// Class Variables
	public static ClientController myClient;

	private static String ipAddress;

	private static int printCnt = 1;

	//Getters
	public static String getIpAddress() {
		return ipAddress;
	}
	
	// Helpful Generic Methods
	public static void print(Class<?> from, String msg) {
		System.out.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}
	
	public static void printErr(Class<?> from, String msg) {
		System.err.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}
}
