package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.BuildItem;
import entity.Customer;
import entity.Item;
import entity.Order;
import entity.Order.OrderStatus;
import entity.Store;
import entity.Item.ItemInBuild;
import entity.Item.OrderItem;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
/**
 * in this class we view all the orders that the customer has make before 
 * with all the details about the order contains the items he has selected and the products
 * that he built before and the details about the items and products 
 * @author saher
 *
 */
public class CustomerOrdersController implements Initializable {
/**
 * ArrayList for save all the orders that customer make from DB
 */
	private ArrayList<Order> orders;
/**
 * to save the order in this object that has selected from the List View 
 */
	private Order selectedOrder;
/**
 * to view the order price
 */
	@FXML
	private TextField PriceTF;
/**
 * to view the address of the customer 
 */
	@FXML
	private TextField addressTF;
/**
 * to view the delivery date of the order
 */
	@FXML
	private TextField deliveryTF;
/**
 *  to view order date
 */
	@FXML
	private TextField orderDateTF;
/**
 * to view order id
 */
	@FXML
	private TextField orderIdTF;
/**
 * to view order status 
 */
	@FXML
	private TextField orderStatusTF;
	/**
	 * to view the store address
	 */
	@FXML
	private TextField storeTF;
/**
 * to view description order 
 */
	@FXML
	private TextArea descriptionTA;
/**
 * to view greeting order
 */
	@FXML
	private TextArea grearingTA;

	/**
	 * to view all the items and products in this VBox
	 */
	@FXML
	private VBox orderItemVB;

	/**
	 * to view orders id in this list
	 */
	@FXML
	private ListView<String> ordersLV;
/**
 * to view Button back
 */
	@FXML
	private VBox innerVbox;
/**
 * to view all the items in product that he has built in this VBox
 */
	@FXML
	private VBox itemsViewVbox;
/**
 * to view all the items and products in this  HBox
 */
	@FXML
	private HBox orderPageHBox;
/**
 * return to orders page / form
 */
	@FXML
	private Button backBtn;
/**
 * to view a message about the canceling
 */
	@FXML
	private Text resaonLbl;
/**
 * to cancel an order 
 */
	@FXML
	private Button cancelOrderBtn;
/**
 * we added a listener for the table view to Recognize the order that we select from the list view 
 * we loaded the orders and the items 
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resaonLbl.setText("\n");
		loadOrders();
		setOptionforSelceted();
		ordersLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {


			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue == null)
					return;

				int indexorder = (Integer.parseInt(newValue)) - 1;
				selectedOrder = orders.get(indexorder);
				previewOrder();

				setOptionforSelceted();

				try {
					loadItemToVBox();
				} catch (IOException e) {
					
					e.printStackTrace();
				}

			}
		});

	}
/**
 * load all the items and products in VBox that selected from order he make 
 * @throws IOException
 */
	private void loadItemToVBox() throws IOException {

		orderItemVB.getChildren().clear();
		selectedOrder = (Order) MainController.getMyClient().send(MessageType.GET, "order/fill", selectedOrder);
		System.out.println(selectedOrder.getItems());

		// load items
		for (OrderItem orderItem : selectedOrder.getItems()) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/orderItem-view.fxml"));
			Node node = fXMLLoader.load();

			orderItemController orderItemController = fXMLLoader.getController();
			orderItemController.setData(orderItem, null, this);

			orderItemVB.getChildren().add(node);

		}

		// load builditem

		for (BuildItem buildItem : selectedOrder.getBuildItems()) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/orderItem-view.fxml"));
			Node node = fXMLLoader.load();

			orderItemController orderItemController = fXMLLoader.getController();
			orderItemController.setData(null, buildItem, this);

			orderItemVB.getChildren().add(node);

		}
	}
/**
 * load all the orders from DB that he make before on list view 
 */
	@SuppressWarnings("unchecked")
	private void loadOrders() {
		int numberOrder = 1;
		orders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET,
				"order/by/id_customer/" + ClientConsoleController.getCustomer().getIdCustomer(), null);

		clearFields();

		for (Order order : orders) {
			ordersLV.getItems().add((numberOrder++) + "");
		}
	}
/**
 * set all the text from the order that we selected from list view 
 */
	private void previewOrder() {

		// fill fields
		orderIdTF.setText(selectedOrder.getIdOrder() + "");
		orderDateTF.setText(selectedOrder.getOrderDate());
		deliveryTF.setText(selectedOrder.getDeliveryDate());
		addressTF.setText(selectedOrder.getAddress());
		PriceTF.setText(selectedOrder.getPrice() + "");
		orderStatusTF.setText(selectedOrder.getOrderStatus() + "");
		storeTF.setText(selectedOrder.getStore() + "");
		descriptionTA.setText(selectedOrder.getDescription());
		grearingTA.setText(selectedOrder.getGreetingCard());

	}


