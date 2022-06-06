package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.Customer;
import entity.Customer.CustomerStatus;
import entity.User;
import entity.User.UserType;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BranchManagerCustomersController implements Initializable {

	private HashMap<User, Customer> userMap = new HashMap<>();

	private User selectedUser = null;
	private Customer selectedCustomer = null;

	// FXML COMPONENTS
	@FXML
	private Button activateBtn;

	@FXML
	private TextField cardTF;

	@FXML
	private Text cardText;

	@FXML
	private Button freezeBtn;

	@FXML
	private Text idCustomerText;

	@FXML
	private Text idUserText;

	@FXML
	private Text nameText;

	@FXML
	private Text usernameText;

	@FXML
	private Text passwordText;

	@FXML
	private Text emailText;
	
    @FXML
    private Button registerBtn;

	@FXML
	private Text phoneText;

	@FXML
	private Text pointText;

	@FXML
	private VBox registerVBox;

	@FXML
	private Label statusLbl;

	@FXML
	private VBox statusVBox;

	@FXML
	private ListView<String> unregisteredLV;

	@FXML
	private ListView<String> registeredLV;

	@SuppressWarnings("unchecked")
	@FXML
	void onActivate() {
		selectedCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
		Customer after=((ArrayList<Customer>)MainController.getMyClient().send(MessageType.UPDATE,"customer/status",selectedCustomer)).get(0);
		if(after.getCustomerStatus()==CustomerStatus.FROZEN)
			selectedCustomer.setCustomerStatus(CustomerStatus.FROZEN);
		setCustomerInfo(after);
	}

	@SuppressWarnings("unchecked")
	@FXML
	void onFreeze() {
		selectedCustomer.setCustomerStatus(CustomerStatus.FROZEN);
		Customer after=((ArrayList<Customer>)MainController.getMyClient().send(MessageType.UPDATE,"customer/status",selectedCustomer)).get(0);
		if(after.getCustomerStatus()==CustomerStatus.ACTIVE)
			selectedCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
		setCustomerInfo(after);
	}

	@FXML
	void onRegister() {
		if((boolean)MainController.getMyClient().send(MessageType.POST,"customer/"+cardTF.getText(),selectedUser)) {
			resetAllInfo();
			fetchUsersCustomers();
			initLists();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		fetchUsersCustomers();
		initLists();

		unregisteredLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				int userId = Integer.parseInt(newValue.split(" - ")[1]);
				for (User user : userMap.keySet()) {
					if (user.getIdUser() == userId) {
						setUserInfo(user);
						registerVBox.setDisable(false);
						resetCustomerInfo();
						break;
					}
				}
				registeredLV.getSelectionModel().clearSelection();
			}
		});

		registeredLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				int userId = Integer.parseInt(newValue.split(" - ")[1]);
				for (User user : userMap.keySet()) {
					if (user.getIdUser() == userId) {
						setUserInfo(user);
						setCustomerInfo(userMap.get(user));
						registerVBox.setDisable(true);
						break;
					}
				}
				unregisteredLV.getSelectionModel().clearSelection();
			}
		});

		cardTF.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//only numbers
				if (!newValue.matches("\\d*")) {
					cardTF.setText(newValue.replaceAll("[^\\d]", ""));
				}
				//size<=16
				if (cardTF.getText().length() > 16) {
	                String s = cardTF.getText().substring(0, 16);
	                cardTF.setText(s);
	            }
				//if size = 16
				if(cardTF.getText().length() == 16)
					registerBtn.setDisable(false);
				else
					registerBtn.setDisable(true);
			}
		});
	}

	// HELPER METHODS

	@SuppressWarnings("unchecked")
	private void fetchUsersCustomers() {
		ArrayList<User> userCustomers = (ArrayList<User>) MainController.getMyClient().send(MessageType.GET,
				"user/by/id_user_type/" + UserType.CUSTOMER.ordinal(), null);
		for (User user : userCustomers) {
			if (user.getIdCustomer() > 0) {
				Customer cus = ((ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET,
						"customer/by/id_customer/" + user.getIdCustomer(), null)).get(0);
				userMap.put(user, cus);
			} else
				userMap.put(user, null);
		}
	}

	private void initLists() {
		registeredLV.getItems().clear();
		unregisteredLV.getItems().clear();

		for (User user : userMap.keySet()) {
			if (userMap.get(user) == null)
				unregisteredLV.getItems().add("User ID - " + user.getIdUser());
			else
				registeredLV.getItems().add("User ID - " + user.getIdUser());
		}

	}

	private void setUserInfo(User user) {
		resetUserInfo();
		
		selectedUser=user;
		
		idUserText.setText(user.getIdUser() + "");
		nameText.setText(user.getName());
		usernameText.setText(user.getUsername());
		passwordText.setText(user.getPassword());
		emailText.setText(user.getEmail());
		phoneText.setText(user.getPhone());

	}

	private void setCustomerInfo(Customer cus) {
		resetCustomerInfo();
		
		selectedCustomer=cus;
		
		idCustomerText.setText(cus.getIdCustomer() + "");
		cardText.setText(cus.getCard());
		pointText.setText(cus.getPoint() + "");
		statusLbl.setText(cus.getCustomerStatus().toString());
		statusLbl.setText(cus.getCustomerStatus().toString());
		if (cus.getCustomerStatus() == CustomerStatus.ACTIVE) {
			statusLbl.setStyle("-fx-background-color: #1de9b6");
			freezeBtn.setDisable(false);
			activateBtn.setDisable(true);
		} else {
			statusLbl.setStyle("-fx-background-color: #ff5000");
			freezeBtn.setDisable(true);
			activateBtn.setDisable(false);
		}
	}

	private void resetAllInfo() {
		resetUserInfo();
		resetCustomerInfo();
	}

	private void resetUserInfo() {
		selectedUser=null;
		idUserText.setText("---");
		usernameText.setText("---");
		passwordText.setText("---");
		nameText.setText("---");
		emailText.setText("---");
		phoneText.setText("---");

		cardTF.clear();
		registerVBox.setDisable(true);
	}

	private void resetCustomerInfo() {
		selectedCustomer=null;
		idCustomerText.setText("---");
		cardText.setText("---");
		pointText.setText("---");

		statusLbl.setText("STATUS");
		freezeBtn.setDisable(true);
		activateBtn.setDisable(true);
		statusLbl.setStyle("-fx-background-color: white");
	}

}
