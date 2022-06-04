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

public class CartItemControl implements Initializable {
	@FXML
    private Button addBtn;
    @FXML
    private StackPane stackPaneforItem;
    @FXML
    private ImageView itemIV;
    @FXML
    private Label amountLabel;

    @FXML
    private Button deleteBtn;

    @FXML
    private Label nameItemLabel;

    @FXML
    private Label priceLabel;
    private OrderItem PressedItem;
    
    CartController cartController;
    
  
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
    public void onAdditemPressed() {

    	CartController.getOrderInProcess().addItemtoOrder(PressedItem);
    	amountLabel.setText(PressedItem.getAmount()+"");
    	cartController.SetTotalPrice();
	}
    
    public void onAddDeletePressed() {
    	
    	 try {
    		   CartController.getOrderInProcess().DeleteItemtoOrder(PressedItem,1);
    	    	amountLabel.setText(PressedItem.getAmount()+"");	
    	    	cartController.SetTotalPrice();
			cartController.LoadCartItem();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    

    }
    public void OnDeleteBtnPressed() {
    	CartController.getOrderInProcess().DeleteItemFromScroll(PressedItem);
    	cartController.setLabelsInCartText();
    	try {
			cartController.LoadCartItem();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
