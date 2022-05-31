package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.fxmlControllers.ClientConsoleController.Navigation;
import control.MainController;
import entity.Store;
import entity.MyMessage.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class OrderDetailsController implements Initializable {
	@FXML
	private ComboBox<String> MinutesCombo;
	@FXML
	private CheckBox AddShippingCheckBox;

	@FXML
	private TextField AddressText;

	@FXML
	private Button BackBtn;

	@FXML
	private DatePicker DelevireyDatePicker;

	@FXML
	private TextArea GreetingArea;

	@FXML
	private ComboBox<String> HourCombo;

	@FXML
	private TextField NameReceiverText;

	@FXML
	private Button PaymentBtn;

	@FXML
	private TextField PhoneText;

	@FXML
	private ComboBox<String> StoreAddressCombo;

	@FXML
	private VBox shippingVbox;

	@FXML
	private TextArea DescribtionArea;

	@FXML
	private Label noteLable;

	@FXML
	private Label required1;

	@FXML
	private Label required2;

	@FXML
	private Label required3;

	@FXML
	private Label required4;

	@FXML
	private Label required5;

	@FXML
	private Label required6;

	private static ArrayList<String> StoreAddressName = new ArrayList<String>();
	private static ArrayList<String> houreNum = new ArrayList<String>();
	private static ArrayList<String> MinNum = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		noteLable.setVisible(false);
		required1.setVisible(false);
		required2.setVisible(false);
		required3.setVisible(false);
		required4.setVisible(false);
		required5.setVisible(false);
		required6.setVisible(false);

		shippingVbox.setVisible(false);

		StoreAddressName = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "store/all", null);
		ObservableList<String> storeAddress = FXCollections.observableArrayList();
		storeAddress.setAll(StoreAddressName);
		StoreAddressCombo.setItems(storeAddress);

		for (int i = 0; i < 24; i++)
			houreNum.add("" + i);

		ObservableList<String> Houre = FXCollections.observableArrayList();
		Houre.setAll(houreNum);
		HourCombo.setItems(Houre);

		for (int i = 0; i < 60; i++)
			MinNum.add("" + i);
		ObservableList<String> Minutes = FXCollections.observableArrayList();
		Minutes.setAll(MinNum);
		MinutesCombo.setItems(Minutes);

	}

	public void OnBackBtnPressed() {
		Navigation.navigator("cart-view.fxml");
	}

	public void ShippingSelected() {
		if (AddShippingCheckBox.isSelected() == false) {
			shippingVbox.setVisible(true);
		} else {
			shippingVbox.setVisible(false);
		}

	}

	public void onPaymentPressed() {
		if (DelevireyDatePicker.getValue() == null || StoreAddressCombo.getValue() == null
				|| HourCombo.getValue() == null || MinutesCombo.getValue() == null) {

			noteLable.setVisible(true);
			required1.setVisible(true);
			required2.setVisible(true);
			required3.setVisible(true);
			required4.setVisible(true);
			required5.setVisible(true);
			required6.setVisible(true);

		} else {
			
			if (AddShippingCheckBox.isSelected() == true) {
				System.out.println(" print inside"+AddressText.getText() + PhoneText.getText()  + NameReceiverText.getText() +"1" );
				
				if (AddressText.getText().trim().isEmpty() || PhoneText.getText().trim().isEmpty()
						|| NameReceiverText.getText().trim().isEmpty()) {
					noteLable.setVisible(true);
					required1.setVisible(true);
					required2.setVisible(true);
					required3.setVisible(true);
					required4.setVisible(true);
					required5.setVisible(true);
					required6.setVisible(true);
						
					 
				}
				 
			}
			else {
			
				noteLable.setVisible(false);
				required1.setVisible(false);
				required2.setVisible(false);
				required3.setVisible(false);
				required4.setVisible(false);
				required5.setVisible(false);
				required6.setVisible(false);
				CartController.getOrderInProcess().setGreetingCard(GreetingArea.getText());
				CartController.getOrderInProcess().setDeliveryDate(DelevireyDatePicker.getValue().toString()+" "+HourCombo.getValue()+":"+MinutesCombo.getValue()+":00");
				CartController.getOrderInProcess().setIdCustomer(ClientConsoleController.getCustomer().getIdCustomer());
				CartController.getOrderInProcess().setDescription(DescribtionArea.getText());
				CartController.getOrderInProcess().setIdOrderStatus(0);
				CartController.getOrderInProcess().setAddress(AddressText.getText());
				CartController.getOrderInProcess().addPriceForShipping();
				if (StoreAddressCombo.getValue() != null)
					CartController.getOrderInProcess().setStore(Store.valueOf(StoreAddressCombo.getValue()));

				System.out.println(CartController.getOrderInProcess().toString());
				System.out.println(DelevireyDatePicker.getValue().toString()+" "+HourCombo.getValue()+":"+MinutesCombo.getValue()+":00");
			}
		}
 

	}
}