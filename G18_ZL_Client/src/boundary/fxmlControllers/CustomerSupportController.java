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
    private int selectedComplaintId;
    private Complaint selectedComplaint;
    public static HashMap<Integer,Complaint> ComplaintMap = new HashMap<>();
	private int isSendedReply = 0;
    public void onSelectComplaint(ActionEvent event) {
    	    }
    


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//send to server
		ComplaintMap.put(1, new Complaint(1,"123456","OnTreatment","1/5/2022",40,"Not good",null));
		ComplaintMap.put(2, new Complaint(2,"543216","OnTreatment","2/5/2022",50,"Not good flower",null));
		ComplaintMap.put(3, new Complaint(3,"645321","OnTreatment","3/5/2022",60,"Not good workers",null));
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
		CustomerIdT.setDisable(true);
		ComplaintIdT.setDisable(true);
		statusT.setDisable(true);
		dateT.setDisable(true);
		refundT.setDisable(true);
		descriptionT.setDisable(true);
		sendReplyButton.setDisable(true);
		ComplaintL.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
		
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if(isSendedReply == 0) {
				selectedComplaintId = ComplaintL.getSelectionModel().getSelectedItem();
				selectedComplaint = ComplaintMap.get(selectedComplaintId);
				ComplaintIdT.setText(Integer.toString(selectedComplaintId));
				CustomerIdT.setText(selectedComplaint.getIdCustomer());
				statusT.setText(selectedComplaint.getStatus());
				dateT.setText(selectedComplaint.getDate());
				refundT.setText(Double.toString(selectedComplaint.getRefund()));
				descriptionT.setText(selectedComplaint.getComplaint());
				//descriptionT.setDisable(true);
				replyT.setText("");
				sendReplyButton.setDisable(false);
				}
				isSendedReply = 0;
			}
			
		});
	}
	public void onSendReply(ActionEvent event) {
		//sendToServer
		isSendedReply = 1;
		ComplaintMap.remove(selectedComplaintId);
		ComplaintL.getItems().clear();
		ComplaintL.getItems().addAll(ComplaintMap.keySet());
		CustomerIdT.setText("");
		ComplaintIdT.setText("");
		statusT.setText("");
		dateT.setText("");
		refundT.setText("");
		descriptionT.setText("");
		replyT.setText("");
		sendReplyButton.setDisable(true);
	}
}