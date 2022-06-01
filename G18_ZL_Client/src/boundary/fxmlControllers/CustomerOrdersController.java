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
import javafx.stage.Modality;

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
    private VBox orderItemVB;
	

	@FXML
	private ListView<String> ordersLV;
	
    @FXML
    private VBox innerVbox;
    
    @FXML
    private VBox itemsViewVbox;
    
    @FXML
    private HBox  orderPageHBox;
    
    @FXML
    private Button backBtn;

	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 
		loadOrders();
		
		ordersLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {


			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				
				
				System.out.println("oldValue "+oldValue+" newValue "+newValue);
  				int indexorder = (Integer.parseInt(newValue))-1;
  				selectedOrder=orders.get(indexorder);
  				previewOrder( );
  				
  				try {
					loadItemToVBox( );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  				
 
			}
		});

	}
	
	private void loadItemToVBox() throws IOException {
		 
		orderItemVB.getChildren().clear();
		selectedOrder = (Order) MainController.getMyClient().send(MessageType.GET, "order/fill", selectedOrder);
		System.out.println(selectedOrder.getItems());
		
		 //load items
		for (OrderItem orderItem : selectedOrder.getItems()) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/orderItem-view.fxml"));
			Node node = fXMLLoader.load();
			
			orderItemController orderItemController = fXMLLoader.getController();
			orderItemController.setData(orderItem,null, this);
			 
			orderItemVB.getChildren().add(node );

		}
		
		// load builditem
		
		for (BuildItem buildItem : selectedOrder.getBuildItems()) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/orderItem-view.fxml"));
			Node node = fXMLLoader.load();
			
			orderItemController orderItemController = fXMLLoader.getController();
		 	orderItemController.setData(null,buildItem, this);
			 
			orderItemVB.getChildren().add(node );

		}
	}

	private void loadOrders( ) {
		int numberOrder=1;
		orders = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET, "order/by/id_customer/"+ClientConsoleController.getCustomer().getIdCustomer(),null);
				
		clearFields();

		for (Order order : orders) {
				ordersLV.getItems().add ((numberOrder++)+"");
		}
	}
	
	
	private void previewOrder( ) {
		 

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
	
	public void  onCancelOrderBtnPressed(){
		System.out.println("cancel");
		Alert errorAlert = new Alert(AlertType.ERROR);
		
		if(selectedOrder==null) {
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You have to pick order to cancel");
			errorAlert.showAndWait();
			return;
		}
		
		int remainingTime=(int)MainController.timeDiffSecond(selectedOrder.getDeliveryDate(), MainController.currentTime(),"hour");
		
		if(remainingTime<1) {
			Alert waringAlert = new Alert(AlertType.WARNING );
			waringAlert.setHeaderText(null);
			waringAlert.setContentText("The remaining time is less than 1 hour we cant ,cancele it !!!");
			waringAlert.showAndWait();
			return;
		}
		else if(remainingTime>3) {
			
			cancelOrder(100);
			
		}
		else {
			cancelOrder(50);
		}
		 
		
		
	}

	private void cancelOrder(int percent) {
		Alert confirmationAlert = new Alert(AlertType.WARNING  );
		
		if(percent==100)
		confirmationAlert.setContentText("Are you sure that you want to cancel order,\n You will get full refund - "+selectedOrder.getPrice()*percent/100);
		else
			confirmationAlert.setContentText("Are you sure that you want to cancel order,\n You will get half refund - "+selectedOrder.getPrice()*percent/100);
			
		confirmationAlert.setHeaderText("Cancel Order");
	 
		ButtonType cancelButton=new ButtonType("cancel",ButtonData.CANCEL_CLOSE) ;
		confirmationAlert.getDialogPane().getButtonTypes().add(cancelButton);
		
		Optional<ButtonType> result=confirmationAlert.showAndWait();
		if(result.isPresent()  &&result.get() == ButtonType.OK) {
			//press ok
			System.out.println("ok");
			
		} else {
			//press cancel
			//System.out.println("cancel");
		}
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

	

	public void showInnerVbox() {
		 
		orderPageHBox.setDisable(true);
		innerVbox.setVisible(true);
	}
	
      public void closeInnerVbox() {
    	  orderPageHBox.setDisable(false);
		innerVbox.setVisible(false);
	}
      
      public void onbackBtnPressed() {
    	  
    	  closeInnerVbox();
      }
      
      public VBox getInnerVbox() {
  		return innerVbox;
  	}

	public VBox getItemsViewVbox() {
		return itemsViewVbox;
	}

	public void setItemsViewVbox(VBox itemsViewVbox) {
		this.itemsViewVbox = itemsViewVbox;
	}
	 
      
 
}
