package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import control.MainController;
import entity.Complaint;
import entity.MyMessage.MessageType;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CustomerComplaintSendViewController implements Initializable {

    @FXML
    private TextField customerIDT;

    @FXML
    private Label wrongCustomerIDLabel;

	@FXML
	private TextArea descriptionTA;
	private static String complaint;
	private static int customerID;
	private static Label wrongLable;
	private ArrayList<Complaint> arrayList;
	private boolean isExistedCustomerID = false;
	public static String getComplaint() {
		return complaint;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wrongLable = wrongCustomerIDLabel;
		visableWrongCustomerIDLabel(false);
		//CustomerSupportController.finishButton.setDisable(false);
		descriptionTA.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				complaint = descriptionTA.getText();
				setDisableFinish(isExistedCustomerID, !descriptionTA.getText().equals(""));
			}
		});
		customerIDT.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					visableWrongCustomerIDLabel(false);
				} else {
				arrayList =	(ArrayList<Complaint>) MainController.getMyClient().send(MessageType.GET, "customer/by/id_customer/" + customerID,null);
				if(arrayList.size()!=0) {
					visableWrongCustomerIDLabel(false);
					isExistedCustomerID = true;
					setDisableFinish(isExistedCustomerID, !descriptionTA.getText().equals(""));
				}
				else {
					visableWrongCustomerIDLabel(true);
					isExistedCustomerID = false;
					setDisableFinish(isExistedCustomerID, !descriptionTA.getText().equals(""));
				}
				
				}
			}
		});
		customerIDT.textProperty().addListener(new ChangeListener<String>() {

			@Override
			 public void changed(ObservableValue<? extends String> observable, String oldValue, 
				        String newValue) {
				        if (!newValue.matches("\\d*")) {
				        	customerIDT.setText(newValue.replaceAll("[^\\d]", ""));
				        }
				        	else {
								visableWrongCustomerIDLabel(false);
								setDisableFinish(isExistedCustomerID, !descriptionTA.getText().equals(""));
				        	}
				        try {
				        customerID = Integer.parseInt(customerIDT.getText());
				        }catch(NumberFormatException e) {}
				        }
			
		});
	}
	private void setDisableFinish(boolean isExistedCustomer, boolean notEmptyDescription) {
		CustomerSupportController.finishButton.setDisable(!isExistedCustomer|| !notEmptyDescription);
	}
	public static void visableWrongCustomerIDLabel(boolean condition) {
		wrongLable.setVisible(condition);
	}
	public static int getCustomerID() {
		return customerID;
	}
}
