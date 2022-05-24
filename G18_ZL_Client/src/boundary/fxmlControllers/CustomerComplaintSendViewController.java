package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class CustomerComplaintSendViewController implements Initializable {

    @FXML
    private Label DescriptionL;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button backButton;

    @FXML
    private TextArea descriptionT;

    @FXML
    private Button sendComplaintButton;

    @FXML
    private VBox vb1;

    @FXML
    private VBox vb2;
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	sendComplaintButton.setDisable(true);
    	descriptionT.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	if(descriptionT.getText().equals(""))
		    		sendComplaintButton.setDisable(true);
		    	else
		    		sendComplaintButton.setDisable(false);
		    }
		});	}

    
    @FXML
    void onBack(ActionEvent event) {
    	

    }

    @FXML
    void onSend(ActionEvent event) { 
    	//send complaint to server
    	descriptionT.clear();
    }

	

}

