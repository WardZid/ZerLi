package control;

import java.text.SimpleDateFormat;
import java.util.Date;

import boundary.ServerView;

public class MainController {

	// main
	public static void main(String[] args) {
		myServer = new ServerController(ServerController.DEFAULT_PORT);

		ServerView.launchApplication(args);

	}

	// Class Variables**********************
	private static ServerController myServer;

	

	// Getters*********************
	public static ServerController getServer() {
		return myServer;
	}
	
	public static String currentTime() {
		//current time is formatted into an appropriate datetime for mysql
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //formats date and time to be suitable for sql
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}



}
