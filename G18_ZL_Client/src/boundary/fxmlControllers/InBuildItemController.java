package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entity.BuildItem;
import entity.Item.ItemInBuild;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class InBuildItemController implements Initializable {

	@FXML
	private Button addBtn;

	@FXML
	private Label amountLabel;

	@FXML
	private Button deleteBtn;

	@FXML
	private Label nameLabel;

	@FXML
	private Label priceLabel;

	ItemInBuild itemInBuild;
	CartCustomItemControl cartCustomItemControl;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setData(ItemInBuild itemInBuild, CartCustomItemControl cartCustomItemControl) {
		this.itemInBuild = itemInBuild;
		setLabelsInBuildItem();
		this.cartCustomItemControl = cartCustomItemControl;
	}

	public void onPressedAddItem() {

		cartCustomItemControl.getPressedItem().addItem(itemInBuild);
		CartController.getOrderInProcess().addPrice(itemInBuild.getPrice(), cartCustomItemControl.getPressedItem());

		cartCustomItemControl.setLabelsIncustomItemCartText();
		setLabelsInBuildItem();
		cartCustomItemControl.getCartController().setLabelsInCartText();
	}

	public void onPressedDelete() throws IOException {

		// item in build have Amount above 1
		if (itemInBuild.getAmount() > 1) {
			cartCustomItemControl.getPressedItem().deleteItem(itemInBuild);
			CartController.getOrderInProcess().deletePrice(itemInBuild.getPrice(),
					cartCustomItemControl.getPressedItem());

		} else {
			// delete all custom item
			if (cartCustomItemControl.getPressedItem().getItemsInBuild().size() == 1) {
				cartCustomItemControl.getCartController().getOrderInProcess()
						.DeleteItemtoOrder(cartCustomItemControl.getPressedItem());
				cartCustomItemControl.getCartController().onPressedbacktoCart();
			} else
			// if item have 1 Amount remove from Vbox inbuildItem
			{
				cartCustomItemControl.getPressedItem().deleteItem(itemInBuild);
				CartController.getOrderInProcess().deletePrice(itemInBuild.getPrice(),
						cartCustomItemControl.getPressedItem());

			}
			cartCustomItemControl.loadCustomItemToVBox();

		}

		cartCustomItemControl.getCartController().LoadCartBuildItem();
		setLabelsInBuildItem();
		cartCustomItemControl.getCartController().setLabelsInCartText();
	}

	private void setLabelsInBuildItem() {

		amountLabel.setText(itemInBuild.getAmount() + "");
		priceLabel.setText(itemInBuild.getPrice() + "");
		nameLabel.setText(itemInBuild.getName() + "");
	}

	@FXML
	void onDeleteItemFromBuild() {

	}

}
