package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import entity.Complaint;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerComplaintsController implements Initializable{
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
    private HBox hb;

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

    @FXML
    private VBox vb1;

    @FXML
    private VBox vb2;

    @FXML
    private VBox vb3;

    @FXML
    private VBox vb4;

    @FXML
    private VBox vb5;
    private int selectedComplaintId;
    private Complaint selectedComplaint;
    public HashMap<Integer,Complaint> complaints = new HashMap<>();
    //public ArrayList<Complaint> list = new ArrayList<>();
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		//send to Server
		complaints.put(1, new Complaint(1,123,"treatment finished","1/5/2022",20,"not good workers","We are sorry we will refund you 20$"));
		complaints.put(2, new Complaint(2,123,"treatment finished","2/5/2022",0,"not good flowers","we have checked the flower but we did not found out anything wrong"));
		complaints.put(3, new Complaint(3,123,"treatment finished","3/5/2022",50,"flowers with disgustes smill","we have checked the flower and your claim is correct so we will refund you 50$"));
		complaints.put(4, new Complaint(4,123,"on treatment","5/5/2022",0,"delivery arrived lately",null));

		ComplaintsL.getItems().addAll(complaints.keySet());
		ComplaintIdT.setEditable(false);
		customerIdT.setEditable(false);
		statusT.setEditable(false);
		descriptionT.setEditable(false);
		refundT.setEditable(false);
		replyT.setEditable(false);
		ComplaintsL.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				selectedComplaintId = ComplaintsL.getSelectionModel().getSelectedItem();
			    selectedComplaint =	complaints.get(selectedComplaintId);
			    customerIdT.setText(Integer.toString(selectedComplaint.getIdCustomer()));
			    ComplaintIdT.setText(Integer.toString(selectedComplaint.getIdComplaint()));
			    statusT.setText(selectedComplaint.getStatus());
			    dateT.setText(selectedComplaint.getDate());
			    refundT.setText(Double.toString(selectedComplaint.getRefund()));
			    descriptionT.setText(selectedComplaint.getComplaint());
			    replyT.setText(selectedComplaint.getResponse());
			}
		});
	}
	@FXML
	public void onSend(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("fxmls/customer-support-view.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("fxmlControllers/client.css").toExternalForm());
//		primaryStage.setTitle("Client");
//		primaryStage.setScene(scene);
//		primaryStage.show();	
//////////////////////////
      //  Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
	}

}
