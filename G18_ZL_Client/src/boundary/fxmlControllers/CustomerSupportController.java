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
import control.MainController;
import entity.Complaint;
import entity.Email;
import entity.MyMessage.MessageType;
import entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/** 
 * This class overrides initialize method 
 * @author Aziz Hamed
 * This class is used in order to give add new complaint and sending reply to complaints functionalities
 */
public class CustomerSupportController implements Initializable{
	/**
	 * The complaint ID textField to show the complaint ID
	 */
	@FXML
	private TextField ComplaintIdT;
	/**
	 * The complaints listView this list view contains complaints ID
	 */

	@FXML
	private ListView<Integer> ComplaintL;
	/**
	 * The customer ID textField to show the customer ID
	 */

	@FXML
	private TextField CustomerIdT;
	/**
	 * Enter new Complaint button is used to open new dialog box which there the survey worker can enter new Complaint for specific customer
	 */
	@FXML
    private Button enterNewComplaintButton;
	/**
	 * The date textField to show the complaint date
	 */

	@FXML
	private TextField dateT;
	/**
	 * The description textFeild to show the complaint (what the customer says)
	 */

	@FXML
	private TextArea descriptionT;
	/**
	 * refund textArea is used to enter refund if the survey worker decided to refund the customer
	 */

	@FXML
	private TextField refundT;
	/**
	 * The reply textField to enter the reply on the complaint 
	 */

	@FXML
	private TextArea replyT;
	/**
	 * Send Reply Button is used to send reply to the customer
	 */
	@FXML
	private Button sendReplyButton;
	/**
	 * Status textField is used to show the status of the complaint
	 */

