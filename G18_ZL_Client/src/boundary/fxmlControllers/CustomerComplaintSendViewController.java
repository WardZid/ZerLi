package boundary.fxmlControllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class CustomerComplaintSendViewController implements Initializable {
	@FXML
	private TextArea descriptionTA;
	private static String complaint;

	public static String getComplaint() {
		return complaint;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		descriptionTA.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				complaint = descriptionTA.getText();
				if (descriptionTA.getText().equals(""))
					CustomerComplaintsController.finishButton.setDisable(true);
				else
					CustomerComplaintsController.finishButton.setDisable(false);
			}
		});
	}
}
