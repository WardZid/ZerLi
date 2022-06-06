package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.ClientView;
import boundary.fxmlControllers.ClientConsoleController.Navigation;
import entity.BuildItem;
import entity.Item.OrderItem;
import entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
/**
 * in this class we view all the items that the customer selected or build it  with there amount and price and all the details 
 * @author saher
 *
 */
public class CartController implements Initializable {

	/**
	 * button in cart page to move to order details page
	 */
	@FXML
	private Button CheckOutBtn;
/**
 * label the view the total price of the order 
 */
	@FXML
	private Label TotalPrice;
/**
 * VBox that contains custom item and details that he choose to build  
 */
    @FXML
    private VBox customVBox;
/**
 * VBox thats contains item details that he selected 
 */
    @FXML
    private VBox itemVBox;
/**
 * view the number of the item that he selected 
 */
	@FXML
	private Label numItems;
/**
 * this scroll pane to view all items that he selected 
 */
	@FXML
	private ScrollPane scroll1;
/**
 * this scroll pane view all the items that he build 
 */
	@FXML
	private ScrollPane scroll2;
/**
 * this VBox view all details of the product that the customer build it 
 */
	@FXML
	private VBox viewCustomItemDetailsVbox;
/**
 * VBox thats view all the details of Cart Page 
 */
	@FXML
	private VBox cartItemVBox;
/**
 * button that exist in viewItemInBuildVBox that returns us to cart form 
 */
	@FXML
	private Button backtoCartBtn;
/**
 * contains all details of  the items to the product that the customer build it 
 */
	@FXML
	private VBox viewItemInBuildVBox;

	/**
	 * we saved all details of the order in this parameter
	 */
	private static Order orderInProcess = new Order();
/**
 * to saved all the items
 */
	private ArrayList<OrderItem> items;
	/**
	 * to save all the build items 
	 */
	private ArrayList<BuildItem> BuildItems;

	/**
	 * we load all the item that the customer has selected on VBox itemVBox
	 * @throws IOException if this action did not pass successfully then throw Exception
	 */
	public void LoadCartItem() throws IOException {
		itemVBox.getChildren().clear();

		items = orderInProcess.getItems();
		for (int i = 0; i < items.size(); i++) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/cart-item-view.fxml"));
			Node node = fXMLLoader.load();

			CartItemControl cartItemControl = fXMLLoader.getController();
			cartItemControl.setData(items.get(i), this);
			
			itemVBox.getChildren().add(node);
		}

	}
	/**
	 * we load all the build items that the customer has build on VBox customVBox
	 * @throws IOException if this action did not pass successfully then throw Exception
	 */
	public void LoadCartBuildItem() throws IOException {
		customVBox.getChildren().clear();
		BuildItems = getOrderInProcess().getBuildItems();
		
		 
 		for (int i = 0; i < BuildItems.size(); i++) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/cart-CustomItem-view.fxml"));
			Node node = fXMLLoader.load();
			CartCustomItemControl cartCustomItemControl = fXMLLoader.getController();
			cartCustomItemControl.setData(BuildItems.get(i), i, this);
			
			customVBox.getChildren().add(node);
		}
	}

	/**
	 * we make the VBox viewCustomItemDetailsVbox invisible and we set and  update the number of the items 
	 * and the total price and we load all the items that he selected and build in first 
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.getViewCustomItemDetailsVbox().setVisible(false);
		items = orderInProcess.getItems();
		setLabelsInCartText();

		try {
			LoadCartItem();
			LoadCartBuildItem();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
/**
 * set for the number of items and the total price 
 */
	public void setLabelsInCartText() {

		numItems.setText(getOrderInProcess().getItemInOrder() + "");
		TotalPrice.setText( String.format("%.2f", getOrderInProcess().getPrice()));

	}
	
	/**
	 * get the viewCustomItemDetailsVbox (the form of the items of the product that  he build )
	 * @return VBox viewCustomItemDetailsVbox
	 */

	public VBox getViewCustomItemDetailsVbox() {
		return viewCustomItemDetailsVbox;
	}

	/**
	 * set for the VBox viewCustomItemDetailsVbox that we load on it the products 
	 * @param viewCustomItemDetailsVbox set 
	 */
	public void setViewCustomItemDetailsVbox(VBox viewCustomItemDetailsVbox) {
		this.viewCustomItemDetailsVbox = viewCustomItemDetailsVbox;
	}
	/**
	 * get the cartItemVBox VBox (form for the details of the cart )
	 * @return VBox cartItemVBox
	 */

	public VBox getCartItemStackPace() {
		return cartItemVBox;
	}
	/**
	 * set for the VBox cartItemVBox that contains all the details of the cart
	 * @param cartItemStackPace set 
	 */
	public void setCartItemStackPace(VBox cartItemStackPace) {
		this.cartItemVBox = cartItemStackPace;
	}

	/**
	 * return from the CustomItemDetailsVbox to the cart page and make CustomItemDetailsVbox invisible and cart page disable false (and visible)
	 */
	public void onPressedbacktoCart() {
		this.setLabelsInCartText();
		this.getViewCustomItemDetailsVbox().setVisible(false);
		this.getCartItemStackPace().setDisable(false);

	}
/**
 * returns the VBox of the items for products that the customer build 
 * @return VBox viewItemInBuildVBox
 */
	public VBox getViewItemInBuildVBox() {
		return viewItemInBuildVBox;
	}
/**
 * set the VBox of the items for products that the customer build 
 * @param viewItemInBuildVBox set 
 */
	public void setViewItemInBuildVBox(VBox viewItemInBuildVBox) {
		this.viewItemInBuildVBox = viewItemInBuildVBox;
	}
/**
 * returns the order that we saved all the order details on this parameter (orderInProcess)
 * @return orderInProcess
 */
	public static Order getOrderInProcess() {
		return orderInProcess;
	}
/**
 * 
 * @param order we set the orderInProcess in order
 */
	public static void setOrderInProcess(Order order) {
		CartController.orderInProcess = order;

	}
/**
 * we set the text of the TotalPrice and numItems
 */
	public void SetTotalPrice() {
		TotalPrice.setText(String.format("%.2f", orderInProcess.getPrice())  );
		numItems.setText(orderInProcess.getItemInOrder() + "");
	}

	/**
	 * first we check if the customer selected items to pay then we move to order details page 
	 * else he must select items first
	 */
	public void CheckOutPressed() {
		if(!(items.isEmpty()) || !(BuildItems.isEmpty())) {
 		Navigation.navigator("Order-Details-view.fxml");
 		
		}
		else {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You should select Items first");
			errorAlert.showAndWait();
		}
	}
/**
 * we make a new order 
 */
	public static void NewOrder() {
		orderInProcess = new Order();
		
	}

}