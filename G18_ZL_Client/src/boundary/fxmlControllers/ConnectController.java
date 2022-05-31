package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class ConnectController implements Initializable {

	@FXML
	/**
	 * TextField to entered desired ip
	 */
	private TextField txtIP;

	@Override
	/**
	 * inits textfield with ip
	 */
	public void initialize(URL location, ResourceBundle resources) {
		txtIP.setText(MainController.getIpAddress());
	}

	@FXML
	/**
	 * attempts t connect, if server is unreachable, shows dialog box with reason
	 * 
	 * @param event
	 */
	void onConnect(ActionEvent event) {
		try {
			MainController.getMyClient().connectToServer(txtIP.getText());
			ClientView.setUpLogIn();
		} catch (Exception e) {

			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText(e.getMessage());
			errorAlert.showAndWait();
		}
	}
}
