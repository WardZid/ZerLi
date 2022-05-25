package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Store;
import entity.User;
import entity.WaitingOrder;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*			
									
									1.
UPDATE assignment3.order O 
SET O.id_order_status = 1 
WHERE O.id_order = orderID

UPDATE -> waiting/approve/orderID

public static int updateOrderToApproved(String orderID){
		int er; // number of effected rows
		try {
			er = statement.executeQuery("UPDATE assignment3.order O SET O.id_order_status = 1 WHERE O.id_order ="+orderID);		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return er;
}

									2.
SELECT O.id_order , C.name_customer as name , C.phone_customer as phone , O.date_order , O.delivery_date_order as delivery_date , O.address_order as address , O.date_cancel as c_date , O.price_order as overall 
FROM assignment3.customer C , assignment3.order O
WHERE O.id_order_status = order_status AND C.id_customer = O.id_customer AND O.id_store = branchID

class WaitingOrder(int orderID, String name, String phone, String o_date, String request_date, String address, double overall)
 				
GET -> waiting/approve/orders/branchID 									
GET -> waiting/cancel/orders/branchID
																		   
public static ArrayList<WaitingOrder> getWaitingOrders(String branch_id, int order_status){
		ArrayList<WaitingOrder> waitings = new ArrayList<>();
		ResultSet rs = null;
		try {
			if(order_status == 0){
				rs = statement.executeQuery("SELECT O.id_order , C.name_customer as name , C.phone_customer as phone , O.date_order , O.delivery_date_order as delivery_date , O.address_order as address , O.price_order as overall FROM assignment3.customer C , assignment3.order O WHERE O.id_order_status =" + order_status + "AND C.id_customer = O.id_customer AND O.id_store =" + branchID);
				rs.beforeFirst(); // ---move back to first row
				while (rs.next()) {
					waitings.add(new WaitingOrder(
							rs.getInt("id_order"),
							rs.getString("name"),                       # NO NEED TO COPY #
							rs.getString("phone"),				     # SAME METHOD AS ABOVE#
							rs.getString("date_order"),
							rs.getString("delivery_date"),
							rs.getString("address"),
							rs.getDouble("overall")));
				}
			}
			else if(order_status == 5){
				rs = statement.executeQuery("SELECT O.id_order , C.name_customer as name , C.phone_customer as phone , O.date_order , O.delivery_date_order as delivery_date , O.address_order as address , O.date_cancel as c_date , O.price_order as overall FROM assignment3.customer C , assignment3.order O WHERE O.id_order_status ="+order_status+"AND C.id_customer = O.id_customer AND O.id_store = "+branchID);
				rs.beforeFirst(); // ---move back to first row
				while (rs.next()) {
					waitings.add(new WaitingOrder(
							rs.getInt("id_order"),
							rs.getString("name"),                       
							rs.getString("phone"),				     
							rs.getString("date_order"),
							rs.getString("delivery_date"),
							rs.getString("address"),
							rs.getString("c_date"),
							rs.getDouble("overall")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return waitings;
}									
					
									3.
																	
									
 * */

public class BranchManagerOrdersController implements Initializable {

	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
	@FXML
    private Text addressText;

    @FXML
    private Button approveButton;

    @FXML
    private Text buyerNameText;

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
    private Text phoneText;
	
	
	/* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
	
    /* ArrayList to save the waiting approval orders in the ListView */
    private static ArrayList<WaitingOrder> waitingApprovalOrders;
    private static ArrayList<Integer> waitingApprovalIDs;
    
    /* ArrayList to save the waiting cancellation orders in the ListView */
    private static ArrayList<WaitingOrder> waitingCancellationOrders;
    private static ArrayList<Integer> waitingCancellationlIDs;
    
    /* the current branch manager's branch(store) ID */
    private static int branchID;
    
    /* to save the user info */
    private static User user = ClientConsoleController.getUser();
    
    /* the selected order's ID */
    private static Integer selectedOrderID;
    
    /* the current selected order */
    private static WaitingOrder currentOrder;
    
    /* if an order is selected from the approve LsitView */
    private static boolean approveSelected = false;
    
    /* if an order is selected from the cancel LsitView */
    private static boolean cancelSelected = false;
	
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
		this.cancelButton.setDisable(true);
		saveOrderIDfromApproveList();
		setOrderDetails();
	}
	
	/**
	 * Action when a line is selected in the approveListView. 
	 */
	public void monthSelectedFromCancelOrderListView() {
		this.approveButton.setDisable(true);
		this.cancelButton.setDisable(false);
		saveOrderIDfromCancelList();
		setOrderDetails();
		
		// METHOD NOT DONE! MAYBE NO NEED,
		saveSelectedCancelOrderCancelationDate();
	}
	
	
	/* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * To set the details of the selected order.
	 */
	private void setOrderDetails() {
		getOrderBySelection();
		this.orderIDText.setText(currentOrder.getOrderID()+"");
		this.buyerNameText.setText(currentOrder.getName());
		this.phoneText.setText(currentOrder.getPhone());
		this.dateOfOrderText.setText(currentOrder.getO_date());
		this.deliveryDateText.setText(currentOrder.getRequest_date());
		this.addressText.setText(currentOrder.getAddress());
		this.overallOrderToPayText.setText(currentOrder.getOverall()+"");
	}
	
	/**
	 * To save the order that is selected from the ListViews
	 */
	private void getOrderBySelection() {
		if(approveSelected == true) {
			for(WaitingOrder wo : waitingApprovalOrders) {
				if(selectedOrderID.equals(wo.getOrderID())) {
					currentOrder = wo;
				}
			}
		}
		if(cancelSelected == true) {
			for(WaitingOrder wo : waitingCancellationOrders) {
				if(selectedOrderID.equals(wo.getOrderID())) {
					currentOrder = wo;
				}
			}
		}
	}
	
	/**
	 * To save the order id if it is selected from Approve ListView
	 */
	private void saveOrderIDfromApproveList(){
		selectedOrderID = ordersToApproveListView.getSelectionModel().getSelectedItem();
	}
	
	/**
	 * To save the order id if it is selected from Cancel ListView
	 */
	private void saveOrderIDfromCancelList(){
		selectedOrderID = ordersToCancelListView.getSelectionModel().getSelectedItem();
	}

	
	/**
	 *  to save the date of the cancellation of the selected order in cancel ListView.
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
		waitingApprovalOrders = (ArrayList<WaitingOrder>) MainController.getMyClient().send(MessageType.GET, ""+branchID , null);
		for(WaitingOrder wo : waitingApprovalOrders)
			waitingApprovalIDs.add(wo.getOrderID());
		ordersToApproveListView.getItems().addAll(waitingApprovalIDs);
	}
	
	/**
	 * Method to initialize the waiting approval ListView
	 */
	@SuppressWarnings("unchecked")
	private void initCancellationListView() {
		waitingCancellationOrders = (ArrayList<WaitingOrder>) MainController.getMyClient().send(MessageType.GET, ""+branchID , null);
		for(WaitingOrder wo : waitingCancellationOrders)
			waitingCancellationlIDs.add(wo.getOrderID());
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
