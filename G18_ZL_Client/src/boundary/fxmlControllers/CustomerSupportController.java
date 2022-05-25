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
	private GridPane gp;

	@FXML
	private HBox hb1;

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
	private StackPane stackPane;

	@FXML
	private TextField statusT;

	@FXML
	private VBox vb1;

	@FXML
	private VBox vb2;

	@FXML
	private VBox vb3;
	@FXML
    private Button refresh;
	private int selectedComplaintId;
	private Complaint selectedComplaint;
	public static HashMap<Integer, Complaint> ComplaintMap = new HashMap<>();
	public void onSelectComplaint(ActionEvent event) {
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// send to server
		ArrayList<Complaint> complaintsList =  (ArrayList<Complaint>)MainController.getMyClient().send(MessageType.GET, "complaint",null);
		for(int i=0 ; i<complaintsList.size() ; i++)
			ComplaintMap.put(complaintsList.get(i).idComplaint, complaintsList.get(i));
//		ComplaintMap.put(1, new Complaint(1, 111, "OnTreatment", "1/5/2022", 0, "Not good", null));
//		ComplaintMap.put(2, new Complaint(2, 222, "OnTreatment", "2/5/2022", 0, "Not good flower", null));
//		ComplaintMap.put(3, new Complaint(3, 333, "OnTreatment", "3/5/2022", 0, "Not good workers", null));
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
		CustomerIdT.setEditable(false);
		ComplaintIdT.setEditable(false);
		statusT.setEditable(false);
		dateT.setEditable(false);
		refundT.setEditable(false);
		descriptionT.setEditable(false);
		replyT.setEditable(false);
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
					ComplaintIdT.setText(Integer.toString(selectedComplaintId));
					CustomerIdT.setText(Integer.toString(selectedComplaint.getIdCustomer()));
					statusT.setText(selectedComplaint.getStatus());
					dateT.setText(selectedComplaint.getDate());
					descriptionT.setText(selectedComplaint.getComplaint());
					// descriptionT.setDisable(true);
					refundT.setEditable(true);
					replyT.setEditable(true);
					replyT.clear();
				}catch(NullPointerException e) {}
					
				}

		});
	}

	public void onSendReply(ActionEvent event) {
		selectedComplaint.setRefund(Integer.parseInt(refundT.getText()));
		selectedComplaint.setResponse(replyT.getText());
		MainController.getMyClient().send(MessageType.UPDATE, "complaint",selectedComplaint);
		ComplaintMap.remove(selectedComplaintId);
		ComplaintL.getItems().clear();
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
		CustomerIdT.clear();
		ComplaintIdT.clear();
		statusT.clear();
		dateT.clear();
		refundT.clear();
		descriptionT.clear();
		replyT.clear();
		replyT.setEditable(false);
		refundT.setEditable(false);
		sendReplyButton.setDisable(true);
	}
	public void onRefresh(ActionEvent event) {
		ArrayList<Complaint> complaintsList =  (ArrayList<Complaint>)MainController.getMyClient().send(MessageType.GET, "complaint/by/Status/unAnswered",null);
		for(int i=0 ; i<complaintsList.size() ; i++)
			ComplaintMap.put(complaintsList.get(i).idComplaint, complaintsList.get(i));
//		ComplaintMap.put(4, new Complaint(4, 417, "OnTreatment", "57/5/2022", 0, "Not good workers", null));
		ComplaintL.getItems().clear();
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
	}
}