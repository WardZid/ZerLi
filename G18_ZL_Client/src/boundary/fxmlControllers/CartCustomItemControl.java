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
/**
 * in this class we view the item with the details
 * @author saher
 *
 */
public class CartCustomItemControl implements Initializable {
/**
 * a plus button to increase the amount
 */
	@FXML
	private Button addBtn;
/**
 * to view the amount of the item 
 */
	@FXML
	private Label amountLabel;
/**
 * a minus button to decrease the amount
 */
	@FXML
	private Button deleteBtn;
/**
 * a button that remove the item from the ArrayList (from the cart)
 */
	@FXML
	private Button DeleteTheItemBtn;
	/**
	 * to view the name of the item
	 */
	@FXML
	private Label nameItemLabel;
/**
 * to view the price of the item
 */
	@FXML
	private Label priceLabel;
/**
 * to view the items of the products that the customer build 
 */
	@FXML
	private Button viewItemBtn; 
/**
 * a parameter from BuildItem type to save all the items details of the product that the customer build
 */
	private BuildItem PressedItem;
/**
 * a Object from CartController type 
 */
	private CartController cartController;
	/**
	 *  remove the item from the ArrayList and update the total price and the number of the items  
	 */
	 public void OnDeleteBtnPressed() {
		 int AmountForDelete=PressedItem.getAmount();
			try {

				CartController.getOrderInProcess().DeleteItemtoOrder(PressedItem,AmountForDelete);
				cartController.LoadCartBuildItem();
				setLabelsIncustomItemCartText();
				cartController.setLabelsInCartText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	 /**
	  * increase the item amount and set text for the labels and the amount
	  */
	public void onAdditemPressed() {
		CartController.getOrderInProcess().addItemtoOrder(PressedItem);
		 
		setLabelsIncustomItemCartText();

		cartController.setLabelsInCartText();
		amountLabel.setText(PressedItem.getAmount() + "");
		 
		
	}
/**
 * decrease the amount of the item and set text for labels and the amount
 */
	public void onDeletePressed() {
         int AmountForDelete=1;
		try {

			CartController.getOrderInProcess().DeleteItemtoOrder(PressedItem,AmountForDelete);
			cartController.LoadCartBuildItem();
			setLabelsIncustomItemCartText();
			cartController.setLabelsInCartText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * view the Stack pane and the CustomItemDetailsVbox() that view all the items of the product that we build
	 * @throws IOException if the action Did not succeed
	 */
	public void onViewItemPressed() throws IOException {

		cartController.getViewCustomItemDetailsVbox().setVisible(true);

		cartController.getCartItemStackPace().setDisable(true);

		loadCustomItemToVBox();
		
		
	} 
	  /**
	   * load all the item of the product that the customer build in ViewItemInBuildVBox   
	   * @throws IOException if the action Did not succeed
	   */
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
/** 
 * set all the labels (id item , name item , price , amount..)
 * @param buildItem is a parameter that have a items 
 * @param i is id and number parameter of the buildItem  
 * @param cartController Object from CartController to set
 */
	public void setData(BuildItem buildItem, int i, CartController cartController) {
		System.out.println("coustom item " + i + " Price " + buildItem.getPrice() + "");
		PressedItem = buildItem;
		PressedItem.setIdBuildItem(i);
		nameItemLabel.setText("coustom item " + i);
		setLabelsIncustomItemCartText();
		this.cartController = cartController;

	}
	
 
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
/**
 * set text of the price and amount the item
 */
	public void setLabelsIncustomItemCartText() {

		priceLabel.setText(PressedItem.getPrice() + "");
		amountLabel.setText(PressedItem.getAmount() + "");

	}
/**
 * 
 * @return PressedItem
 */
	public BuildItem getPressedItem() {
		return PressedItem;
	}
 /**
  * set the pressedItem 
  * @param pressedItem parameter from BuildItem type 
  */
	public void setPressedItem(BuildItem pressedItem) {
		PressedItem = pressedItem;
	}
/**
 * parameter from CartController type
 * @return cartController 
 */
	public CartController getCartController() {
		return cartController;
	}
/**
 * parameter from CartController type
 * @param cartController set
 */
	public void setCartController(CartController cartController) {
		this.cartController = cartController;
	}

	
	
}
