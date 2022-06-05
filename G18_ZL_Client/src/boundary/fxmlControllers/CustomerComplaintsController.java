package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.ClientController;
import control.MainController;
import entity.Complaint;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/** 
 * This class overrides initialize method 
 * @author Aziz Hamed
 * This class is used in order to give the customer the opportunity to see his complaints history
 */
public class CustomerComplaintsController implements Initializable {
	
	/**
	 * List view that contain complaints ID
	 */
	@FXML
	private ListView<Integer> ComplaintsL;
	
	/**
	 * Text field to show the selected complaint ID from the list view
	 */
	@FXML
	private TextField ComplaintIdT;
	
	/**
	 * Text field to show the customer ID of the selected complaint ID from the list view
	 */

	@FXML
	private TextField customerIdT;
	
	/**
	 * Text field to show the date of the selected complaint ID
	 */

	@FXML
	private TextField dateT;
	
	/**
	 * Text area to show the description of the selected complaint ID
	 */

	@FXML
	private TextArea descriptionT;
	
	/**
	 * Refund text field to show the customer the refund that take for the selected complaint
	 */
	@FXML
	private TextField refundT;
	
	/**
	 * Reply text area to show the reply of the selected complaint
	 */

	@FXML
	private TextArea replyT;
	
	/**
	 * status text field to show the status of the selected complaint
	 */

	@FXML
	private TextField statusT;
	
	/**
	 * selectedComplaintId contains the selected Complaint ID
	 */
	private int selectedComplaintId;
	
	/**
	 * selectedComplaint contains the selected Complaint
	 */
	private Complaint selectedComplaint;
	
	/**
	 * complaint map is used to contain the complaints ID and the complaints itself (each Complaint ID point on the suit complaint object
	 */
	private HashMap<Integer, Complaint> complaints = new HashMap<>();
	
	/**
	 * initialize function is used to show the complaints ID on the list view and to create many listeners
	 */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeComplaintsList();
		/**
		 * complaints list view Listener is used to show the suitable information of the chosen complaint id in the text fields
		 */
		ComplaintsL.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				selectedComplaintId = ComplaintsL.getSelectionModel().getSelectedItem();
				// get complaint
				selectedComplaint = complaints.get(selectedComplaintId);
				fillingTexts();
			}

		});
	}
	/**
	 * This function is used to fill the text fields and the text areas according to the selected Complaint ID
	 */
	private void fillingTexts() {
		customerIdT.setText(Integer.toString(selectedComplaint.getIdCustomer()));
		ComplaintIdT.setText(Integer.toString(selectedComplaint.getIdComplaint()));
		statusT.setText(selectedComplaint.getStatus());
		dateT.setText(selectedComplaint.getDate());
		refundT.setText(Double.toString(selectedComplaint.getRefund()));
		descriptionT.setText(selectedComplaint.getComplaint());
		replyT.setText(selectedComplaint.getResponse());
	}
	/**
	 * This function is used to initialize the complaints ID list view
	 */
	private void initializeComplaintsList() {
		ArrayList<Complaint> complaintsList =  ComplaintQueryFromDB(MessageType.GET,null);
		for(int i=0 ; i<complaintsList.size() ; i++)
			complaints.put(complaintsList.get(i).getIdComplaint(), complaintsList.get(i));
		ComplaintsL.getItems().clear();
		ComplaintsL.getItems().addAll(complaints.keySet());
	}
	
	/**
	 * This function is used to ask for some specific queries
	 * @param messageType what we want from the server to do with the data base (UPDATE,POST,GET,INFO)
	 * @param complaint the complaint that we want to post, update or get
	 * @return Object from the data base
	 */

	private ArrayList<Complaint> ComplaintQueryFromDB(MessageType messageType, Complaint complaint){
		return (ArrayList<Complaint>) MainController.getMyClient().send(messageType, "complaint/by/id_customer/"+ ClientConsoleController.getCustomer().getIdCustomer(),complaint);
	}

}
