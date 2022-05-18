package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
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
    private int selectedComplaint;
    public static ArrayList<Integer> ComplaintsIdlist = new ArrayList<>();
    public static ArrayList<Complaint> ComplaintsList = new ArrayList<>();
	private String ComplaintIdString,refundString;
    public void onSelectComplaint(ActionEvent event) {
    	    }
    


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//send to server
		ComplaintsIdlist.add(1);
		ComplaintsIdlist.add(2);
		ComplaintsIdlist.add(3);
		ComplaintsList.add(new Complaint(1,"123456","OnTreatment","1/5/2022",40,"Not good",null));
		ComplaintsList.add(new Complaint(2,"543216","OnTreatment","2/5/2022",50,"Not good flower",null));
		ComplaintsList.add(new Complaint(1,"645321","OnTreatment","3/5/2022",60,"Not good workers",null));


		ComplaintL.getItems().addAll(ComplaintsIdlist);
		sendReplyButton.setDisable(true);
		ComplaintL.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
		
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				selectedComplaint = ComplaintL.getSelectionModel().getSelectedItem();
				ComplaintIdString = Integer.toString(selectedComplaint-1);
				CustomerIdT.setText(ComplaintsList.get(selectedComplaint-1).getIdCustomer());
				ComplaintIdT.setText(ComplaintIdString);
				statusT.setText(ComplaintsList.get(selectedComplaint-1).getStatus());
				dateT.setText(ComplaintsList.get(selectedComplaint-1).getDate());
				refundString = Double.toString(ComplaintsList.get(selectedComplaint-1).getRefund());
				refundT.setText(refundString);
				descriptionT.setText(ComplaintsList.get(selectedComplaint-1).getComplaint());
				replyT.setText("");
				sendReplyButton.setDisable(false);
			}
			
		});
	}
	public void onSendReply(ActionEvent event) {
		System.out.println(selectedComplaint);
		ComplaintsList.remove(selectedComplaint-1);
		ComplaintsIdlist.remove(selectedComplaint -1);
		ComplaintL.getItems().clear();
		ComplaintL.getItems().addAll(ComplaintsIdlist);
		CustomerIdT.setText("");
		ComplaintIdT.setText("");
		statusT.setText("");
		dateT.setText("");
		refundT.setText("");
		replyT.setText("");
		sendReplyButton.setDisable(true);
	}
}