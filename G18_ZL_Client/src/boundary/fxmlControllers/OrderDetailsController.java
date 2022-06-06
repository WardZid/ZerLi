package boundary.fxmlControllers;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.fxmlControllers.ClientConsoleController.Navigation;
import control.MainController;
import entity.Store;
import entity.MyMessage.MessageType;
import entity.Order;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
/**
 * In this class we  an order and we set the details of the order in DB 
 * and we check the payment if all pass successfully
 * we check if all the details is correct and all  pass successfully
 * @author saher
 *
 */
public class OrderDetailsController implements Initializable {
	/**
	 * check box for the refund if the customer wants to use refund
	 */
    @FXML
    private CheckBox useRefundCheckBox;
    /**
     * check box for the delivery now if the customer wants to delivery in current time 
     */
	@FXML
	private CheckBox DeliveryNow;



	@FXML
	private VBox orderDetailsVbox;
	/**
	 * Vbox for the payment window to show it or make it invisible
	 */
	@FXML
	private VBox PaymentVbox;

	/**
	 * this button to move to payment page
	 */
	@FXML
	private Button NextBtn;

/**
 * this label update the refund for the customer  
 */
	@FXML
	private Label refundLable1;

/**
 *  in this combo box we insert the minutes from 0 to 59 
 */
	@FXML
	private ComboBox<String> MinutesCombo;
	/**
	 *check box if the customer wants to add shipping for the order
	 */
	@FXML
	private CheckBox AddShippingCheckBox;
/**
 * text field the save the address of the customer 
 */
	@FXML
	private TextField AddressText;
/**
 * to back to Cart page
 */
	@FXML
	private Button BackBtn;
/**
 * the customer selected the date of delivery 
 */
	@FXML
	private DatePicker DelevireyDatePicker;
/**
 * the customer maybe wants to insert a greeting
 */
	@FXML
	private TextArea GreetingArea;
/**
 *  in this combo box we insert the hours from 0 to 23 
 */
	@FXML
	private ComboBox<String> HourCombo;
/**
 * the customer insert his name when he add shipping
 */
	@FXML
	private TextField NameReceiverText;
/**
 * when the customer finished from order and he wants to pay then he clicked on this button
 */
	@FXML
	private Button PaymentBtn;
/**
 * the customer insert his phone number when he select to add shipping 
 */
	@FXML
	private TextField PhoneText;
/**
 * this combo box to insert the addresses of the stores in this combo box
 */
	@FXML
	private ComboBox<String> StoreAddressCombo;
/**
 * to make the shipping details visible or invisible we puts the details in this vbox
 */
	@FXML
	private VBox shippingVbox;
/**
 * if the customer wants to add description for the order
 */
	@FXML
	private TextArea DescribtionArea;
/**
 * this label in first we hide it and we make it visible when the customer didn't fill all the required fields 
 */
	@FXML
	private Label noteLable;
/**
 * we make this label * visible when the customer didn't fill all the required fields
 */
	@FXML
	private Label required1;
	/**
	 * we make this label * visible when the customer didn't fill all the required fields
	 */
	@FXML
	private Label required2;
	/**
	 * we make this label * visible when the customer didn't fill all the required fields
	 */
	@FXML
	private Label required3;
	/**
	 * we make this label * visible when the customer didn't fill all the required fields
	 */
	@FXML
	private Label required4;
	/**
	 * we make this label * visible when the customer didn't fill all the required fields
	 */
	@FXML
	private Label required5;
	/**
	 * we make this label * visible when the customer didn't fill all the required fields
	 */
	@FXML
	private Label required6;
	/**
	 * we make this label * visible when the customer didn't fill all the required fields
	 */

