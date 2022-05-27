package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Store;
import entity.User;
import entity.Customer;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*			
									
									1.
UPDATE assignment3.order O 
SET O.id_order_status = value 
WHERE O.id_order = orderID

UPDATE -> waiting/approve/orderID

public static int updateOrderStatus(String orderID, String value){
		int er; // number of effected rows
		try {
			er = statement.executeQuery("UPDATE assignment3.order O SET O.id_order_status ="+value+"WHERE O.id_order ="+orderID);		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return er;
}																   								
																	
									
 * */

/**
 * @author hamza
 *
 */
public class BranchManagerOrdersController implements Initializable {

	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
	@FXML
    private Text addressText;

    @FXML
    private Button approveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text dateOfOrderText;

    @FXML
    private Text deliveryDateText;

    @FXML
    private Text orderIDText;

    @FXML
    private ListView<Integer> ordersToApproveListView;

    @FXML
    private ListView<Integer> ordersToCancelListView;

    @FXML
    private Text overallOrderToPayText;
    
    @FXML
    private Button viewFullDetailsButton;
    
    @FXML
    private Button unapproveButton;
    
    /* ------------------------------------------------ */
    /*      \/ FXML Full Order Details Variables \/     */
    /* ------------------------------------------------ */
    
    @FXML
    private Text fAddress;

    @FXML
    private Text fCID;

    @FXML
    private Text fCName;

    @FXML
    private TextArea fDescription;

    @FXML
    private TextArea fGreeting;

    @FXML
    private Text fOCDate;

    @FXML
    private Text fODDate;

    @FXML
    private Text fONumber;

    @FXML
    private Text fOStatus;

    @FXML
    private Text fOdate;

    @FXML
    private Text fOverall;
	
	
	/* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    /* scene to use when viewing the full order */
    private Scene scene;
	
    /* ArrayList to save the waiting approval orders in the ListView */
    private static ArrayList<Order> waitingApprovalOrders;
    private static ArrayList<Integer> waitingApprovalIDs;
    
    /* ArrayList to save the waiting cancellation orders in the ListView */
    private static ArrayList<Order> waitingCancellationOrders;
    private static ArrayList<Integer> waitingCancellationlIDs;
    
    /* the current branch manager's branch(store) ID */
    private static int branchID;
    
    /* to save the user info */
    private static User user = ClientConsoleController.getUser();
    
    /* the selected order's ID */
    private static Integer selectedOrderID;
    
    /* the current selected order */
    private static Order currentOrder;
    
    /* if an order is selected from the approve LsitView */
    private static boolean approveSelected = false;
    
    /* if an order is selected from the cancel LsitView */
    private static boolean cancelSelected = false;
    
    /* the name of the customer with the current selected order */
    private static Customer currentCustomer;
	
	/* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBranchID();
		initListViews();
		setActionOnApprovaeListView();
		setActionOnCancelListView();
		
	}
	
	/* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * Action when a line is selected in the approveListView. 
	 */
	public void monthSelectedFromApproveOrderListView() {
		this.approveButton.setDisable(false);
		this.unapproveButton.setDisable(false);
		this.cancelButton.setDisable(true);
		setOrderDetails();
	}
	
	/**
	 * Action when a line is selected in the approveListView. 
	 */
	public void monthSelectedFromCancelOrderListView() {
		this.approveButton.setDisable(true);
		this.unapproveButton.setDisable(true);
		this.cancelButton.setDisable(false);
		setOrderDetails();
		
		// METHOD NOT DONE! MAYBE NO NEED,
		saveSelectedCancelOrderCancelationDate();
	}
	
	/**
	 * Method to do when "View Full Details" is pressed. 
	 * Opens a new window showing the full details of the selected order.
	 * 
	 * @param event
	 * 
	 * @throws IOException 
	 */
	public void actionOnViewFullDetailsBTN(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("boundary/fxmls/full-order-details.fxml"));
		DialogPane dp = loader.load();
		BranchManagerOrdersController controller = loader.getController();
		controller.setFullOrderDetails();
		Stage stage = new Stage();
		//stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.showAndWait();
	}
	
	
	/* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * Method to set the full order details in the window
	 */
	@SuppressWarnings("unchecked")
	private void setFullOrderDetails() {
		ArrayList<Customer> c = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET, "customer/by/id_customer"+currentOrder.getIdCustomer() , null);
		currentCustomer = c.get(0);
		this.fAddress.setText(currentOrder.getAddress());
		this.fCName.setText(currentCustomer.getName());
		this.fCID.setText(currentOrder.getIdCustomer()+"");
		this.fONumber.setText(currentOrder.getIdOrder()+"");
		this.fOStatus.setText(OrderStatus.getById(currentOrder.getIdOrderStatus()).toString());
		this.fOdate.setText(currentOrder.getOrderDate());
		this.fODDate.setText(currentOrder.getDeliveryDate());
		this.fOCDate.setText(currentOrder.getCancelDate());
		this.fDescription.setText(currentOrder.getDescription());
		this.fGreeting.setText(currentOrder.getGreetingCard());
	}
	
	/**
	 * To set the details of the selected order.
	 */
	private void setOrderDetails() {
		getOrderBySelection();
		saveOrderID();
		this.orderIDText.setText(currentOrder.getIdOrder()+"");
		this.dateOfOrderText.setText(currentOrder.getOrderDate());
		this.deliveryDateText.setText(currentOrder.getDeliveryDate());
		this.addressText.setText(currentOrder.getAddress());
		this.overallOrderToPayText.setText(currentOrder.getPrice()+"");
	}
	
	/**
	 * To save the order that is selected from the ListViews
	 */
	private void getOrderBySelection() {
		if(approveSelected == true) {
			for(Order o : waitingApprovalOrders) {
				if(selectedOrderID.equals(o.getIdOrder())) {
					currentOrder = o;
					setCorrectValues();
				}
			}
		}
		if(cancelSelected == true) {
			for(Order o : waitingCancellationOrders) {
				if(selectedOrderID.equals(o.getIdOrder())) {
					currentOrder = o;
					setCorrectValues();
				}
			}
		}
	}
	
	/**
	 * Check the null fields in the Order and change to "N/A".
	 */
	private void setCorrectValues() {
		if(currentOrder.getAddress() == null)
			currentOrder.setAddress("N/A");
		if(currentOrder.getCancelDate() == null)
			currentOrder.setCancelDate("N/A");
		if(currentOrder.getDeliveryDate() == null)
			currentOrder.setDeliveryDate("N/A");
		if(currentOrder.getDescription() == null)
			currentOrder.setDescription("N/A");
		if(currentOrder.getGreetingCard() == null)
			currentOrder.setGreetingCard("N/A");
		if(currentOrder.getOrderDate() == null)
			currentOrder.setOrderDate("N/A");
	}
	
	/**
	 * Method to save the order ID
	 */
	private void saveOrderID(){
		selectedOrderID = currentOrder.getIdOrder();
	}

	
	/**
	 *  to save the -date of cancellation- of the selected order in cancel ListView.
	 */
	private void saveSelectedCancelOrderCancelationDate() {
		// TO DO LATER... ( AFTER FINISHING THE APPROVE LIST )
	}
	
	/**
	 * Method to do when an ID is selected from approve ListView
	 */
	private void setActionOnApprovaeListView() {
		ordersToApproveListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

			@Override
			// this method has the main Action that happens when selection accrues on ListView
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				approveSelected = true;
				cancelSelected = false;
				monthSelectedFromApproveOrderListView();
			}
		});
	}
	
	/**
	 * Method to do when an ID is selected from cancel ListView
	 */
	private void setActionOnCancelListView() {
		ordersToCancelListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			// this method has the main Action that happens when selection accrues on ListView
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				cancelSelected = true;
				approveSelected = false;
				monthSelectedFromCancelOrderListView();
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
		waitingApprovalOrders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET, "order/by/id_order_status/0"+branchID , null);
		for(Order o : waitingApprovalOrders)
			waitingApprovalIDs.add(o.getIdOrder());
		ordersToApproveListView.getItems().addAll(waitingApprovalIDs);
	}
	
	/**
	 * Method to initialize the waiting approval ListView
	 */
	@SuppressWarnings("unchecked")
	private void initCancellationListView() {
		waitingCancellationOrders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET, "order/by/id_order_status/5"+branchID , null);
		for(Order o : waitingCancellationOrders)
			waitingCancellationlIDs.add(o.getIdOrder());
		ordersToCancelListView.getItems().addAll(waitingCancellationlIDs);
	}
	
	/**
	 * Method that asks the server to get the ID of the branch respectively with the current user.
	 */
	@SuppressWarnings("unchecked")
	private void setBranchID() {
		ArrayList<Store> stores = (ArrayList<Store>)MainController.getMyClient().send(MessageType.GET, "store/by/id_user/"+user.getIdUser(), null);
		branchID = stores.get(0).ordinal();		
	}
	
}
