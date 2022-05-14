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

public class ConnectViewController implements Initializable {

    @FXML
    private TextField txtIP;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtIP.setText(MainController.getIpAddress());
	}
	
	@FXML
	void onConnect(ActionEvent event) {
		if (MainController.myClient.connectToServer(txtIP.getText())) {
			MainController.reqOrders();
			ClientView.setUpClient();
		}
		else {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("Unable to reach server");
			errorAlert.showAndWait();
		}
	}
}
