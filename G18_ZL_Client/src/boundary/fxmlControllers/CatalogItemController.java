package boundary.fxmlControllers;

import java.awt.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

import entity.Item;
import entity.Item.OrderItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
/**
 * in this class we view the item details 
 * @author saher
 *
 */
public class CatalogItemController implements Initializable {
	/**
	 * item image
	 */
	   @FXML
	    private ImageView itemIV;
	   /**
	    * parameter from Item type
	    */
	private Item PressedItem;
/**
 * to view item name 
 */
	@FXML
	private Label namelabel;
/**
 * to view the item price
 */
	@FXML
	private Text priceText;
/**
 * sale label
 */
	@FXML
	private Label saleLbl;
/**
 * to view price after sale
 */
	@FXML
	private Text salePriceText;
/**
 * VBox contains item Details
 */
	@FXML
	private VBox catalogItemVbox;
/**
 * parameter from CatalogController type
 */
	CatalogController catalogController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
/**
 * we set all the label from item we get and set sale if exist
 * @param item parameter from Item type to set all the data 
 * @param catalogController from CatalogController type
 */
	public void setData(Item item, CatalogController catalogController) {
		 
		PressedItem = item;
		namelabel.setText(item.getName());
		if(item.getImage()!=null)
			itemIV.setImage(item.getImage());
		this.catalogController = catalogController;
		if (item.getSale() == 0) {
			salePriceText.setText("");
			priceText.setText(item.getPrice() + "");
		} else {
			salePriceText.setText(item.getPriceBeforeSale() + ""); 
			priceText.setText(item.getPrice()+ "");
			saleLbl.setVisible(true);

		}

	}
/**
 * add item to cart and update and set number of item label
 */
	public void onPressedAddItem() {

		CartController.getOrderInProcess().addItemtoOrder(PressedItem);
		catalogController.setLabelNumItemInOrderText();

	}
/**
 * view the VBox of item description and disable true for the catalog VBox
 */
	public void onItemPressed() {

		catalogController.getvboxViewItemDescription().setVisible(true);
		catalogController.getcatalogvbox().setDisable(true);
		catalogController.getnameItemLable().setText(PressedItem.getName());
		catalogController.getdescriptionLable().setText(PressedItem.getDescription());
	}

}
