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
import javafx.scene.image.ImageView;
/**
 * in this class we view the item with his details  that exist in cart page in scroll pane  
 * @author saher
 *
 */
public class InBuildItemController implements Initializable {
/**
 * to increase the amount of the item (add item to the order)
 */
	@FXML
	private Button addBtn;
	 @FXML
	    private ImageView itemIV;
/**
 * to view the amount of the price
 */
	@FXML
	private Label amountLabel;
/**
 * to decrease the amount of the item 
 */
	@FXML
	private Button deleteBtn;
/**
 * to view the name of the item 
 */
	@FXML
	private Label nameLabel;
/**
 * to view the price of the item 
 */
	@FXML
	private Label priceLabel;
/**
 * parameter from ItemInBuild type to save the product details 
 */
	ItemInBuild itemInBuild;
	/**
	 * parameter from CartCustomItemControl type 
	 */
	CartCustomItemControl cartCustomItemControl;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

	}
/**
 * to set all the data from the itemInBuild that we get set(name, price ,amount)  
 * @param itemInBuild from ItemInBuild type
 * @param cartCustomItemControl from CartCustomItemControl type
 */
	public void setData(ItemInBuild itemInBuild, CartCustomItemControl cartCustomItemControl) {
		this.itemInBuild = itemInBuild;
		setLabelsInBuildItem();
		this.cartCustomItemControl = cartCustomItemControl;
	}
/**
 * to increase the amount of the item and add item in to order  and set the labels
 */
	public void onPressedAddItem() {

		cartCustomItemControl.getPressedItem().addItem(itemInBuild);
		CartController.getOrderInProcess().addPrice(itemInBuild.getPrice(), cartCustomItemControl.getPressedItem());

		cartCustomItemControl.setLabelsIncustomItemCartText();
		setLabelsInBuildItem();
		cartCustomItemControl.getCartController().setLabelsInCartText();
	}
/**
 * decrease the amount of the item and remove it from order and delete all the custom item if the amount =0 
 * load the item in the scroll pan in the card  
 * @throws IOException
 */
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
						.DeleteItemtoOrder(cartCustomItemControl.getPressedItem(), 1);
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
/**
 * set the amountLabel and priceLabel and nameLabel with the name and price and amount of the itemInBuild
 */
	private void setLabelsInBuildItem() {

		amountLabel.setText(itemInBuild.getAmount() + "");
		priceLabel.setText(itemInBuild.getPrice() + "");
		nameLabel.setText(itemInBuild.getName() + "");
		itemIV.setImage(itemInBuild.getImage());
	}

 

}
