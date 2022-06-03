package boundary.fxmlControllers;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.fxmlControllers.ClientConsoleController.Navigation;
import control.MainController;
import entity.Store;
import entity.MyMessage.MessageType;
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

public class OrderDetailsController implements Initializable {

	@FXML
	private CheckBox DeliveryNow;

	@FXML
	private HBox refundHbox;
	@FXML
	private Label fillCreditCardLable;
	@FXML
	private VBox orderDetailsVbox;
	@FXML
	private VBox PaymentVbox;

	@FXML
	private Button NextBtn;

	@FXML
	private Label Note;
	@FXML
	private Label refundLable1;
	@FXML
	private Label refundLable2;

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

	@FXML
	private Label required7;
	@FXML
	private Label required8;
	@FXML
	private TextField credutCardtextfield;
	private int flag = 0;

	private static ArrayList<String> StoreAddressName = new ArrayList<String>();
	private static ArrayList<String> houreNum = new ArrayList<String>();
	private static ArrayList<String> MinNum = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (ClientConsoleController.getCustomer().getPoint() >= CartController.getOrderInProcess().getPrice()) {
			refundLable2.setText(ClientConsoleController.getCustomer().getPoint() + "");
			Note.setVisible(true);
			refundHbox.setVisible(true);
		}

		refundLable1.setText(ClientConsoleController.getCustomer().getPoint() + "");
		this.getPaymentVbox().setVisible(false);