	@FXML
	private Label required8;
	/**
	 * we insert the credit card from DB to this TextField 
	 */
	@FXML
	private TextField credutCardtextfield;
	/**
	 * this flag to make sure that you can make the payment page visible or not 
	 */
	private int flagToMakePayment = 0;
/**
 * we insert from DB to this ArrayList all the address of the stores
 */
	private static ArrayList<String> StoreAddressName = new ArrayList<String>();
	/**
	 * we insert from DB to this ArrayList all the hours
	 */
	private static ArrayList<String> houreNum = new ArrayList<String>();
	/**
	 * we insert from DB to this ArrayList all the minutes
	 */
	private static ArrayList<String> MinNum = new ArrayList<String>();

	
	/**
	 * we added a listener for phoneText to make sure that the customer insert a correct thing
	 * we make all the Notes invisible in first 
	 * in first we make also the shipping details invisible 
	 * and we insert the details to all the combo box (store address, hour,minutes)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	
		this.getPaymentVbox().setVisible(false);

		PhoneText.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					// Textfield on focus" ;

				} else {

					// Textfield is null
					if ((AddShippingCheckBox.isSelected() == true && PhoneText.getText().trim().isEmpty())) {
						PhoneText.setText(0 + "");
					}

					else {
						// Textfield is not number

						if ( (AddShippingCheckBox.isSelected() == true && !isNumeric(PhoneText.getText()))) {
							PhoneText.setText(0 + "");
						} else {
							if ( (AddShippingCheckBox.isSelected() == true
									&& (PhoneText.getText().length() > 10 || PhoneText.getText().length() < 10))) {
								PhoneText.setText(0 + "");
							}
							// Textfield ==0
							if ( (AddShippingCheckBox.isSelected() == true
											&& Integer.parseInt(PhoneText.getText()) == 0)) {
								PhoneText.setText(0 + "");
							} else {
								PhoneText.setStyle("-fx-text-fill: black; ");
								Integer.parseInt(PhoneText.getText());
							}
						}
					}

				}
			}
		});
		

		SetNodeFillAllFeilds(false);

		
		shippingVbox.setVisible(false);

		StoreAddressName = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "store/all", null);
		ObservableList<String> storeAddress = FXCollections.observableArrayList();
		storeAddress.setAll(StoreAddressName);
		StoreAddressCombo.setItems(storeAddress);

		// fill combo box with hours
		for (int i = 0; i < 24; i++) {
			houreNum.add(String.format("%02d", i));
		}

		ObservableList<String> Houre = FXCollections.observableArrayList();
		Houre.setAll(houreNum);
		HourCombo.setItems(Houre);

		// fill combo box with Minutes
		for (int i = 0; i < 60; i++) {
			MinNum.add(String.format("%02d", i));
		}
		
		ObservableList<String> Minutes = FXCollections.observableArrayList();
		Minutes.setAll(MinNum);
		MinutesCombo.setItems(Minutes);

	}
/**
 * this function to make sure if the customer selected the DeliveryNow check box then
 * we make the other combo box of the delivery is disable true (the minutes ,hour , DelevireyDatePicker) 
 * else we make it  disable false (the minutes ,hour , DelevireyDatePicker) 
 * @param event check box DeliveryNow if selected or not 
 */
	public void change(ActionEvent event) {
		if (DeliveryNow.isSelected() == true) {
			flagToMakePayment = 0;
			DelevireyDatePicker.setValue(null);
			MinutesCombo.setValue(null);
			HourCombo.setValue(null);
			DelevireyDatePicker.setDisable(true);
			MinutesCombo.setDisable(true);
			HourCombo.setDisable(true);

		} else {

			DelevireyDatePicker.setDisable(false);
			MinutesCombo.setDisable(false);
			HourCombo.setDisable(false);

		}

	}
/**
 * this function if we clicked on Back button we move to Cart page  
 */
	public void OnBackBtnPressed() {
		Navigation.navigator("cart-view.fxml");
	}
/**
 * this function to make the shipping details (shippingVbox) visible or invisible when the customer select shipping check box
 */
	public void ShippingSelected() {
		if (AddShippingCheckBox.isSelected() == false) {
			shippingVbox.setVisible(true);
		} else {
			shippingVbox.setVisible(false);
		}

	}
/**
 * to make all this label visible or invisible 
 * @param status false or true 
 */
	public void SetNodeFillAllFeilds(boolean status) {
		noteLable.setVisible(status);
		required1.setVisible(status);
		required2.setVisible(status);
		required3.setVisible(status);
		required4.setVisible(status);
		required5.setVisible(status);
		required6.setVisible(status);
		required8.setVisible(status);
 
	}
/**
 * when we clicked on payment button we make sure that all the order details is saved in DB and 
 * we returned to Catalog page if the customer wants to make new order
 */
	public void onPaymentPressed() {
		
			SetOrderDetailsAndSend();

	}
/**
 * in this function we set all the order details and we saved the details in DB 
 * and we check if this is the first order to this customer then he have a 20% sale
 * and we checked if the customer has select a shipping then we added a 20$ to the total price 
 * and we checked if the customer has select to use his refund if he have a refund
 * then we finished we moved to catalog page and we make a new order if the customer wants to add order  
 */
	public void SetOrderDetailsAndSend() {

		if (DeliveryNow.isSelected() == false)
			CartController.getOrderInProcess().setDeliveryDate(DelevireyDatePicker.getValue().toString() + " "
					+ HourCombo.getValue() + ":" + MinutesCombo.getValue() + ":00");
		if (DeliveryNow.isSelected() == true)
			CartController.getOrderInProcess().setDeliveryDate(null);
		CartController.getOrderInProcess().setIdCustomer(ClientConsoleController.getCustomer().getIdCustomer());
		CartController.getOrderInProcess().setGreetingCard(GreetingArea.getText());
		CartController.getOrderInProcess().setDescription(DescribtionArea.getText());
		CartController.getOrderInProcess().setIdOrderStatus(0);
		CartController.getOrderInProcess().setAddress(AddressText.getText());
		CartController.getOrderInProcess().setOrderDate(MainController.currentTime());

		System.out.println(CartController.getOrderInProcess());

		if (StoreAddressCombo.getValue() != null)
			CartController.getOrderInProcess().setStore(Store.valueOf(StoreAddressCombo.getValue()));

		//// for the price and refund

		/// if customer added shipping
		if (AddShippingCheckBox.isSelected() == true) {
			CartController.getOrderInProcess().addPriceForShipping();
			if (ClientConsoleController.getCustomer().getPoint() == 0) {}
//				CartController.getOrderInProcess().addPriceForShipping();
			else {
				if(useRefundCheckBox.isSelected()==true) {
				// refund > price order
				if (ClientConsoleController.getCustomer().getPoint() >= CartController.getOrderInProcess().getPrice()) {
				 
					MainController.getMyClient().send(MessageType.UPDATE, "customer/point/"+ClientConsoleController.getCustomer().getIdCustomer()+"/"+-CartController.getOrderInProcess().getPrice(), null);
					ClientConsoleController.getCustomer().setPoint(ClientConsoleController.getCustomer().getPoint()
							- CartController.getOrderInProcess().getPrice());
				}
				// if refund < price order
				else {
					System.out.println("you have to pay " + (ClientConsoleController.getCustomer().getPoint()
							- CartController.getOrderInProcess().getPrice()));
					 
					MainController.getMyClient().send(MessageType.UPDATE, "customer/point/"+ClientConsoleController.getCustomer().getIdCustomer()+"/"+-ClientConsoleController.getCustomer().getPoint(), null);
					ClientConsoleController.getCustomer().setPoint(0);
				}
				
			}
				
			}
			System.out.println("Refund =" + ClientConsoleController.getCustomer().getPoint());
		}

		//// without shipping

		else {
			
			if (ClientConsoleController.getCustomer().getPoint() != 0) {
				if(useRefundCheckBox.isSelected()==true) {
				// refund > price order
				if (ClientConsoleController.getCustomer().getPoint() >= CartController.getOrderInProcess().getPrice()) {
					MainController.getMyClient().send(MessageType.UPDATE, "customer/point/"+ClientConsoleController.getCustomer().getIdCustomer()+"/"+-CartController.getOrderInProcess().getPrice(), null);
					ClientConsoleController.getCustomer().setPoint(ClientConsoleController.getCustomer().getPoint()
							- CartController.getOrderInProcess().getPrice());
				}
				// if refund < price order
				else {
					System.out.println("you have to pay " + (CartController.getOrderInProcess().getPrice()
							- ClientConsoleController.getCustomer().getPoint()));
					
					MainController.getMyClient().send(MessageType.UPDATE, "customer/point/"+ClientConsoleController.getCustomer().getIdCustomer()+"/"+-ClientConsoleController.getCustomer().getPoint(), null);
					ClientConsoleController.getCustomer().setPoint(0);
				}

			} 
			}
			System.out.println("check Cb =" + useRefundCheckBox.isSelected());
			System.out.println("Refund =" + ClientConsoleController.getCustomer().getPoint());

		}
		
		// this ArrayList to check if the customer have an orders before or not ,if he didn't have we give him a 20% sale
		@SuppressWarnings("unchecked")
		ArrayList<Order> orderForFirstTime = (ArrayList<Order>) MainController.getMyClient().send(MessageType.GET,
				"order/by/id_customer/" + ClientConsoleController.getCustomer().getIdCustomer(), null);
		if (orderForFirstTime.size() == 0) {
			CartController.getOrderInProcess().setPrice(CartController.getOrderInProcess().getPrice() * 0.8);
			System.out.println("order size= " + orderForFirstTime.size());
		}
		
		
		System.out.println("ordder==" + CartController.getOrderInProcess());
		MainController.getMyClient().send(MessageType.POST, "order", CartController.getOrderInProcess());
		CartController.NewOrder();
		Navigation.navigator("catalog-view.fxml");
	}

