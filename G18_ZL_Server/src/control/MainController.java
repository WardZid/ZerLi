package control;

import boundary.ServerView;

public class MainController {

	// main
	public static void main(String[] args) {
		myServer = new ServerController(ServerController.DEFAULT_PORT);

		ServerView.launchApplication(args);

	}

	// Class Variables**********************
	private static ServerController myServer;
	private static int printCnt = 1;

	

	// Getters*********************
	public static ServerController getServer() {
		return myServer;
	}


	public static void print(Class<?> from, String msg) {
		System.out.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}

	public static void printErr(Class<?> from, String msg) {
		System.err.println("<" + (printCnt++) + ">\t[" + from.getName() + "]:\t" + msg);
	}

}
