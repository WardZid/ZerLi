package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import control.MainController;
import entity.Complaint;
import entity.Customer;
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
/** 
 * This class overrides initialize method 
 * @author Aziz Hamed
 * This class is used in order to give adding complaint functionality
 */
public class CustomerComplaintSendViewController implements Initializable {
	
	/**
	 * customer ID text field is used to write the ID of the customer that complaints
	 */

    @FXML
    private TextField customerIDT;
    
    /**
     * wrongCustomerIDLabel is used to show the service worker wrong customer ID number if the customer ID that entered it don't exist in the data base
     */

    @FXML
    private Label wrongCustomerIDLabel;
    
    /**
     * descriptionTA is used to write the description of the complaint
     */

	@FXML
	private TextArea descriptionTA;
	/**
	 * complaint contains the description of the complaint
	 */
	private static String complaint;
	/**
	 * customerID contains the ID of the customer that complaint 
	 */
	private static int customerID;
	private static Label wrongLable;
	/**
	 * isExistedCustomerID = true if the customer ID exists and false if not
	 */
	private boolean isExistedCustomerID = false;
	/**
	 * This Function is used to return the description of the new Complaint
	 * @return the new description of the new Complaint
	 */
	public static String getComplaint() {
		return complaint;
	}
	/**
	 * initialize function to create many listeners and initialize many things
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wrongLable = wrongCustomerIDLabel;
		visableWrongCustomerIDLabel(false);
		//CustomerSupportController.finishButton.setDisable(false);
		/**
		 * description listener is used to save the current description of complaint and to disable the finish button if the text area contains null or ""
		 */
		descriptionTA.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				complaint = descriptionTA.getText();
				setDisableFinish(isExistedCustomerID, !descriptionTA.getText().equals(""));
			}
		});
		/**
		 * customer ID text field listener is used to check if the current written customer ID is existed in the data base
		 */
		customerIDT.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				ArrayList<Customer> customerList;
				if (newPropertyValue) {
					visableWrongCustomerIDLabel(false);
				} else {
					customerList =	(ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET, "customer/by/id_customer/" + customerID,null);
				if(customerList.size()!=0) {
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
		/**
		 * This Customer ID listener is used to prevent the user to enter letters and to disable finish button if the there is no existed customer ID is written in the text field
		 */
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
	/**
	 * This function is used to decide to enable or disable the finish button according to the parameters
	 * @param isExistedCustomer contains true if the customer ID is existed in the data base and false if not
	 * @param notEmptyDescription contains true if the description is not null and false if null
	 */
	private void setDisableFinish(boolean isExistedCustomer, boolean notEmptyDescription) {
		CustomerSupportController.finishButton.setDisable(!isExistedCustomer|| !notEmptyDescription);
	}
	/**
	 * This function is used to decide to make the label visable or not according to the sended parameter
	 * @param condition decide if the label has to be visible or not
	 */
	public static void visableWrongCustomerIDLabel(boolean condition) {
		wrongLable.setVisible(condition);
	}
	/**
	 * This function returns the customer ID
	 * @return The customer ID when finish button clicked
	 */
	public static int getCustomerID() {
		return customerID;
	}
}
