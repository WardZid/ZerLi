package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import entity.BuildItem;
import entity.Item;
import entity.Item.ItemInBuild;
import entity.Item.OrderItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
/**
 *  in this class we view the item with the details and options to build 
 * @author saher
 *
 */
public class BuildItemController implements Initializable {
	/**
	 * item image
	 */
	   @FXML
	    private ImageView itemIV;
	   /**
	    * check box to select items that he wants to build the product 
	    */
	@FXML
	private CheckBox chooceItemChecBox;
/**
 * to view the item price
 */
	@FXML
	private Label priceLabel;
	/**
	 * to view the item name 
	 */
	@FXML
	private Label namelabel;
/**
 * to insert a quantity thats he wants 
 */
	@FXML
	private TextField quantityTextField;
	/**
	 * sale label
	 */
    @FXML
    private Label saleLbl2;
    /**
     * price the item  before sale 
     */
    @FXML
    private Text PriceWithoutSale;
	/**
	 * parameter from BuildItemsPageController type 
	 */
	BuildItemsPageController buildItemsPageController;
	/**
	 * parameter from Item type to save the item details and use it .
	 */
	Item presseditem;
	/**
	 * parameter from OrderItem type to save the item details and use it .
	 */
	OrderItem orderItem;
/**
 * set all the data and text from the item that we get it (name, price, amount,..)
 * @param item from Item type
 * @param buildItemsPageController from BuildItemsPageController type
 */
	public void setData(Item item, BuildItemsPageController buildItemsPageController) {
		presseditem = item;
		
		chooceItemChecBox.setText(item.getPrice() + "");
		namelabel.setText(item.getName());
		
		if(item.getImage()!=null)
			itemIV.setImage(item.getImage());
		
		this.buildItemsPageController = buildItemsPageController;
		
		
		if (item.getSale() == 0) {
			PriceWithoutSale.setText("");
			chooceItemChecBox.setText(item.getPrice() + "");
		} else {
			 
			PriceWithoutSale.setText(item.getPriceBeforeSale() + "");
 			chooceItemChecBox.setText(item.getPrice()+ "");
			saleLbl2.setVisible(true);

		}
	}
/**
 * we added a listener for the quantityTextField to check that the customer insert a correct number 
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		quantityTextField.setText(0 + "");
		quantityTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// Textfield on focus" ;
					 
				} else {
					
					// Textfield not focus";

					// Textfield is null
					if (quantityTextField.getText().trim().isEmpty()) {
						chooceItemChecBox.setSelected(false);
						quantityTextField.setDisable(true);
						quantityTextField.setText(0 + "");
						BuildItemsPageController.deleteItemIsNotselected(presseditem);
					} 
					
					else {
						// Textfield is not number
						if (!isNumeric(quantityTextField.getText())) {
							quantityTextField.setText("0");
							BuildItemsPageController.deleteItemIsNotselected(presseditem);
							chooceItemChecBox.setSelected(false);
							quantityTextField.setDisable(true);
						} else {
							// Textfield ==0
							if (Integer.parseInt(quantityTextField.getText()) == 0) {
								BuildItemsPageController.deleteItemIsNotselected(presseditem);
								chooceItemChecBox.setSelected(false);
								quantityTextField.setDisable(true);
								BuildItemsPageController.deleteItemIsNotselected(presseditem);
								
								
							}else {

							// add item
							quantityTextField.setStyle("-fx-text-fill: black; ");
							Integer.parseInt(quantityTextField.getText());
							BuildItemsPageController.SelectedItem(presseditem,
									Integer.parseInt(quantityTextField.getText()));
							}
						}
					}

				}
			}
		});

	}
/**
 * if the customer select the check box the text field disable false and set text field to 1 if it is 0
 * else disable true and set text field to 0
 * @param event if the chooceItemChecBox selected or not
 */
	public void change(ActionEvent event) {

		if (chooceItemChecBox.isSelected()) {
			// checkBox selected
			quantityTextField.setDisable(false);

			if (quantityTextField.getText().equals("0")) {
				quantityTextField.setText("1");
				BuildItemsPageController.SelectedItem(presseditem, 1);
			}
			

		} else {
			// checkBox not selected
			BuildItemsPageController.deleteItemIsNotselected(presseditem);
			quantityTextField.setDisable(true);
				quantityTextField.setText(0+"");

		}
	}
/**
 * check sting is number
 * @param strNum
 * @return
 */
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			int d = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
