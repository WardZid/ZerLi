package boundary.fxmlControllers;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import control.MainController;
import entity.Store;
import entity.User;
import entity.Customer;
import entity.Email;
import entity.MyMessage.MessageType;
import entity.Order;
import entity.Order.OrderStatus;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*			
									
									1.																   																									
									
 * */

/**
 * @author hamza
 *
 */
public class BranchManagerOrdersController implements Initializable {

	/* ------------------------------------------------ */
	/* \/ FXML Variables \/ */
	/* ------------------------------------------------ */

	/**
	 * VBox that contains the texts of the full order details
	 */
	@FXML
	private VBox myVBox;

	/**
	 * the order address
	 */
	@FXML
	private Text addressText;

	/**
	 * button to approve an order
	 */
	@FXML
	private Button approveButton;

	/**
	 * button to cancel an order
	 */
	@FXML
	private Button cancelButton;

	/**
	 * text contains the date when the order was made
	 */
	@FXML
	private Text dateOfOrderText;

	/**
	 * text that contains the date of delivery of the order
	 */
	@FXML
	private Text deliveryDateText;

	/**
	 * text that contains the order ID
	 */
	@FXML
	private Text orderIDText;

	/**
	 * list view of the orders that need approve
	 */
	@FXML
	private ListView<String> ordersToApproveListView;

	/**
	 * list view of orders that need to be canceled
	 */
	@FXML
	private ListView<String> ordersToCancelListView;

	/**
	 * text that contains the price of the order
	 */
	@FXML
	private Text overallOrderToPayText;

	/**
	 * button to show full order details
	 */
	@FXML
	private Button viewFullDetailsButton;

	/**
	 * button to reject an order
	 */
	@FXML
	private Button rejectButton;

	/* ------------------------------------------------ */
	/* \/ FXML Full Order Details Variables \/ */
	/* ------------------------------------------------ */

	/**
	 * the order details vbox
	 */
	@FXML
	private VBox orderVBox;

	/**
	 * order address in full review
	 */
	@FXML
	private Text fAddress;

	/**
	 * order id in full review
	 */
	@FXML
	private Text fCID;

	/**
	 * customer name in full review
	 */
	@FXML
	private Text fCName;

	/**
	 * order description in full review
	 */
	@FXML
	private TextArea fDescription;

	/**
	 * order greeting in full review
	 */
	@FXML
	private TextArea fGreeting;

	/**
	 * order cancellation date in full review
	 */
	@FXML
	private Text fOCDate;

	/**
	 * order delivery date in full review
	 */
	@FXML
	private Text fODDate;

	/**
	 * order number in full review
	 */
	@FXML
	private Text fONumber;

	/**
	 * order status in full review
	 */
	@FXML
	private Text fOStatus;

	/**
	 * order date of order in full review
	 */
	@FXML
	private Text fOdate;

	/**
	 * order price in full review
	 */
	@FXML
	private Text fOverall;

	/* ------------------------------------------------ */
	/* \/ Help Variables \/ */
	/* ------------------------------------------------ */

	/* scene to use when viewing the full order */
	private Scene scene;

	/* ArrayList to save the waiting approval orders in the ListView */
	private ArrayList<Order> waitingApprovalOrders;
	private ArrayList<String> waitingApprovalIDs = new ArrayList<>();

	/* ArrayList to save the waiting cancellation orders in the ListView */
	private ArrayList<Order> waitingCancellationOrders;
	private ArrayList<String> waitingCancellationlIDs = new ArrayList<>();

	/* the current branch manager's branch(store) ID */
	private int branchID;

	/* to save the user info */
	private User user = ClientConsoleController.getUser();

	/* the selected order's ID */
	private String selectedOrderID;

	/* the current selected order */
	private Order currentOrder;

	/* if an order is selected from the approve LsitView */
	private boolean approveSelected = false;

	/* if an order is selected from the cancel LsitView */
	private boolean cancelSelected = false;

	/* the name of the customer with the current selected order */
	private Customer currentCustomer;

