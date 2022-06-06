package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entity.Item;
import entity.Item.OrderItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
/**
 * we view the item that he select with the details of the items
 * @author saher
 *
 */
public class CartItemControl implements Initializable {
	/**
	 * to increase the amount of the item
	 */
	@FXML
    private Button addBtn;
	/**
	 * contains all the details of the item
	 */
    @FXML
    private StackPane stackPaneforItem;
    /**
     * item image
     */
    @FXML
    private ImageView itemIV;
   /**
    * to view item amount
    */
    @FXML
    private Label amountLabel;
/**
 * to decrease item amount
 */
    @FXML
    private Button deleteBtn;
/**
 * to view item name 
 */
    @FXML
    private Label nameItemLabel;
/**
 * to view item price
 */
    @FXML
    private Label priceLabel;
    /**
     * a parameter from OrderItem type to save all details of the item
     */
    private OrderItem PressedItem;
    /**
     * parameter from CartController type 
     */
    CartController cartController;
    
  /**
   * set all the label text (amount, price, name,image ,PressedItem) 
   * @param item from OrderItem type to set all the details of the item 
   * @param cartController from CartController type 
   */
    public void setData(OrderItem item, CartController cartController) {
		PressedItem=item;
		System.out.println("here setData"+item);
		nameItemLabel.setText(item.getName());
		priceLabel.setText(    item.getPrice()+"");
		amountLabel.setText(item.getAmount()+"");
		this.cartController=cartController;
		if(item.getImage()!=null)
		itemIV.setImage(item.getImage());
	}
    /**
     * to add item on the order and update/increase the amount and the price and set amountLabel text
     */
    public void onAdditemPressed() {

    	CartController.getOrderInProcess().addItemtoOrder(PressedItem);
    	amountLabel.setText(PressedItem.getAmount()+"");
    	cartController.SetTotalPrice();
	}
    /**
     * to decrease the amount of the item and update the price of the order and set text for the amountLable 
     */
    public void onAddDeletePressed() {
    	
    	 try {
    		   CartController.getOrderInProcess().DeleteItemtoOrder(PressedItem,1);
    	    	amountLabel.setText(PressedItem.getAmount()+"");	
    	    	cartController.SetTotalPrice();
			cartController.LoadCartItem();
		} catch (IOException e) {
			e.printStackTrace();
		}
    
/**
 * remove the item from the order and update the order price and the amount and set text for the labels 
 */
    }
    public void OnDeleteBtnPressed() {
    	CartController.getOrderInProcess().DeleteItemFromScroll(PressedItem);
    	cartController.setLabelsInCartText();
    	try {
			cartController.LoadCartItem();
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
