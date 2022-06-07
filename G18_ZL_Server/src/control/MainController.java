package control;

import java.text.SimpleDateFormat;
import java.util.Date;

import boundary.ServerView;

/**
 * main class that starts the server
 * @author wardz
 *
 */
public class MainController {

	// main 
	/**
	 * main method that inits server instance (not listens just init)
	 * calls ui to start
	 * @param args
	 */
	public static void main(String[] args) {
		myServer = new ServerController(ServerController.DEFAULT_PORT);

		ServerView.launchApplication(args);

	}

	// Class Variables**********************
	/**
	 * static server instance
	 */
	private static ServerController myServer;

	// Getters*********************
	/**
	 * returns server instance
	 * @return
	 */
	public static ServerController getServer() {
		return myServer;
	}
	/**
	 * returns current time from pc
	 * @return string of time suitble for mysql DateTime
	 */
	public static String currentTime() {
		//current time is formatted into an appropriate datetime for mysql
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //formats date and time to be suitable for sql
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}



}