	@FXML
	private TextField statusT;
	/**
	 * selectedComplaintId contains the current complaint id that the survey worker choose from the list view
	 */
	private int selectedComplaintId;
	/**
	 * selectedComplaint contains the current complaint that the survey worker choose from the list
	 */
	private Complaint selectedComplaint;
	/**
	 * The finish button is placed in the dialog box and is used to send the new complaint to the server
	 */
	public static Node finishButton;
	/**
	 * complaint map is used to contain the complaints ID and the complaints itself (each Complaint ID point on the suit complaint object)
	 */
	private static HashMap<Integer, Complaint> ComplaintMap = new HashMap<>();
	/**
	 * initialize function is used to show the complaints ID on the list view and to create many listeners
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setComplaintsListView();
		setEditable(false);
		sendReplyButton.setDisable(true);
		/**
		 * change listener on the reply text field to disable reply button if there is no text in the reply text field
		 */
		replyT.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		       if(!replyT.getText().equals(""))
		    	   sendReplyButton.setDisable(false);
		       else
		    	   sendReplyButton.setDisable(true);
		    }
		});
		/**
		 * Listener on refund text field is used to prevent the survey worker to enter letters 
		 */
		refundT.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            refundT.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		/**
		 * complaints list view Listener is used to show the suitable information of the chosen complaint id in the text fields
		 */
		ComplaintL.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				try {
					refundT.setText("");
					selectedComplaintId = ComplaintL.getSelectionModel().getSelectedItem();
					selectedComplaint = ComplaintMap.get(selectedComplaintId);
					setTexts();
					setEditable(true);
					replyT.clear();
				}catch(NullPointerException e) {}
					
				}
		});
	}
	/**
	 * This function is used to make the refund text field and the reply text field editable if currently the survey worker point on specific complaint id
	 * @param toEdit  toEdit = true if the store worker point on complaint id and false if not
	 */

	private void setEditable(boolean toEdit) {
		refundT.setEditable(toEdit);
		replyT.setEditable(toEdit);
	}
	/**
	 * This function is used to set the suitable texts in the text fields according to the complaint id that choosed
	 */
	private void setTexts() {
		ComplaintIdT.setText(Integer.toString(selectedComplaintId));
		CustomerIdT.setText(Integer.toString(selectedComplaint.getIdCustomer()));
		statusT.setText(selectedComplaint.getStatus());
		dateT.setText(selectedComplaint.getDate());
		descriptionT.setText(selectedComplaint.getComplaint());
	}
	/**
	 * This function is used to initialize the complaints list view (ask the server for all open complaints and enter the ID of all the returned complaints to the list view
	 */
	private void setComplaintsListView() {
		ArrayList<Complaint> complaintsList =  (ArrayList<Complaint>)ComplaintQueryFromDB(MessageType.GET,null);
		for(int i=0 ; i<complaintsList.size() ; i++)
		ComplaintMap.put(complaintsList.get(i).getIdComplaint(), complaintsList.get(i));
		ComplaintL.getItems().clear();
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
	}
	/**
	 * This function is start when the survey workers click on send reply button (set the reply in the suit complaint object and send the this object to the server)
	 * @param event 
	 */

	public void onSendReply(ActionEvent event) {
		if(refundT.getText().equals(""))
			refundT.setText("0");
		MainController.getMyClient().send(MessageType.UPDATE,"customer/point/"+ CustomerIdT.getText() + "/" + refundT.getText() ,null);
		selectedComplaint.setRefund(Integer.parseInt(refundT.getText()));
		selectedComplaint.setResponse(replyT.getText());
		MainController.getMyClient().send(MessageType.UPDATE, "complaint",selectedComplaint);
		ComplaintMap.remove(selectedComplaintId);
		User user = (User)MainController.getMyClient().send(MessageType.GET,"user/by/id_customer/" + CustomerIdT.getText(),null);
		Email email=new Email(user.getEmail(), "The treatment of complaint ["+selectedComplaintId+"] has been finished!", "Your Complaint has been treated\nComplaint Reply:\n"+ replyT.getText() +"\n" + "refund " + refundT.getText());
		MainController.getMyClient().send(MessageType.SEND, "email", email);
		clearTexts();
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
		setEditable(false);
		sendReplyButton.setDisable(true);
	}

	private void clearTexts() {
		ComplaintL.getItems().clear();
		CustomerIdT.clear();
		ComplaintIdT.clear();
		statusT.clear();
		dateT.clear();
		refundT.clear();
		descriptionT.clear();
		replyT.clear();
	}
	/**
	 * This function will start when the store worker click on Enter new Complaint button then will open dialog box to enter the new Complaint information
	 * @param event
	 * @throws IOException is thrown if the load of the dialog box failed
	 */
	
	@FXML
	public void onEnter(ActionEvent event) throws IOException {
		Dialog<ButtonType> dialog = LoadDialogPane();
		Optional<ButtonType> clickedButton = dialog.showAndWait();
		if (clickedButton.get() == ButtonType.FINISH) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Complaint newComplaint = new Complaint(CustomerComplaintSendViewController.getCustomerID(),
					dtf.format(now), CustomerComplaintSendViewController.getComplaint());
		 ComplaintQueryFromDB(MessageType.POST, newComplaint);
			initialize(null,null);
			dialog.close();
		}
		else 
			dialog.close();
		
	}
	/**
	 * This function is used to load the dialog box and open it
	 * @return the dialog pane 
	 * @throws IOException is thrown if the load of the dialog box failed
	 */
	private Dialog<ButtonType> LoadDialogPane() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(
		ClientView.class.getResource("/boundary/fxmls/customer-complaints-send-view.fxml"));
		DialogPane pane = fxmlLoader.load();
		finishButton = pane.lookupButton(ButtonType.FINISH);
		finishButton.setDisable(true);
		pane.lookupButton(ButtonType.FINISH).setDisable(true);
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setDialogPane(pane);
		return dialog;
	}
	/**
	 * This function is used to ask for some specific queries
	 * @param messageType what we want from the server to do with the data base (UPDATE,POST,GET,INFO)
	 * @param complaint the complaint that we want to post, update or get
	 * @return Object from the data base
	 */
	private Object ComplaintQueryFromDB(MessageType messageType, Complaint complaint){
		return  MainController.getMyClient().send(messageType, "complaint/by/status_complaint/OPEN",complaint);
	}

}