	/* ------------------------------------------------ */
	/* \/ initialize function \/ */
	/* ------------------------------------------------ */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBranchID();
		initListViews();
		setActionOnApprovaeListView();
		setActionOnCancelListView();

	}

	/* ------------------------------------------------ */
	/* \/ Action Methods \/ */
	/* ------------------------------------------------ */

	/**
	 * Method to do when approve Button is clicked.
	 * 
	 * @param event - event handler
	 */
	@FXML
	@SuppressWarnings("unchecked")
	public void approveButtonAction(ActionEvent event) {
		// change the current selected order status to 1 (APPROVED).
		currentOrder.setIdOrderStatus(OrderStatus.PROCESSING.ordinal());

		if (currentOrder.getDeliveryDate() == "") {
			currentOrder.setDeliveryDate(MainController.addHoursToCurrentTime(3));
			MainController.getMyClient().send(MessageType.UPDATE,"order/DeliveryDate/", currentOrder);
					
		}

		ArrayList<Order> order = (ArrayList<Order>) MainController.getMyClient().send(MessageType.UPDATE,
				"order/status", currentOrder);

		if (order.size() != 0) {
			User currentUser = (User) MainController.getMyClient().send(MessageType.GET,
					"user/by/id_customer/" + order.get(0).getIdCustomer(), null);

			Email email = new Email(currentUser.getEmail(),
					"Order number [" + order.get(0).getIdOrder() + "] has been approved!",
					"Your order has been approved\nYou will get it soon ^_^\n");

			MainController.getMyClient().send(MessageType.SEND, "email", email);
		}
		// check if order was changed.
		if (order.get(0).getIdOrderStatus() == OrderStatus.PROCESSING.ordinal())
			System.out.println("Order updated successfully!");
		else
			System.out.println("Error updating order!");
		initListViews();
		clearAfterButtonPressed();

		approveButton.setDisable(true);
		rejectButton.setDisable(true);
	}

	/**
	 * Method to do when reject Button is clicked.
	 * 
	 * @param event - event handler
	 */
	@FXML
	@SuppressWarnings("unchecked")
	public void rejectButtonAction(ActionEvent event) {
		// change the current selected order status to 4 (UNAPPROVED).
		currentOrder.setIdOrderStatus(OrderStatus.UNAPPROVED.ordinal());
		ArrayList<Order> order = (ArrayList<Order>) MainController.getMyClient().send(MessageType.UPDATE,
				"order/status", currentOrder);
		// check if order was changed.
		if (order.get(0).getIdOrderStatus() == OrderStatus.UNAPPROVED.ordinal())

			System.out.println("Order updated successfully!");
		else
			System.out.println("Error updating order!");
		disableAllButtons();
		initListViews();

		MainController.getMyClient().send(MessageType.UPDATE,
				"customer/point/" + currentOrder.getIdCustomer() + "/" + currentOrder.getPrice(), null);
		clearAfterButtonPressed();

		if (order.size() != 0) {
			User currentUser = (User) MainController.getMyClient().send(MessageType.GET,
					"user/by/id_customer/" + order.get(0).getIdCustomer(), null);
			Email email = new Email(currentUser.getEmail(),
					"Order number [" + order.get(0).getIdOrder() + "] has been unapproved!",
					"Your order has been unapproved.\n you will get a refund of " + order.get(0).getPrice() + "  \n");
			MainController.getMyClient().send(MessageType.SEND, "email", email);
		}

		approveButton.setDisable(true);
		rejectButton.setDisable(true);
	}

	/**
	 * Method to do when cancel Button is clicked.
	 * 
	 * @param event - event handler
	 */
	@FXML
	@SuppressWarnings("unchecked")
	public void cancelButtonAction(ActionEvent event) {
          double MoneyForRefund=0;
	 
		// change the current selected order status to 3 (CANCELED).
		currentOrder.setIdOrderStatus(OrderStatus.CANCELLED.ordinal());
		ArrayList<Order> order = (ArrayList<Order>) MainController.getMyClient().send(MessageType.UPDATE,
				"order/status", currentOrder);
		// check if order was changed.
		if (order.get(0).getIdOrderStatus() == OrderStatus.CANCELLED.ordinal())
			System.out.println("Order updated successfully!");
		else
			System.out.println("Error updating order!");
		disableAllButtons();

		ordersToCancelListView.getSelectionModel().clearSelection();

		initListViews();
		
		if(  MainController.timeDiffHour(currentOrder.getDeliveryDate(), currentOrder.getCancelDate())>=3    )
			MoneyForRefund=currentOrder.getPrice();
		
		else if(  MainController.timeDiffHour(currentOrder.getDeliveryDate(), currentOrder.getCancelDate())<3 && MainController.timeDiffHour(currentOrder.getDeliveryDate(), currentOrder.getCancelDate())>=1    )
			MoneyForRefund=currentOrder.getPrice()*0.5;
		else
			MoneyForRefund=0;  
			
			
		User currentUser = (User) MainController.getMyClient().send(MessageType.GET,
				"user/by/id_customer/" + currentOrder.getIdCustomer(), null);
		
		
		Email email = new Email(currentUser.getEmail(),"Order number [" + currentOrder.getIdOrder() + "] has been canceled!","we canceled your order   .\n you will get a refund of " + MoneyForRefund + "  \n");
				
				
	
		
		
		MainController.getMyClient().send(MessageType.UPDATE,
				"customer/point/" + currentOrder.getIdCustomer() + "/" + currentOrder.getPrice(), null);
		
		
		
		
		clearAfterButtonPressed();
		approveSelected = false;
		cancelSelected = false;
		cancelButton.setDisable(true);
	}

	/**
	 * Action when a line is selected in the approveListView.
	 */
	public void orderSelectedFromApproveOrderListView() {
		this.approveButton.setDisable(false);
		this.rejectButton.setDisable(false);
		this.cancelButton.setDisable(true);
		setOrderDetails();
	}

	/**
	 * Action when a line is selected in the approveListView.
	 */
	public void monthSelectedFromCancelOrderListView() {

		this.approveButton.setDisable(true);
		this.rejectButton.setDisable(true);
		this.cancelButton.setDisable(false);
		setOrderDetails();

		// METHOD NOT DONE! MAYBE NO NEED,
	}

	/**
	 * Method to do when "View Full Details" is pressed. Opens a new window showing
	 * the full details of the selected order.
	 * 
	 * @param event
	 * 
	 * @throws IOException
	 */
	@FXML
	public void actionOnViewFullDetailsBTN(ActionEvent event) throws IOException {
		myVBox.setDisable(true);
		orderVBox.setVisible(true);
		setFullOrderDetails();
	}

	/* ------------------------------------------------ */
	/* \/ Help Methods \/ */
	/* ------------------------------------------------ */

	private void disableAllButtons() {
		approveButton.setDisable(true);
		rejectButton.setDisable(true);
		cancelButton.setDisable(true);
		viewFullDetailsButton.setDisable(true);
	}

	/**
	 * Method to set the full order details in the window
	 */
	@SuppressWarnings("unchecked")
	private void setFullOrderDetails() {
		ArrayList<Customer> c = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET,
				"customer/by/id_customer/" + currentOrder.getIdCustomer(), null);
		currentCustomer = c.get(0);
		User u = (User) MainController.getMyClient().send(MessageType.GET,
				"user/by/id_coustmer/" + currentCustomer.getIdCustomer(), null);
		this.fAddress.setText(currentOrder.getAddress());
		this.fCName.setText(u.getName());
		this.fCID.setText(currentOrder.getIdCustomer() + "");
		this.fONumber.setText(currentOrder.getIdOrder() + "");
		this.fOStatus.setText(OrderStatus.getById(currentOrder.getIdOrderStatus()).toString());
		this.fOdate.setText(currentOrder.getOrderDate());
		this.fODDate.setText(currentOrder.getDeliveryDate());
		this.fOCDate.setText(currentOrder.getCancelDate());
		this.fDescription.setText(currentOrder.getDescription());
		this.fGreeting.setText(currentOrder.getGreetingCard());
		this.fOverall.setText(currentOrder.getPrice() + "");
	}

	/**
	 * To set the details of the selected order.
	 */
	private void setOrderDetails() {

		getOrderBySelection();
		viewFullDetailsButton.setDisable(false);
		this.orderIDText.setText(currentOrder.getIdOrder() + "");
		this.dateOfOrderText.setText(currentOrder.getOrderDate());
		this.deliveryDateText.setText(currentOrder.getDeliveryDate());
		this.addressText.setText(currentOrder.getAddress());
		this.overallOrderToPayText.setText(currentOrder.getPrice() + "");

	}

	/**
	 * To save the order that is selected from the ListViews
	 */
	private void getOrderBySelection() {

		if (approveSelected == true) {
			for (Order o : waitingApprovalOrders) {
				if (selectedOrderID.equals(o.getIdOrder() + "")) {
					currentOrder = o;
				}

			}
		}
		if (cancelSelected == true) {
			System.out.println("try2************");
			for (Order o : waitingCancellationOrders) {
				if (selectedOrderID.equals(o.getIdOrder() + "")) {
					currentOrder = o;
				}
			}
		}
		setCorrectValues();

	}

	/**
	 * Check the null fields in the Order and change to "N/A".
	 */
	private void setCorrectValues() {
		if (currentOrder.getAddress() == null)
			currentOrder.setAddress("");
		if (currentOrder.getCancelDate() == null)
			currentOrder.setCancelDate("");
		if (currentOrder.getDeliveryDate() == null)
			currentOrder.setDeliveryDate("");
		if (currentOrder.getDescription() == null)
			currentOrder.setDescription("");
		if (currentOrder.getGreetingCard() == null)
			currentOrder.setGreetingCard("");
		if (currentOrder.getOrderDate() == null)
			currentOrder.setOrderDate("");
		if (currentOrder.getAddress() == " ")
			currentOrder.setAddress("");
	}

	/**
	 * Method to save the order ID
	 */
	private void saveOrderID() {
		String[] split = null;
		String[] split2 = null;
		if (approveSelected == true) {
			split = ordersToApproveListView.getSelectionModel().getSelectedItem().split(" - ");
		}
		if (cancelSelected == true) {
			split = ordersToCancelListView.getSelectionModel().getSelectedItem().split(" - ");
		}
		split2 = split[0].split(" : ");
		selectedOrderID = split2[1];

	}

	/**
	 * Method to do when an ID is selected from approve ListView
	 */
	private void setActionOnApprovaeListView() {
		ordersToApproveListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			// this method has the main Action that happens when selection accrues on
			// ListView
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (arg2 != null) {
					approveSelected = true;
					cancelSelected = false;
					saveOrderID();
					orderSelectedFromApproveOrderListView();
				}
			}
		});
	}

	/**
	 * Method to do when an ID is selected from cancel ListView
	 */
	private void setActionOnCancelListView() {
		ordersToCancelListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			// this method has the main Action that happens when selection accrues on
			// ListView
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

				if (arg2 != null) {

					System.out.println("try1 ***********");
					cancelSelected = true;
					approveSelected = false;
					saveOrderID();

					monthSelectedFromCancelOrderListView();

				}
			}
		});
	}

	/**
	 * Method to initialize the ListViews
	 */
	private void initListViews() {
		initApprovalListView();
		initCancellationListView();
	}

	/**
	 * Method to initialize the waiting approval ListView
	 */
	@SuppressWarnings("unchecked")
	private void initApprovalListView() {
		waitingApprovalIDs.clear();
		ordersToApproveListView.getItems().clear();
		waitingApprovalOrders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET,
				"order/by/id_order_status/0", null);
		for (Order o : waitingApprovalOrders)
			waitingApprovalIDs.add("Order ID : "+o.getIdOrder() + " - " + "Customer ID : " + o.getIdCustomer());
		ordersToApproveListView.getItems().addAll(waitingApprovalIDs);
	}

	/**
	 * Method to initialize the waiting approval ListView
	 */
	@SuppressWarnings("unchecked")
	private void initCancellationListView() {
		waitingCancellationlIDs.clear();
		ordersToCancelListView.getItems().clear();
		waitingCancellationOrders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET,
				"order/by/id_order_status/5", null);
		for (Order o : waitingCancellationOrders)
			waitingCancellationlIDs.add("Order ID : "+o.getIdOrder() + " - " + "Customer ID : " + o.getIdCustomer());
		ordersToCancelListView.getItems().addAll(waitingCancellationlIDs);
	}

	/**
	 * Method that asks the server to get the ID of the branch respectively with the
	 * current user.
	 */
	@SuppressWarnings("unchecked")
	private void setBranchID() {
//		ArrayList<Store> stores = (ArrayList<Store>)MainController.getMyClient().send(MessageType.GET, "store/by/id_user/"+user.getIdUser(), null);
//		if(stores.size()!=0)
//		branchID = stores.get(0).ordinal();		

		Store store = Store.getById(ClientConsoleController.getWorker().getIdStore());
		branchID = store.ordinal();
	}

	@FXML
	public void onCancelPressed() {
		myVBox.setDisable(false);
		orderVBox.setVisible(false);
	}

	/**
	 * Method to clear every thing after a button is pressed (not the view vull
	 * details button)
	 */
	private void clearAfterButtonPressed() {
		viewFullDetailsButton.setDisable(true);
		orderIDText.setText("");
		dateOfOrderText.setText("");
		addressText.setText("");
		deliveryDateText.setText("");
		overallOrderToPayText.setText("");
	}

}
