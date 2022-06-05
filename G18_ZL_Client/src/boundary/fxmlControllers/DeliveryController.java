package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.BuildItem;
import entity.Customer;
import entity.Email;
import entity.Item.OrderItem;
import entity.MyMessage.MessageType;
import entity.Order;
import entity.Order.OrderStatus;
import entity.Store;
import entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DeliveryController implements Initializable {

	/**
	 * Orders to be viewed
	 */
	private ArrayList<Order> orders;
	/**
	 * Currently selected order
	 */
	private Order selectedOrder;
	/**
	 * the user account of the customer of selected order
	 */
	private User buyingUser;

	// FXML Components
	/**
	 * Combobox to view stores and be used to filter products
	 */
	@FXML
	private ComboBox<Store> storeCB;
	/**
	 * listview to show orders by id and address ("id/address")
	 */
	@FXML
	private ListView<String> ordersLV;

	//ORDER INFO
	/**
	 * order id Textfield
	 */
	@FXML
	private TextField orderIdTF;
	/**
	 * branch order was sent from
	 */
	@FXML
	private TextField branchTF;
	/**
	 * date and time that the order was checked out and payed for
	 */
	@FXML
	private TextField orderDateTF;
	/**
	 * date of the requested delivery
	 */
	@FXML
	private TextField deliveryTF;
	/**
	 * address to be delivered to (if picked)
	 */
	@FXML
	private TextField addressTF;
	
	//CUSTOMER INFO
	/**
	 * customer that paid for the order
	 */
	@FXML
	private TextField custNameTF;
	/**
	 * name of customer in case the delivery worker needed to call while delivering
	 */
	@FXML
	private TextField custNumTF;

	/**
	 * Listview that shows the items of the selected order (includes normal items and builditems)
	 */
	@FXML
	private ListView<String> itemsLV;

	/**
	 * TextArea to show an items information in textual way, the delivery worker doesnt need more
	 */
	@FXML
	private TextArea itemSummaryTA;

	/**
	 * button to update the order status in the server and set it as DELIVERED
	 */
	@FXML
	private Button confirmBtn;

	@Override
	/**
	 * initializing all listview listeners and fetching orders
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		storeCB.getItems().setAll(Store.values());

		storeCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Store>() {
			@Override
			public void changed(ObservableValue<? extends Store> observable, Store oldValue, Store newValue) {
				loadOrders(newValue);
			}
		});
		ordersLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				// string is presented as "id -> address"
				String[] idAndAddress = newValue.split(" / ");
				int id = Integer.parseInt(idAndAddress[0]);

				// looking for correct order
				for (Order order : orders)
					if (order.getIdOrder() == id) {
						previewOrder(order);
						break;
					}
			}
		});
		itemsLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				String[] chosen = newValue.split(" / ");
				if (chosen[0].equals("BUILD ITEM:"))
					for (BuildItem item : selectedOrder.getBuildItems()) {
						String string = "BUILD ITEM:" + " / " + item.getIdBuildItem() + " / "
								+ selectedOrder.getIdOrder();
						if (newValue.equals(string)) {
							itemSummaryTA.setText(item.infoString());
							return;
						}
					}

				if (chosen[0].equals("ITEM:"))
					for (OrderItem item : selectedOrder.getItems()) {
						if (newValue.equals("ITEM:" + " / " + item.getIdItem() + " / " + item.getName())) {
							itemSummaryTA.setText(item.infoString());
							return;
						}
					}
			}
		});

		storeCB.getSelectionModel().select(-1);
		loadOrders(null);
	}

	@SuppressWarnings("unchecked")
	/**
	 * fetches all orders to be viewed and shows the orders according to what store
	 * was picked in the combo box if the parameter receives a null, all orders in
	 * PROCESSING are shown, regardless of store
	 * 
	 * @param store Store to filter orders
	 */
	private void loadOrders(Store store) {
		orders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET, "order/by/id_order_status/1",
				null);
		clearFields();

		for (Order order : orders) 
			if (store == null || order.getStore().equals(store))
				ordersLV.getItems().add(order.getIdOrder() + " / " + order.getAddress());
	}

	/**
	 * clears all the relevant fields in the page to be ready for the next input or
	 * just clear the page
	 */
	private void clearFields() {

		ordersLV.getItems().clear();
		selectedOrder = null;
		buyingUser=null;

		orderIdTF.clear();
		branchTF.clear();
		orderDateTF.clear();
		deliveryTF.clear();
		addressTF.clear();
		custNameTF.clear();
		custNumTF.clear();

		itemsLV.getItems().clear();
		itemSummaryTA.clear();

		confirmBtn.setDisable(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Receives an order and displays all the relevant information to the delivery
	 * worker
	 * 
	 * @param o Order to be previewed
	 */
	private void previewOrder(Order o) {
		selectedOrder = o;

		// fill fields
		orderIdTF.setText(selectedOrder.getIdOrder() + "");
		branchTF.setText(selectedOrder.getStore().toString());
		orderDateTF.setText(selectedOrder.getOrderDate());
		deliveryTF.setText(selectedOrder.getDeliveryDate());
		addressTF.setText(selectedOrder.getAddress());

		ArrayList<Customer> customers = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET,
				"customer/by/id_customer/" + selectedOrder.getIdCustomer(), null);

		buyingUser = (User) MainController.getMyClient().send(MessageType.GET,
				"user/by/id_user/" + customers.get(0).getIdUser(), null);
		
		custNameTF.setText(buyingUser.getName());
		custNumTF.setText(buyingUser.getPhone());

		loadItems();
		confirmBtn.setDisable(false);
	}

	/**
	 * Fetches the items in the order from the server and loads their id,orderid(if
	 * build item), and name into a listview
	 */
	private void loadItems() {

		itemsLV.getItems().clear();
		itemSummaryTA.clear();

		selectedOrder = (Order) MainController.getMyClient().send(MessageType.GET, "order/fill", selectedOrder);

		for (BuildItem item : selectedOrder.getBuildItems()) {
			String string = "BUILD ITEM:" + " / " + item.getIdBuildItem() + " / " + selectedOrder.getIdOrder();
			itemsLV.getItems().add(string);
		}
		for (OrderItem item : selectedOrder.getItems()) {
			itemsLV.getItems().add("ITEM:" + " / " + item.getIdItem() + " / " + item.getName());
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	/**
	 * updates an order status in the server to be set as DELIVERED
	 */
	void onConfirmPressed() {
		if (selectedOrder == null)
			return;

		selectedOrder.setOrderStatus(OrderStatus.DELIVERED);
		ArrayList<Order> orders=(ArrayList<Order>) MainController.getMyClient().send(MessageType.UPDATE, "order/status", selectedOrder);
		if(orders.size()!=0 && orders.get(0).getIdOrderStatus()==selectedOrder.getIdOrderStatus()) {
			Email email=new Email(buyingUser.getEmail(), "Order number ["+selectedOrder.getIdOrder()+"] has been delivered!", "Your order has been delivered\nOrder Summary:\n"+selectedOrder.toString());
			MainController.getMyClient().send(MessageType.SEND, "email", email);
		}
			

		storeCB.getSelectionModel().select(-1);
		loadOrders(null);
	}
}
