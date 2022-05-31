package boundary.fxmlControllers;

import java.awt.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

import entity.Item;
import entity.Item.OrderItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CatalogItemController implements Initializable {

	private Item PressedItem;

	@FXML
	private Label namelabel;

	@FXML
	private Text priceText;

	@FXML
	private Label saleLbl;

	@FXML
	private Text salePriceText;

	@FXML
	private VBox catalogItemVbox;

	CatalogController catalogController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void setData(Item item, CatalogController catalogController) {
		 
		PressedItem = item;
		namelabel.setText(item.getName());
		this.catalogController = catalogController;
		if (item.getSale() == 0) {
			salePriceText.setText("");
			priceText.setText(item.getPrice() + "");
		} else {
			salePriceText.setText(item.getPrice()+ "");
			item.setPriceAfterSale(item.getSale());
			priceText.setText(item.getPrice() + "");
			saleLbl.setVisible(true);

		}

	}

	public void onPressedAddItem() {

		CartController.getOrderInProcess().addItemtoOrder(PressedItem);
		catalogController.setLabelNumItemInOrderText();

	}

	public void onItemPressed() {

		catalogController.getvboxViewItemDescription().setVisible(true);
		catalogController.getcatalogvbox().setDisable(true);
		catalogController.getnameItemLable().setText(PressedItem.getName());
		catalogController.getdescriptionLable().setText(PressedItem.getDescription());
	}

}