	/**
	 * this button exist on payment form if the customer does not want to pay 
	 * and he wants to return to order details page he click on cancel button
	 */
	public void CancelBtnPressed() {
		PaymentVbox.setVisible(false);
		getorderDetailsVbox().setDisable(false);
	}
/**
 * this function returns PaymentVbox (the payment form)
 * @return VBox PaymentVbox
 */
	public VBox getPaymentVbox() {
		return PaymentVbox;
	}
	/**
	 * this function returns orderDetailsVbox (the Order details form)
	 * @return VBox orderDetailsVbox
	 */
	public VBox getorderDetailsVbox() {
		return orderDetailsVbox;
	}

	/**
	 * on this function we check before to move to payment form if the customer insert all the field correctly
	 * and we check if he selected a correctly time and we  get the point for the customer from DB and set the refund text  on payment form
	 * and we check if the phone number that the customer insert is correct else he get a alert message 
	 */
	public void OnNextBtnPressed() {
		credutCardtextfield.setText(ClientConsoleController.getCustomer().getCard());
		int point = (int) MainController.getMyClient().send(MessageType.GET, "customer/point/"+ClientConsoleController.getCustomer().getIdCustomer(),null);
		refundLable1.setText(point + "");
		if (DelevireyDatePicker.getValue() != null && HourCombo.getValue() != null && MinutesCombo.getValue() != null)
			flagToMakePayment = 1;
		if (flagToMakePayment == 1) {
			String delevireyDateTime = DelevireyDatePicker.getValue().toString() + " " + HourCombo.getValue() + ":"
					+ MinutesCombo.getValue() + ":00";
			if (MainController.timeDiffHour(delevireyDateTime, MainController.currentTime()) < 3) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("You should pick correct time");
				errorAlert.showAndWait();

			}
			
			
			else
				flagToMakePayment = 0;
		}
		
