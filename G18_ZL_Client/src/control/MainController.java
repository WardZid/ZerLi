package control;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import boundary.ClientView;

public class MainController {

	// main
	public static void main(String[] args) {
		myClient =new ClientController(getIpAddress(), ClientController.DEFAULT_PORT);
		ClientView.launchApplication(args);
	}

	// Class Variables
	
	/**
	 * instance of the client
	 */
	private static ClientController myClient;

	/**
	 * default ip address to be added to the connect page's textfield
	 */
	private static String ipAddress;

	//Getters
	/**
	 * 
	 * @return instance of my client
	 */
	public static ClientController getMyClient() {
		return myClient;
	}
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
	
	public static String currentTime() {
		//current time is formatted into an appropriate datetime for mysql
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //formats date and time to be suitable for sql
		Date date = new Date(System.currentTimeMillis());  
		System.out.println("user logged at: "+formatter.format(date)); 
		return formatter.format(date);
	}
	
	
}