/**
 * when we click on cancel button we check all the status of the order canceling 
 * if we can to cancel  the order due to the delivery order and how much the refund that the customer deserve 
 */
	public void onCancelOrderBtnPressed() {
		System.out.println("cancel");
		Alert errorAlert = new Alert(AlertType.ERROR);

		if (selectedOrder == null) {
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You have to pick order to cancel");
			errorAlert.showAndWait();
			return;
		}

		int remainingTime = (int) MainController.timeDiffHour(selectedOrder.getDeliveryDate(),
				MainController.currentTime());

		if (remainingTime < 1) {
			Alert waringAlert = new Alert(AlertType.WARNING);
			waringAlert.setHeaderText(null);
			waringAlert.setContentText("The remaining time is less than 1 hour we cant ,cancele it !!!");
			waringAlert.showAndWait();
			return;
		} else if (remainingTime <= 0) {
			Alert waringAlert = new Alert(AlertType.WARNING);
			waringAlert.setHeaderText(null);
			waringAlert.setContentText("you already received the item ,cannot cancel it");
			waringAlert.showAndWait();
			return;
		} else if (remainingTime > 3) {

			cancelOrder(100);

		} else {
			cancelOrder(50);
		}

	}

	/**
	 * this method gets a parameter percent this is the refund that the customer deserve 
	 * then we showing a message that asking if he wants to sure canceling  
	 * if we can to cancel then we update refund else he gets alert message 
	 * @param percent
	 */
	private void cancelOrder(int percent) {
		Alert confirmationAlert = new Alert(AlertType.WARNING);

		if (percent == 100)
			confirmationAlert.setContentText("Are you sure that you want to cancel order,\n You will get full refund - "
					+ selectedOrder.getPrice() * percent / 100);
		else
			confirmationAlert.setContentText("Are you sure that you want to cancel order,\n You will get half refund - "
					+ selectedOrder.getPrice() * percent / 100);

		confirmationAlert.setHeaderText("Cancel Order");

		ButtonType cancelButton = new ButtonType("cancel", ButtonData.CANCEL_CLOSE);
		confirmationAlert.getDialogPane().getButtonTypes().add(cancelButton);

		Optional<ButtonType> result = confirmationAlert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			// press ok
			selectedOrder.setIdOrderStatus(OrderStatus.WAITING_CANCELLATION.ordinal());

			MainController.getMyClient().send(MessageType.UPDATE, "order/status", selectedOrder);

		} else {
			// press cancel
			setOptionforSelceted();
			// System.out.println("cancel");
			return;
		}
		
		orderStatusTF.setText("WAITING_CANCELLATION");
		setOptionforSelceted();
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
		PriceTF.clear();
		orderStatusTF.clear();
		storeTF.clear();
		descriptionTA.clear();
		grearingTA.clear();

	}
/**
 * here we view all the items that has selected to build the product
 */
	public void showInnerVbox() {

		orderPageHBox.setDisable(true);
		innerVbox.setVisible(true);
	}
/**
 * here we hide the VBox that view all the items that has selected to build the product
 */
	public void closeInnerVbox() {
		orderPageHBox.setDisable(false);
		innerVbox.setVisible(false);
	}
/**
 * to return to orders page
 */
	public void onbackBtnPressed() {

		closeInnerVbox();
	}

	/**
	 * @return VBox innerVbox
	 */
	public VBox getInnerVbox() {
		return innerVbox;
	}

	/**
	 * 
	 * @return VBox itemsViewVbox
	 */
	public VBox getItemsViewVbox() {
		return itemsViewVbox;
	}
	/**
	 * set the itemsViewVbox VBox
	 * @param itemsViewVbox
	 */

	public void setItemsViewVbox(VBox itemsViewVbox) {
		this.itemsViewVbox = itemsViewVbox;
	}
/**
 * here we check the canceling order status and we show a message due to the canceling 
 */
	public void setOptionforSelceted() {

		int MinRemaining = 1;
		int sedondChooiceForRemaining = 3;

		int remainingTime;
		clearCancelOption();
		// check null selectedOrder
		if (selectedOrder == null) {
			disableCancelOrderButton("");
			return;
		} else {
			// wrong instances
			if (selectedOrder.getIdOrderStatus() == OrderStatus.CANCELLED.ordinal()) {
				disableCancelOrderButton("the order status is CANCELLED\nnot able to cancle it");
				return;
			} else if (selectedOrder.getIdOrderStatus() == OrderStatus.UNAPPROVED.ordinal()) {
				disableCancelOrderButton("the order status is UNAPPROVED\nnot able to cancle it");
				return;
			} else if (selectedOrder.getIdOrderStatus() == OrderStatus.WAITING_CANCELLATION.ordinal()) {
				disableCancelOrderButton("the order status is WAITING \nCANCELLATION not able to delete it");
				return;
			} else {
				// right instances for Status
				// check time now
				remainingTime = (int) MainController.timeDiffHour(selectedOrder.getDeliveryDate(),
						MainController.currentTime());
				if (remainingTime < MinRemaining) {
					disableCancelOrderButton("The remaining time is less than 1 hour \nwe cant cancle your order !!!");
					return;
				}
				
				// right instances for show order item
				if (remainingTime >= MinRemaining) {
                     showCancelOrderButton();
				}

			}

		}
	}

	private boolean checkStatusOrder() {

		return false;
	}
/**
 * here we clear the message and we view the cancel button
 */
	private void clearCancelOption() {
		resaonLbl.setText("\n");
		cancelOrderBtn.setDisable(true);

	}
/**
 * view the message of canceling order status
 * @param reason string contains the message
 */
	private void disableCancelOrderButton(String reason) {
		resaonLbl.setText(reason);
		cancelOrderBtn.setDisable(true);

	}
/**
 * disable for cancel button
 */
	private void showCancelOrderButton( ) {
		 
		cancelOrderBtn.setDisable(false);

	}
}