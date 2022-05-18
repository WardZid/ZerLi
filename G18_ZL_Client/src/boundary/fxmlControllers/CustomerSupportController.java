package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import control.ClientController;
import control.MainController;
import entity.Complaint;
import entity.MyMessage;
import entity.MyMessage.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CustomerSupportController implements Initializable {
	 @FXML
	    private TableColumn<Complaint, Integer> complaintId;

	    @FXML
	    private TableView<Complaint> complaintT;

	    @FXML
	    private TableColumn<Complaint, Integer> customerId;

	    @FXML
	    private TableColumn<Complaint, String> date;

	    @FXML
	    private Label description;

	    @FXML
	    private TextArea descriptionT;

	    @FXML
	    private HBox hb;

	    @FXML
	    private TableColumn<Complaint, Integer> refund;

	    @FXML
	    private Label replyL;

	    @FXML
	    private TextArea replyT;

	    @FXML
	    private Button sendReply;

	    @FXML
	    private StackPane stackPane;

	    @FXML
	    private TableColumn<Complaint, String> status;

	    @FXML
	    private VBox vb1;

	    @FXML
	    private VBox vb2;

	    @FXML
	    private VBox vb3;
    public	static ObservableList<Complaint> list = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		complaintId.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("Complaint_id"));;
		customerId.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("Customer_id"));;
		status.setCellValueFactory(new PropertyValueFactory<Complaint, String>("Status"));;
		date.setCellValueFactory(new PropertyValueFactory<Complaint, String>("date"));;
		refund.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("refund"));;
		complaintT.setItems(list);
		sendReply.setDisable(true);
	}
	public void onSendReply(ActionEvent event) {
		
	}
}