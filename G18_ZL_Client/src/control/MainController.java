package control;

import java.net.InetAddress;
import java.net.UnknownHostException;

import boundary.ClientView;

public class MainController {

	// main
	public static void main(String[] args) {
		myClient = ClientController.getInstance(getIpAddress(), ClientController.DEFAULT_PORT);
		ClientView.launchApplication(args);
	}

	// Class Variables
	
	/**
	 * instance of the client
	 */
	public static ClientController myClient;

	/**
	 * default ip address to be added to the connect page's textfield
	 */
	private static String ipAddress;

	/**
	 * console log counter
	 */
	private static int printCnt = 1;

	//Getters
	/**
	 * pulls the ip of its own pc to be used as the server's ip to connect to
	 * @return <String> ip address of the this pc
	 */
	public static String getIpAddress() {
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			ipAddress = "10.10.8.135";
			e.printStackTrace();
		}
		return ipAddress;
	}
	
	// Helpful Generic Methods
	/**
	 * Prints standard info messages
	 * @param from class that wants to print
	 * @param msg simple info message
	 */
	public static void print(Class<?> from, String msg) {
		System.out.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}
	
	/**
	 * 
	 * @param from class that wants to print
	 * @param msg simple error message
	 */
	public static void printErr(Class<?> from, String msg) {
		System.err.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}
}
