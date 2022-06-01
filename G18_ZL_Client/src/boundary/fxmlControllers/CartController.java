package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.ClientView;
import boundary.fxmlControllers.ClientConsoleController.Navigation;
import entity.BuildItem;
import entity.Item;
import entity.Item.OrderItem;
import entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CartController implements Initializable {

	@FXML
	private Button CheckOutBtn;

	@FXML
	private Label TotalPrice;

	@FXML
	private GridPane gridCustom;

	@FXML
	private GridPane gridItem;

	@FXML
	private Label numItems;

	@FXML
	private ScrollPane scroll1;

	@FXML
	private ScrollPane scroll2;

	@FXML
	private VBox viewCustomItemDetailsVbox;

	@FXML
	private VBox cartItemVBox;

	@FXML
	private Button backtoCartBtn;

	@FXML
	private VBox viewItemInBuildVBox;

	private static Order orderInProcess = new Order();

	private ArrayList<OrderItem> items;
	private ArrayList<BuildItem> BuildItems;

	public void LoadCartItem() throws IOException {
		int column = 0;
		int row = 1;
		gridItem.getChildren().clear();

		items = orderInProcess.getItems();
		for (int i = 0; i < items.size(); i++) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/cart-item-view.fxml"));
			Node node = fXMLLoader.load();

			CartItemControl cartItemControl = fXMLLoader.getController();
			cartItemControl.setData(items.get(i), this);
			if (column == 1) {
				column = 0;
				row++;
			}

			gridItem.add(node, column++, row);
		}

	}

	public void LoadCartBuildItem() throws IOException {
		gridCustom.getChildren().clear();
		int column = 0;
		int row = 1;
		BuildItems = getOrderInProcess().getBuildItems();
		if(BuildItems==null)
 		for (int i = 0; i < BuildItems.size(); i++) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/cart-CustomItem-view.fxml"));
			Node node = fXMLLoader.load();

			CartCustomItemControl cartItemControl = fXMLLoader.getController();
			cartItemControl.setData(BuildItems.get(i), i, this);
			if (column == 1) {
				column = 0;
				row++;
			}

			gridCustom.add(node, column++, row);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.getViewCustomItemDetailsVbox().setVisible(false);
		items = orderInProcess.getItems();
		setLabelsInCartText();

		try {
			LoadCartItem();
			LoadCartBuildItem();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setLabelsInCartText() {

		numItems.setText(getOrderInProcess().getItemInOrder() + "");
		TotalPrice.setText(getOrderInProcess().getPrice() + "");

	}

	public VBox getViewCustomItemDetailsVbox() {
		return viewCustomItemDetailsVbox;
	}

	public void setViewCustomItemDetailsVbox(VBox viewCustomItemDetailsVbox) {
		this.viewCustomItemDetailsVbox = viewCustomItemDetailsVbox;
	}

	public VBox getCartItemStackPace() {
		return cartItemVBox;
	}

	public void setCartItemStackPace(VBox cartItemStackPace) {
		this.cartItemVBox = cartItemStackPace;
	}

	public void onPressedbacktoCart() {
		this.setLabelsInCartText();
		this.getViewCustomItemDetailsVbox().setVisible(false);
		this.getCartItemStackPace().setDisable(false);

	}

	public VBox getViewItemInBuildVBox() {
		return viewItemInBuildVBox;
	}

	public void setViewItemInBuildVBox(VBox viewItemInBuildVBox) {
		this.viewItemInBuildVBox = viewItemInBuildVBox;
	}

	public static Order getOrderInProcess() {
		return orderInProcess;
	}

	public static void setOrderInProcess(Order order) {
		CartController.orderInProcess = order;

	}

	public void SetTotalPrice() {
		TotalPrice.setText(orderInProcess.getPrice() + "");
		numItems.setText(orderInProcess.getItemInOrder() + "");
	}

	public void CheckOutPressed() {
		if(!(items.isEmpty()) || !(BuildItems.isEmpty())) {
 		Navigation.navigator("Order-Details-view.fxml");
 		
		}
	}

}