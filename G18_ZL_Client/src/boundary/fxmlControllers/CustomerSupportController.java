package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.ClientController;
import control.MainController;
import entity.Complaint;
import entity.MyMessage;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CustomerSupportController implements Initializable {
	@FXML
	private Label ComplaintIdL;

	@FXML
	private TextField ComplaintIdT;

	@FXML
	private ListView<Integer> ComplaintL;

	@FXML
	private Label CustomerIdL;

	@FXML
	private TextField CustomerIdT;

	@FXML
	private Label StatusL;

	@FXML
	private Label dateL;

	@FXML
	private TextField dateT;

	@FXML
	private Label descriptionL;

	@FXML
	private TextArea descriptionT;

	@FXML
	private Label refundL;

	@FXML
	private TextField refundT;

	@FXML
	private Label replyL;

	@FXML
	private TextArea replyT;

	@FXML
	private Button sendReplyButton;

	@FXML
	private TextField statusT;
	@FXML
    private Button refresh;
	private int selectedComplaintId;
	private Complaint selectedComplaint;
	private static HashMap<Integer, Complaint> ComplaintMap = new HashMap<>();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setComplaintsListView();
		setEditable(false);
		sendReplyButton.setDisable(true);
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
		refundT.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            refundT.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		ComplaintL.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				try {
					selectedComplaintId = ComplaintL.getSelectionModel().getSelectedItem();
					selectedComplaint = ComplaintMap.get(selectedComplaintId);
					setTexts();
					// descriptionT.setDisable(true);
					setEditable(true);
					replyT.clear();
				}catch(NullPointerException e) {}
					
				}
		});
	}

	private void setEditable(boolean toEdit) {
		refundT.setEditable(toEdit);
		replyT.setEditable(toEdit);
	}
	private void setTexts() {
		ComplaintIdT.setText(Integer.toString(selectedComplaintId));
		CustomerIdT.setText(Integer.toString(selectedComplaint.getIdCustomer()));
		statusT.setText(selectedComplaint.getStatus());
		dateT.setText(selectedComplaint.getDate());
		descriptionT.setText(selectedComplaint.getComplaint());
	}


	private void setComplaintsListView() {
		ArrayList<Complaint> complaintsList =  ComplaintQueryFromDB(MessageType.GET,null);
		for(int i=0 ; i<complaintsList.size() ; i++)
		ComplaintMap.put(complaintsList.get(i).getIdComplaint(), complaintsList.get(i));
		ComplaintL.getItems().clear();
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
	}

	public void onSendReply(ActionEvent event) {
		selectedComplaint.setRefund(Integer.parseInt(refundT.getText()));
		selectedComplaint.setResponse(replyT.getText());
		MainController.getMyClient().send(MessageType.UPDATE, "complaint",selectedComplaint);
		ComplaintMap.remove(selectedComplaintId);
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
	public void onRefresh(ActionEvent event) {
		ArrayList<Complaint> complaintsList =  ComplaintQueryFromDB(MessageType.GET,null);
		setComplaintsListView();
	}
	
	private ArrayList<Complaint> ComplaintQueryFromDB(MessageType messageType, Complaint complaint){
		return (ArrayList<Complaint>) MainController.getMyClient().send(messageType, "complaint/by/status_complaint/OPEN",complaint);
	}
}