		credutCardtextfield.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {

				if (newPropertyValue) {
					// Textfield on focus" ;

				} else {

					// Textfield is null
					if (credutCardtextfield.getText().trim().isEmpty()) {
						credutCardtextfield.setText(0 + "");
					}

					else {
						// Textfield is not number

						if (!isNumeric(credutCardtextfield.getText())) {
							credutCardtextfield.setText("0");
						} else {
							if (credutCardtextfield.getText().length() > 6) {
								credutCardtextfield.setText("0");

							}
							// Textfield ==0
							if (Integer.parseInt(credutCardtextfield.getText()) == 0) {
								credutCardtextfield.setText("0");

							} else {

								credutCardtextfield.setStyle("-fx-text-fill: black; ");
								Integer.parseInt(credutCardtextfield.getText());
								Integer.parseInt(credutCardtextfield.getText());
							}
						}
					}

				}
			}
		});

		noteLable.setVisible(false);
		required1.setVisible(false);
		required2.setVisible(false);
		required3.setVisible(false);
		required4.setVisible(false);
		required5.setVisible(false);
		required6.setVisible(false);
		required7.setVisible(false);
		required8.setVisible(false);
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

	public void change(ActionEvent event) {
		if (DeliveryNow.isSelected() == true) {
			flag = 0;
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

	public void SetNodeFillAllFeilds(boolean status) {
		noteLable.setVisible(status);
		required1.setVisible(status);
		required2.setVisible(status);
		required3.setVisible(status);
		required4.setVisible(status);
		required5.setVisible(status);
		required7.setVisible(status);
		required8.setVisible(status);

	}

	public void onPaymentPressed() {
		if (ClientConsoleController.getCustomer().getPoint() < CartController.getOrderInProcess().getPrice()) {
			if (credutCardtextfield.getText().trim().isEmpty()
					|| Integer.parseInt(credutCardtextfield.getText()) == 0) {
				required7.setVisible(true);
				fillCreditCardLable.setVisible(true);
			} else {
				credutCardtextfield.setDisable(true);
				fillCreditCardLable.setVisible(false);
				SetOrderDetails();
			}
		} else {

			SetOrderDetails();
			
		}
	}

	public void SetOrderDetails() {

		CartController.getOrderInProcess().setGreetingCard(GreetingArea.getText());
		if (DeliveryNow.isSelected() == false)
			CartController.getOrderInProcess().setDeliveryDate(DelevireyDatePicker.getValue().toString() + " "
					+ HourCombo.getValue() + ":" + MinutesCombo.getValue() + ":00");
		if (DeliveryNow.isSelected() == false)
			CartController.getOrderInProcess().setDeliveryDate(null);
		CartController.getOrderInProcess().setIdCustomer(ClientConsoleController.getCustomer().getIdCustomer());
		CartController.getOrderInProcess().setDescription(DescribtionArea.getText());
		CartController.getOrderInProcess().setIdOrderStatus(0);
		CartController.getOrderInProcess().setAddress(AddressText.getText());
		CartController.getOrderInProcess().setOrderDate(MainController.currentTime());
		if (StoreAddressCombo.getValue() != null)
			CartController.getOrderInProcess().setStore(Store.valueOf(StoreAddressCombo.getValue()));

		//// for the price and refund

		/// if customer added shipping
		if (AddShippingCheckBox.isSelected() == true) {
			if (ClientConsoleController.getCustomer().getPoint() == 0)
				CartController.getOrderInProcess().addPriceForShipping();
			else {

				// refund > price order
				if (ClientConsoleController.getCustomer().getPoint() >= CartController.getOrderInProcess().getPrice()) {
					ClientConsoleController.getCustomer().setPoint(ClientConsoleController.getCustomer().getPoint()
							- CartController.getOrderInProcess().getPrice());
					// +update refund in DB
				}
				// if refund < price order
				else {
					System.out.println("you have to pay " + (ClientConsoleController.getCustomer().getPoint()
							- CartController.getOrderInProcess().getPrice()));
					ClientConsoleController.getCustomer().setPoint(0);
					// +update refund in DB
				}
				CartController.getOrderInProcess().addPriceForShipping();
			}
			System.out.println("Refund =" + ClientConsoleController.getCustomer().getPoint());
		}

		//// without shipping

		else {
			if (ClientConsoleController.getCustomer().getPoint() != 0) {
				// refund > price order
				if (ClientConsoleController.getCustomer().getPoint() >= CartController.getOrderInProcess().getPrice()) {
					ClientConsoleController.getCustomer().setPoint(ClientConsoleController.getCustomer().getPoint()
							- CartController.getOrderInProcess().getPrice());
					// +update refund in DB
				}
				// if refund < price order
				else {
					System.out.println("you have to pay " + (CartController.getOrderInProcess().getPrice()
							- ClientConsoleController.getCustomer().getPoint()));
					ClientConsoleController.getCustomer().setPoint(0);
					// +update refund in DB
				}

			}
			System.out.println("Refund =" + ClientConsoleController.getCustomer().getPoint());
			
		}
		CartController.NewOrder();
		Navigation.navigator("catalog-view.fxml");
	}

	public VBox getPaymentVbox() {
		return PaymentVbox;
	}

	public VBox getorderDetailsVbox() {
		return orderDetailsVbox;
	}

	public void OnNextBtnPressed() {
		if (DelevireyDatePicker.getValue() != null && HourCombo.getValue() != null && MinutesCombo.getValue() != null)
			flag = 1;
		if (flag == 1) {
			String delevireyDateTime = DelevireyDatePicker.getValue().toString() + " " + HourCombo.getValue() + ":"
					+ MinutesCombo.getValue() + ":00";
			if (MainController.timeDiffHour(delevireyDateTime, MainController.currentTime()) < 3) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("You should pick correct time");
				errorAlert.showAndWait();

			} else
				flag = 0;
		}

		if ((DelevireyDatePicker.getValue() == null && DeliveryNow.isSelected() == false)
				|| StoreAddressCombo.getValue() == null
				|| (HourCombo.getValue() == null && DeliveryNow.isSelected() == false)
				|| (MinutesCombo.getValue() == null && DeliveryNow.isSelected() == false)) {
			SetNodeFillAllFeilds(true);
		} else {

			if (AddShippingCheckBox.isSelected() == true) {

				if (AddressText.getText().trim().isEmpty() || PhoneText.getText().trim().isEmpty()
						|| NameReceiverText.getText().trim().isEmpty()) {
					SetNodeFillAllFeilds(true);
				} else {
					if (flag == 0) {
						this.getPaymentVbox().setVisible(true);
						getorderDetailsVbox().setDisable(true);
					}
				}

			} else {
				if (flag == 0) {
					SetNodeFillAllFeilds(false);
					this.getPaymentVbox().setVisible(true);
					getorderDetailsVbox().setDisable(true);
				}
			}

		}
	}

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
