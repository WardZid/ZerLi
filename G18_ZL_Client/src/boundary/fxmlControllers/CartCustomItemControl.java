package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import entity.BuildItem;
import entity.Item.ItemInBuild;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CartCustomItemControl implements Initializable {

	@FXML
	private Button addBtn;

	@FXML
	private Label amountLabel;

	@FXML
	private Button deleteBtn;

	@FXML
	private Label nameItemLabel;

	@FXML
	private Label priceLabel;

	@FXML
	private Button viewItemBtn;

	private BuildItem PressedItem;

	private CartController cartController;

	public void onAdditemPressed() {
		CartController.getOrderInProcess().addItemtoOrder(PressedItem);
		setLabelsIncustomItemCartText();

		cartController.setLabelsInCartText();
	}

	public void onDeletePressed() {

		try {

			CartController.getOrderInProcess().DeleteItemtoOrder(PressedItem);
			cartController.LoadCartBuildItem();
			setLabelsIncustomItemCartText();
			cartController.setLabelsInCartText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onViewItemPressed() throws IOException {

		cartController.getViewCustomItemDetailsVbox().setVisible(true);

		cartController.getCartItemStackPace().setDisable(true);

		loadCustomItemToVBox();
	} 

	public void loadCustomItemToVBox() throws IOException {
		cartController.getViewItemInBuildVBox().getChildren().clear();

		for (ItemInBuild itemInBuild : PressedItem.getItemsInBuild().values()) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/inBuildItem-view.fxml"));
			Node node = fXMLLoader.load();
			
			InBuildItemController inBuildItemController = fXMLLoader.getController();
			
		 	inBuildItemController.setData(itemInBuild,this);
			cartController.getViewItemInBuildVBox().getChildren().add(node );

		}

	}

	public void setData(BuildItem buildItem, int i, CartController cartController) {
		System.out.println("coustom item " + i + " Price " + buildItem.getPrice() + "");
		PressedItem = buildItem;
		nameItemLabel.setText("coustom item " + i);
		setLabelsIncustomItemCartText();
		this.cartController = cartController;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setLabelsIncustomItemCartText() {

		priceLabel.setText(PressedItem.getPrice() + "");
		amountLabel.setText(PressedItem.getAmount() + "");

	}

	public BuildItem getPressedItem() {
		return PressedItem;
	}

	public void setPressedItem(BuildItem pressedItem) {
		PressedItem = pressedItem;
	}

	public CartController getCartController() {
		return cartController;
	}

	public void setCartController(CartController cartController) {
		this.cartController = cartController;
	}

	
	
}
