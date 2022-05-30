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

public class CustomerComplaintsController implements Initializable {
	@FXML
	private ListView<Integer> ComplaintsL;
	@FXML
	private Label ComplaintIdL;

	@FXML
	private TextField ComplaintIdT;

	@FXML
	private Button buttonSendComplaints;

	@FXML
	private Label customerIdL;

	@FXML
	private TextField customerIdT;

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
	private Label refundL;

	@FXML
	private TextField refundT;

	@FXML
	private Label replyL;

	@FXML
	private TextArea replyT;

	@FXML
	private Label statusL;

	@FXML
	private TextField statusT;
	private int selectedComplaintId;
	private Complaint selectedComplaint;
	public static Node finishButton;
	private HashMap<Integer, Complaint> complaints = new HashMap<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeComplaintsList();
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
	private void fillingTexts() {
		customerIdT.setText(Integer.toString(selectedComplaint.getIdCustomer()));
		ComplaintIdT.setText(Integer.toString(selectedComplaint.getIdComplaint()));
		statusT.setText(selectedComplaint.getStatus());
		dateT.setText(selectedComplaint.getDate());
		refundT.setText(Double.toString(selectedComplaint.getRefund()));
		descriptionT.setText(selectedComplaint.getComplaint());
		replyT.setText(selectedComplaint.getResponse());
	}
	private void initializeComplaintsList() {
		ArrayList<Complaint> complaintsList =  ComplaintQueryFromDB(MessageType.GET,null);
		for(int i=0 ; i<complaintsList.size() ; i++)
			complaints.put(complaintsList.get(i).getIdComplaint(), complaintsList.get(i));
		ComplaintsL.getItems().clear();
		ComplaintsL.getItems().addAll(complaints.keySet());
	}

	@FXML
	public void onSend(ActionEvent event) throws IOException {
		Dialog<ButtonType> dialog = LoadDialogPane();
		Optional<ButtonType> clickedButton = dialog.showAndWait();
		if (clickedButton.get() == ButtonType.FINISH) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			Complaint newComplaint = new Complaint(ClientConsoleController.getCustomer().getIdCustomer(),
					dtf.format(now), CustomerComplaintSendViewController.getComplaint());
			ComplaintQueryFromDB(MessageType.POST, newComplaint);
			ArrayList<Complaint> complaintsList = ComplaintQueryFromDB(MessageType.GET,null);  
			initializeComplaintsList();
			dialog.close();
		} else
			dialog.close();
	}
	private Dialog<ButtonType> LoadDialogPane() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(
		ClientView.class.getResource("/boundary/fxmls/customer-complaints-send-view.fxml"));
		DialogPane pane = fxmlLoader.load();
		finishButton = pane.lookupButton(ButtonType.FINISH);
		pane.lookupButton(ButtonType.FINISH).setDisable(true);
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setDialogPane(pane);
		return dialog;
	}
	private ArrayList<Complaint> ComplaintQueryFromDB(MessageType messageType, Complaint complaint){
		return (ArrayList<Complaint>) MainController.getMyClient().send(messageType, "complaint/by/id_customer/"+ ClientConsoleController.getCustomer().getIdCustomer(),complaint);
	}

}
