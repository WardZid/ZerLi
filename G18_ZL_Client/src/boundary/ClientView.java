package boundary;

import java.io.IOException;

import control.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientView extends Application {

	// Screen size properties
//	private static double width;
//	private static double height;
//
//	private static double centerW;
//	private static double centerH;

	public static Parent connect = null;
	public static Parent logIn = null;
	public static Parent clientConsole = null;

	private static Stage primaryStage;
	public static Scene primaryScene;

	public static void launchApplication(String[] args) {

		ClientView.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
//
//			Rectangle2D screenBounds = Screen.getPrimary().getBounds();
//			width = screenBounds.getWidth();
//			height = screenBounds.getHeight();
//			centerW = width / 2;
//			centerH = height / 2;

			ClientView.primaryStage = primaryStage;

			setUpScenes();

			setUpStage(primaryScene);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * This method is closed when the application is closed. Its purpose is to stop
	 * the client connection and make sure all closing procedures are accomplished
	 * 
	 */
	public void stop() {
		MainController.getMyClient().disconnectFromServer();
	}

	private void setUpScenes() throws IOException {
		connect = FXMLLoader.load(getClass().getResource("fxmls/connect-view.fxml"));
		primaryScene = new Scene(connect);
	}

	private void setUpStage(Scene scene) {
		scene.getStylesheets().add("/boundary/fxmlControllers/client.css");
		primaryStage.setTitle("Client Console"); // window title
		primaryStage.getIcons().add(new Image("/boundary/media/client_icon.png"));
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	public static void setUpConnect() {
		if (connect == null)
			try {
				connect = FXMLLoader.load((ClientView.class).getResource("fxmls/connect-view.fxml"));
			} catch (IOException e) {
				MainController.printErr(ClientView.class, "Could not fetch 'connect' FXML");
			}
		primaryScene.setRoot(connect);
		primaryStage.sizeToScene();
	}

	public static void setUpLogIn() {

		try {
			logIn = FXMLLoader.load((ClientView.class).getResource("fxmls/log-in-view.fxml"));
		} catch (IOException e) {
			MainController.printErr(ClientView.class, "Could not fetch 'log in' FXML");
		}
		primaryScene.setRoot(logIn);
		primaryStage.sizeToScene();
	}

	public static void setUpClientConsole() {
		try {
			clientConsole = FXMLLoader.load((ClientView.class).getResource("fxmls/client-console-view.fxml"));
		} catch (IOException e) {
			MainController.print(ClientView.class, "Could not fetch 'client' FXML");
		}
		primaryScene.setRoot(clientConsole);
		primaryStage.sizeToScene();

	}

}
