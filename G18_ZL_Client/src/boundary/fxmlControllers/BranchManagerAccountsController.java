package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Customer;
import entity.Customer.CustomerStatus;
import entity.MyMessage.MessageType;
import entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*
								1.
				add the following to serverController
				1- "user/by/id_user_status/3"
				2- "survey/id/all"
				3- "user/customer/by/id_customer_status/0 or 1 or 2"


 * */

public class BranchManagerAccountsController implements Initializable {

	/* ------------------------------------------------ */
	/* \/ FXML Variables \/ */
	/* ------------------------------------------------ */

	@FXML
	private Button approveUserButton;

	@FXML
	private Button appointSurveyButton;

	@FXML
	private Button changeStatusButton;

	@FXML
	private Button changeUserButton;

	@FXML
	private ListView<String> customersListView;

	@FXML
	private ListView<String> newUsersListView;

	@FXML
	private ListView<String> storeWorkersListView;

	@FXML
	private ChoiceBox<String> customerTypeChoiceBox;

	@FXML
	private ChoiceBox<String> surveyIDChoiceBox;

	@FXML
	private TextField cardNumberTextField;

	@FXML
	private TextField newUserCardTextField;

	@FXML
	private Text userIDText;

	@FXML
	private Text emailText;

	@FXML
	private Text fullNameText;

	@FXML
	private Text phoneText;

	@FXML
	private Text userNameText;

	@FXML
	private Text error1;

	@FXML
	private Text error2;

	@FXML
	private Text error3;

	@FXML
	private Text error4;

	/* ------------------------------------------------ */
	/* \/ Help Variables \/ */
	/* ------------------------------------------------ */

	// array list of unapproved users
	private ArrayList<Customer> unapprovedArrayList;

	// array list of approved users
	private ArrayList<Customer> approvedCustomersArrayList;

	// array list of frozen users
	private ArrayList<Customer> frozenCustomersArrayList;

	// array list of workers
	private ArrayList<User> workersArrayList;

	// array list of survey IDs
	private ArrayList<String> surveyIDsArrayList;

	// the current selected User
//	private User selectedUser;

	// the current selected Customer
	private Customer selectedCustomer;

	// the default error messages
	private String defError1, defError2, defError3, defError4;