		if ((DelevireyDatePicker.getValue() == null && DeliveryNow.isSelected() == false)
				|| StoreAddressCombo.getValue() == null
				|| (HourCombo.getValue() == null && DeliveryNow.isSelected() == false)
				|| (MinutesCombo.getValue() == null && DeliveryNow.isSelected() == false)) {
			SetNodeFillAllFeilds(true);
	
		} else {
		
			if (AddShippingCheckBox.isSelected() == true) {
				
				 if (PhoneText.getText().trim().isEmpty() || Integer.parseInt(PhoneText.getText()) == 0) {
						Alert errorAlert = new Alert(AlertType.ERROR);
						errorAlert.setHeaderText(null);
						errorAlert.setContentText("uncorrect Phone Number");
						errorAlert.showAndWait();}
				 
				if (AddressText.getText().trim().isEmpty() || PhoneText.getText().trim().isEmpty()
						|| NameReceiverText.getText().trim().isEmpty()|| Integer.parseInt(PhoneText.getText()) == 0) {
					SetNodeFillAllFeilds(true);
					 
				} else {
					if (flagToMakePayment == 0) {
						this.getPaymentVbox().setVisible(true);
						getorderDetailsVbox().setDisable(true);
					}
				}

			} else {
				if (flagToMakePayment == 0) {
					SetNodeFillAllFeilds(false);
					this.getPaymentVbox().setVisible(true);
					getorderDetailsVbox().setDisable(true);
				}
			}

		}

	
	}
/**
 * this function checked if the string contains just numbers 
 * @param strNum string from text field that customer insert 
 * @return true if number else false 
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
