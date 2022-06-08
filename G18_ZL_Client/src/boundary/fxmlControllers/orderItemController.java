package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.BuildItem;
import entity.Item;
import entity.Order;
import entity.Item.ItemInBuild;
import entity.Item.OrderItem;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
/**
 * in this class we view the item details the exist in Order Page 
 * that the customer has make order 
 * @author saher
 *
 */
public class orderItemController implements Initializable {
/**to view amount of the price
 * 
 */
	@FXML
	private Label amountLbl;
/**
 * to view item name 
 */
	@FXML
	private Label nameOrderLbl;
/**
 * to view item price
 */
	@FXML
	private Label priceLbl;
/**
 * to view all the items that the customer has selected them to build product 
 */
	@FXML
	private Button viewBtn;
	
	 @FXML
	  private ImageView itemIV;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
/**
 * Object from Item type to save/take all the item details 
 */
	Item orderItem;
	/**
	 * Object from BuildItem type to save/take all the item details 
	 */
	BuildItem buildItem;
	/**
	 * Object from CustomerOrdersController type to call the function from there
	 */
	CustomerOrdersController customerOrdersController;
/**
 * set all the data text from the orderItem and buildItem we get 
 * and set visible true for view button if the object from Build item type to view the items of the product 
 * @param orderItem object from OrderItem type to take the item information
 * @param buildItem object from BuildItem type to take the item information
 * @param customerOrdersController to call the some methods from there
 */
	public void setData(OrderItem orderItem, BuildItem buildItem, CustomerOrdersController customerOrdersController) {
		this.orderItem = orderItem;
		this.buildItem = buildItem;
		this.customerOrdersController = customerOrdersController;

		if (orderItem != null) {
			nameOrderLbl.setText("" + orderItem.getName());
			amountLbl.setText("x" + orderItem.getAmount());
			priceLbl.setText(orderItem.getPriceAfterSale()+ "");
			if(orderItem.getImage()!=null)
				itemIV.setImage(orderItem.getImage());
		} else {
			nameOrderLbl.setText("Custom Item");
			amountLbl.setText("x" + buildItem.getAmount());
			priceLbl.setText("");
			viewBtn.setVisible(true);
			
		}

	}
/**
 * view VBox that contains all the items and the product that the customer has selected for this order 
 * by calling  showInnerVbox method
 */
	public void onViewCustomItemPressed() {

		customerOrdersController.showInnerVbox();

		try {

			loaditemtoitemsViewVbox();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
/**
 * load all the items and products in the item VBox that exist in order that he make before
 * @throws IOException
 */
	private void loaditemtoitemsViewVbox() throws IOException {

		customerOrdersController.getItemsViewVbox().getChildren().clear();

		// load items
		for (ItemInBuild itemInBuild : buildItem.getItemsInBuild().values()) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/orderItem-view.fxml"));
			Node node = fXMLLoader.load();

			orderItemController orderItemController = fXMLLoader.getController();
			orderItemController.setDataforVbox(itemInBuild);

			customerOrdersController.getItemsViewVbox().getChildren().add(node);

		}

	}
 /**
  * set all the text from itemInBuild that we get like name amount and price for the VBox 
  * @param itemInBuild
  */
	private void setDataforVbox(ItemInBuild itemInBuild) {
		nameOrderLbl.setText("" + itemInBuild.getName());
		amountLbl.setText("x" + itemInBuild.getAmount()); 
		priceLbl.setText(itemInBuild.getPriceAfterSale() + "");
		if(itemInBuild.getImage()!=null)
			itemIV.setImage(itemInBuild.getImage());
 
	}

}
