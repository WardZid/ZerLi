package boundary;

import java.io.IOException;

import boundary.fxmlControllers.ServerViewController;
import control.MainController;
import control.ThreadController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * javafx applcation class th starts te gui
 * @author wardz
 *
 */
public class ServerView extends Application {
	/**
	 *  static instance of the controller to pass it any info it might need
	 */
	private static ServerViewController svFXController;
	/**
	 * log print counter
	 */
	private static int printCnt = 1;
	/**
	 * calls the static function for starting the gui 
	 * @param args
	 */
	public static void launchApplication(String[] args) {
		ServerView.launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		try {
			// fetch pre-made fxml file that details the gui's appearance
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/server-view.fxml"));
			Parent root = loader.load();

			// fetch the static controller instance from the FXML loader
			svFXController = loader.getController();

			primaryStage.setTitle("Server Console"); // window title
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/boundary/fxmlControllers/server.css");
			primaryStage.getIcons().add(new Image("/boundary/media/server_icon.png"));
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	@Override
	/**
	 * This method is closed when the application is closed. Its purpose is to stop
	 * the server and make sure all closing procedures are accomplished
	 * 
	 */
	public void stop() {
		
		ThreadController.stopTimers();
		MainController.getServer().stopServer();
	}

	/**
	 * prints messages to the log
	 * @param from
	 * @param msg
	 */
	public static void print(Class<?> from, String msg) {
		String outputString="<" + (printCnt++) + ">  \t[" + from.getName() + "]:\t" + msg;
		svFXController.printLog(outputString);
		System.out.println(outputString);
	}

	/**
	 * prints error messages to the log
	 * @param from
	 * @param msg
	 */
	public static void printErr(Class<?> from, String msg) {
		String outputString="<" + (printCnt++) + ">  \t[" + from.getName() + "]:\t" + msg;
		svFXController.printErrLog(outputString);
		System.err.println(outputString);
	}

}
