package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Customer;
import entity.Order;
import entity.Store;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CustomerOrdersController implements Initializable {

	private ArrayList<Order> orders;
 
	private Order selectedOrder;

	@FXML
	private TextField PriceTF;

	@FXML
	private TextField addressTF;

	@FXML
	private TextField deliveryTF;
	@FXML
	private TextField orderDateTF;

	@FXML
	private TextField orderIdTF;

	@FXML
	private TextField orderStatusTF;
	@FXML
	private TextField storeTF;

	@FXML
	private TextArea descriptionTA;

	@FXML
	private TextArea grearingTA;

	

	@FXML
	private ListView<String> ordersLV;

	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 
		loadOrders();
		
		ordersLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {


			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				
				
				System.out.println("oldValue "+oldValue+" newValue "+newValue);
  				int indexorder = (Integer.parseInt(newValue));
  				previewOrder(orders.get(--indexorder));
 
			}
		});

	}
	
	private void loadOrders( ) {
		int numberOrder=1;
		orders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET, "order/by/id_customer/"+ClientConsoleController.getCustomer().getIdCustomer(),null);
				
		clearFields();

		for (Order order : orders) {
				ordersLV.getItems().add ((numberOrder++)+"");
		}
	}
	
	
	private void previewOrder(Order o) {
		selectedOrder = o;

		// fill fields
		orderIdTF.setText(selectedOrder.getIdOrder() + "");
		orderDateTF.setText(selectedOrder.getOrderDate());
		deliveryTF.setText(selectedOrder.getDeliveryDate());
		addressTF.setText(selectedOrder.getAddress());
		PriceTF.setText(selectedOrder.getPrice()+"");
		orderStatusTF.setText(selectedOrder.getOrderStatus()+"");
		storeTF.setText(selectedOrder.getStore()+"");
		descriptionTA.setText(selectedOrder.getDescription());
		grearingTA.setText(selectedOrder.getGreetingCard());
		 
	 
	  
	}
	
	
	
	
	/**
	 * clears all the relevant fields in the page to be ready for the next input or
	 * just clear the page
	 */
	private void clearFields() {

		ordersLV.getItems().clear();
		selectedOrder = null;

		orderIdTF.clear();
		 
		orderDateTF.clear();
		deliveryTF.clear();
		addressTF.clear();
		 
 

		 
	}
 
}
