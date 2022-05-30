package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Order;
import entity.Order.OrderStatus;
import entity.Store;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DeliveryController implements Initializable {
	
	private ArrayList<Order> orders;
	
	//FXML Components
	@FXML
	private ComboBox<Store> storeCB;
	@FXML
	private ListView<String> ordersLV;

	@FXML
	private TextField orderIdTF;
	@FXML
	private TextField orderDateTF;
	@FXML
	private TextField deliveryTF;
	@FXML
	private TextField addressTF;
	

	@FXML
	private TextField custNameTF;
	@FXML
	private TextField custNumTF;

	@FXML
	private ListView<?> itemsLV;
	
	@FXML
	private TextArea itemSummaryTA;

	@FXML
	private Button confirmBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		storeCB.getItems().setAll(Store.values());
		fetchOrders();
		
		
	}
	

	@SuppressWarnings("unchecked")
	private void fetchOrders() {
		orders=(ArrayList<Order>) MainController.getMyClient().send(MessageType.GET,"order/by/id_order_status/1", null);
		clearFields();
		
		initOrdersListView();
	}
	
	private void clearFields() {
		ordersLV.getItems().clear();
		
		orderIdTF.clear();
		orderDateTF.clear();
		deliveryTF.clear();
		addressTF.clear();
		custNameTF.clear();
		custNumTF.clear();
		
		itemsLV.getItems().clear();
		itemSummaryTA.clear();
		
		confirmBtn.setDisable(true);
	}
	
	private void initOrdersListView(){
		for (Order order : orders) {
			ordersLV.getItems().add(order.getIdOrder()+" / "+ order.getAddress());//split(" / ")
		}
	}

	@FXML
	void onConfirmPressed() {

	}

}