	/* ------------------------------------------------ */
	/* \/ initialize function \/ */
	/* ------------------------------------------------ */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		saveErrorMessages();
		initListViews();
		initChoiceBoxes();
		addTextLimiter();
		setActionOnListViews();
	}

	/* ------------------------------------------------ */
	/* \/ Action Methods \/ */
	/* ------------------------------------------------ */

	/**
	 * Method to do when a line is selected from the new users List View
	 */
	private void newUserSelectedAction() {
		String[] split = newUsersListView.getSelectionModel().getSelectedItem().split(" - ");
		String id = split[0];
		setAllToDefault();
		for (Customer c : unapprovedArrayList) {
			String uid = c.getIdUser() + "";
			if (uid.equals(id)) {
				selectedCustomer = c;
			}
		}
		this.newUserCardTextField.setDisable(false);
		this.approveUserButton.setDisable(false);
		this.appointSurveyButton.setDisable(true);
		this.changeStatusButton.setDisable(true);
		this.changeUserButton.setDisable(true);
		setUserDetailsInText();
	}

	/**
	 * Method to do when a line is selected from the customers List View
	 */
	private void customerSelectedAction() {
		String[] split = customersListView.getSelectionModel().getSelectedItem().split(" - ");
		String id = split[0];
		setAllToDefault();
		for (Customer c : approvedCustomersArrayList) {
			String uid = c.getIdUser() + "";
			if (uid.equals(id)) {
				selectedCustomer = c;
			}
		}
		for (Customer c : frozenCustomersArrayList) {
			String uid = c.getIdUser() + "";
			if (uid.equals(id)) {
				selectedCustomer = c;
			}
		}
		this.cardNumberTextField.setDisable(false);
		this.customerTypeChoiceBox.setDisable(false);
		this.approveUserButton.setDisable(true);
		this.appointSurveyButton.setDisable(true);
		this.changeStatusButton.setDisable(false);
		this.changeUserButton.setDisable(false);
		setUserDetailsInText();
	}

	/**
	 * Method to do when a line is selected from the workers List View
	 */
	private void workerSelectedAction() {
		String[] split = storeWorkersListView.getSelectionModel().getSelectedItem().split(" - ");
		String id = split[0];
		setAllToDefault();
		for (User u : workersArrayList) {
			String uid = u.getIdUser() + "";
			if (uid.equals(id)) {
				selectedUser = u;
			}
		}
		this.surveyIDChoiceBox.setDisable(false);
		this.approveUserButton.setDisable(true);
		this.appointSurveyButton.setDisable(false);
		this.changeStatusButton.setDisable(true);
		this.changeUserButton.setDisable(false);
		setUserDetailsInText();
	}

	@SuppressWarnings("unchecked")
	@FXML
	/**
	 * approve the selected user - switch account to an approved customer
	 * 
	 * @param event
	 */
	private void approveUserButtonPressed(ActionEvent event) {
		if (this.newUserCardTextField.getText().isEmpty()) {
			// if the new user card number text field is empty
			setAllToDefault();
			error1.setVisible(true);
		} else {
			// if the text field is not empty
			try {
				hideErrors();
				setDefaultErrorMessages();
				int Value = Integer.parseInt(newUserCardTextField.getText());
				
				selectedCustomer.setIdCustomerStatus(1);
				selectedCustomer.setCard(newUserCardTextField.getText());
				ArrayList<Customer> c2 = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.UPDATE,"customer/status", selectedCustomer);
				if (c2.get(0).getCard().equals(newUserCardTextField.getText())
						&& c2.get(0).getIdCustomerStatus() == 1) {
					System.out.println("Customer Updatded!");
				} else {
					System.out.println("Failed to updat customer!");
				}
			} catch (NumberFormatException e) {
				error1.setText("Enter Only Numbers!");
				error1.setVisible(true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	/**
	 * appoint the selected worker to set the survey answers
	 * 
	 * @param event
	 */
	private void appointSurveyButtonPressed(ActionEvent event) {
		if (this.surveyIDChoiceBox.getSelectionModel().isEmpty()) {
			// if there is no selection
			setAllToDefault();
			error3.setVisible(true);
		} else {
			// if there is a selection
			hideErrors();
			setDefaultErrorMessages();
			String choice = this.surveyIDChoiceBox.getSelectionModel().getSelectedItem();
			User userUpdate;
			for (User u : workersArrayList) {
				if (u.getIdUser() == selectedUser.getIdUser()) {
					userUpdate = u;
				}
			}
			// userUpdate.set
			// TO BE CPNTINUED ...
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	/**
	 * change the selected customer status to the selected option in choice box
	 * 
	 * @param event
	 */
	private void changeStatusButtonPressed(ActionEvent event) {
		if (this.customerTypeChoiceBox.getSelectionModel().isEmpty()) {
			// if there is no selection
			setAllToDefault();
			error2.setVisible(true);
		} else {
			// if there is a selection
			hideErrors();
			setDefaultErrorMessages();
			String choice = this.customerTypeChoiceBox.getSelectionModel().getSelectedItem();
			ArrayList<Customer> c1 = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET,
					"customer/by/id_user/" + selectedUser.getIdUser(), null);
			Customer customerUpdate = c1.get(0);
			customerUpdate.setIdCustomerStatus(CustomerStatus.valueOf(choice).ordinal());
			ArrayList<Customer> c2 = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.UPDATE,
					"customer", customerUpdate);
			if (c2.get(0).getIdCustomerStatus() == CustomerStatus.valueOf(choice).ordinal()) {
				System.out.println("Customer Updatded!");
			} else {
				System.out.println("Failed to updat customer!");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	/**
	 * @param event
	 */
	private void changeUserButtonPressed(ActionEvent event) {
		if (this.cardNumberTextField.getText().isEmpty()) {
			// if there is no text in the text field
			setAllToDefault();
			error4.setVisible(true);
		} else {
			// if there is text in text field
			try {
				int Value = Integer.parseInt(cardNumberTextField.getText());
				ArrayList<Customer> c1 = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET,
						"customer/by/id_user/" + selectedUser.getIdUser(), null);
				Customer customerUpdate = c1.get(0);
				customerUpdate.setCard(cardNumberTextField.getText());
				ArrayList<Customer> c2 = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.UPDATE,
						"customer", customerUpdate);
				if (c2.get(0).getCard().equals(cardNumberTextField.getText())) {
					System.out.println("Customer Updatded!");
				} else {
					System.out.println("Failed to updat customer!");
				}
			} catch (NumberFormatException e) {
				error4.setText("Enter Only Numbers!");
				error4.setVisible(true);
			}
		}
	}

	/* ------------------------------------------------ */
	/* \/ Help Methods \/ */
	/* ------------------------------------------------ */

	/**
	 * Method to save the default error messages
	 */
	private void saveErrorMessages() {
		defError1 = error1.getText();
		defError2 = error2.getText();
		defError3 = error3.getText();
		defError4 = error4.getText();
	}

	/**
	 * Method to disable all buttons
	 */
	private void disableAllButtons() {
		this.approveUserButton.setDisable(true);
		this.appointSurveyButton.setDisable(true);
		this.changeStatusButton.setDisable(true);
		this.changeUserButton.setDisable(true);
	}

	/**
	 * Method to hide all errors
	 */
	private void hideErrors() {
		error1.setVisible(false);
		error2.setVisible(false);
		error3.setVisible(false);
		error4.setVisible(false);
	}

	/**
	 * Method to epty all the text fields
	 */
	private void emptyTextFields() {
		this.cardNumberTextField.clear();
		this.newUserCardTextField.clear();
	}

	/**
	 * Method to unselect all the choice boxes
	 */
	private void unselectChoiceBoxes() {
		this.customerTypeChoiceBox.getSelectionModel().clearSelection();
		this.surveyIDChoiceBox.getSelectionModel().clearSelection();
	}

	/**
	 * Method to set the default error messages
	 */
	private void setDefaultErrorMessages() {
		error1.setText(defError1);
		error2.setText(defError2);
		error3.setText(defError3);
		error4.setText(defError4);
	}

	/**
	 * Method to disable all the choice boxes
	 */
	private void disableChoiceBoxes() {
		this.customerTypeChoiceBox.setDisable(true);
		this.surveyIDChoiceBox.setDisable(true);
	}

	/**
	 * Method to disable all the text fields
	 */
	private void disableTextFields() {
		this.cardNumberTextField.setDisable(true);
		this.newUserCardTextField.setDisable(true);
	}

	/**
	 * Method to return the window to default state
	 */
	private void setAllToDefault() {
		disableChoiceBoxes();
		disableTextFields();
		disableAllButtons();
		hideErrors();
		emptyTextFields();
		unselectChoiceBoxes();
		setDefaultErrorMessages();
	}

	/**
	 * Method to initialize the list views.
	 */
	private void initListViews() {
		initUnapprovedUsersListView();
		initCustomersListView();
		initWorkersListView();
	}

	/**
	 * Method to initialize all choice boxes
	 */
	private void initChoiceBoxes() {
		initSurveyIDChoiceBox();
		initCustomerTypeChoiceBox();
	}

	private void setActionOnListViews() {
		setActionOnUnapprovedListView();
		setActionOnCustomerListView();
		setActionOnWorkersListView();
	}

	/**
	 * Method to initialize Unapproved users list view.
	 */
	@SuppressWarnings("unchecked")
	private void initUnapprovedUsersListView() {
		unapprovedArrayList = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET,
				"customer/by/id_customer_status/0", null);
		newUsersListView.getItems().clear();
		for (User u : unapprovedArrayList) {
			newUsersListView.getItems().add(u.getIdUser() + " - " + u.getUsername());
		}
	}

	/**
	 * Method to initialize the customers list view.
	 */
	@SuppressWarnings("unchecked")
	private void initCustomersListView() {
		approvedCustomersArrayList = (ArrayList<User>) MainController.getMyClient().send(MessageType.GET,
				"user/customer/by/id_customer_status/1", null);
		frozenCustomersArrayList = (ArrayList<User>) MainController.getMyClient().send(MessageType.GET,
				"user/customer/by/id_customer_status/2", null);
		customersListView.getItems().clear();
		customersListView.getItems().clear();
		for (User u : approvedCustomersArrayList) {
			customersListView.getItems().add(u.getIdUser() + " - " + u.getUsername());
		}
		for (User u : frozenCustomersArrayList) {
			customersListView.getItems().add(u.getIdUser() + " - " + u.getUsername());
		}
	}

	/**
	 * Method to initialize the workers list view.
	 */
	@SuppressWarnings("unchecked")
	private void initWorkersListView() {
		workersArrayList = (ArrayList<User>) MainController.getMyClient().send(MessageType.GET,
				"user/by/id_user_status/3", null);
		storeWorkersListView.getItems().clear();
		for (User u : workersArrayList) {
			storeWorkersListView.getItems().add(u.getIdUser() + " - " + u.getUsername());
		}
	}

	/**
	 * Method to initialize the surveyID choice box
	 */
	@SuppressWarnings("unchecked")
	private void initSurveyIDChoiceBox() {
		surveyIDsArrayList = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "survey/id/all",
				null);
		surveyIDChoiceBox.getItems().addAll(surveyIDsArrayList);
	}

	/**
	 * Method to initialize the customer type choice box
	 */
	private void initCustomerTypeChoiceBox() {
		customerTypeChoiceBox.getItems().add(CustomerStatus.UNAPPROVED.toString());
		customerTypeChoiceBox.getItems().add(CustomerStatus.FROZEN.toString());
		customerTypeChoiceBox.getItems().add(CustomerStatus.APPROVED.toString());
	}

	/**
	 * initializing action on new user list view
	 */
	private void setActionOnUnapprovedListView() {
		newUsersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				newUserSelectedAction();
			}
		});
	}

	/**
	 * initializing action on customer list view
	 */
	private void setActionOnCustomerListView() {
		customersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				customerSelectedAction();
			}
		});
	}

	/**
	 * initializing action on worker list view
	 */
	private void setActionOnWorkersListView() {
		storeWorkersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				workerSelectedAction();
			}
		});
	}

	/**
	 * Method to show user details
	 */
	private void setUserDetailsInText() {
		this.userIDText.setText(selectedUser.getIdUser() + "");
		this.userNameText.setText(selectedUser.getUsername());
		// this.fullNameText.setText(selectedUser.getFirstName()+"
		// "+selectedUser.getLastName());
		// this.emailText.setText(selectedUser.getEmail());
		// this.phoneText.setText(selectedUser.getPhoneNumber());
	}

	/**
	 * Method to limit the text length in the text fields
	 */
	public void addTextLimiter() {
		newUserCardTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (newUserCardTextField.getText().length() > 16) {
					String s = newUserCardTextField.getText().substring(0, 16);
					newUserCardTextField.setText(s);
				}
			}
		});
		cardNumberTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (cardNumberTextField.getText().length() > 16) {
					String s = cardNumberTextField.getText().substring(0, 16);
					cardNumberTextField.setText(s);
				}
			}
		});
	}

}