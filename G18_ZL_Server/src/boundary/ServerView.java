package boundary;

import java.io.IOException;

import boundary.fxmlControllers.ServerViewController;
import control.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ServerView extends Application {
	// static instance of the controller to pass it any info it might need
	@SuppressWarnings("unused")
	private static ServerViewController svFXController;

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
			//primaryStage.setResizable(false);
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
		MainController.getServer().stopServer();
		System.exit(0);
	}

}
