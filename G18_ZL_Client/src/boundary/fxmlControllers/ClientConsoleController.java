package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.Customer;
import entity.Store;
import entity.MyMessage.MessageType;
import entity.User;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ClientConsoleController implements Initializable {

	private static User user;
	private static Customer customer;

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		ClientConsoleController.user = user;
	}

	public static Customer getCustomer() {
		return customer;
	}

	public static void setCustomer(Customer customer) {
		ClientConsoleController.customer = customer;
	}

	@FXML
	private Label consoleLbl;

	@FXML
	private ImageView logoIV;

	@FXML
	private StackPane mainSP;

	@FXML
	private Text nameText;

	@FXML
	private VBox menuVB;

	// Current page
	private Node currentNode;
	private static Button pressedBtn;

	// user specific FXML nodes

	// Customer menu
	private Button cusCatalogBtn;
	private Button cusCartBtn;
	private Button cusOrdersBtn;
	private Button cusComplaintsBtn;

	// BM MENU

	private Button bmCustomersBtn;
	private Button bmIReportsBtn;
	private Button bmOReportsBtn;
	private Button bmOrdersBtn;

	// CEO MENU

	private Button ceoIReportsBtn;
	private Button ceoOReportsBtn;
	private Button ceoCReportsBtn;
	// Store worker

	// delivery worker

	// Customer support

	// support specialist

	// catalog manager

	@FXML
	void onLogOutPressed(MouseEvent event) {
		MainController.getMyClient().send(MessageType.INFO,"log/out",user);
		user = null;
		customer = null;
		ClientView.setUpLogIn();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		Navigation.setClientConsoleController(this);
		logoIV.setImage(new Image("boundary/media/zli-logo.png"));
		menuVB.getStylesheets().add("boundary/fxmlControllers/menu.css");
		if (user == null)
			return;

		consoleLbl.setText(user.getUserType().toString().replace('_', ' ') + " CONSOLE");
		nameText.setText(user.getName());

		switch (user.getUserType()) {
		case CUSTOMER:
			loadCustomerConsole();
			break;
		case BRANCH_MANAGER:
			loadBranchManagerConsole();
			break;
		case CEO:
			loadCEOConsole();
			break;
		case STORE_WORKER:
			loadStoreWorkerConsole();
			break;
		case DELIVERY_WORKER:
			loadDeliveryWorkerConsole();
			break;
		case CUSTOMER_SUPPORT:
			loadCustomerSupportConsole();
			break;
		case SUPPORT_SPECIALIST:
			loadSupportSpecialistConsole();
			break;
		case CATALOG_MANAGER:
			loadCatalogManagerConsole();
			break;
		default:
			ClientView.printErr(getClass(), "no such user type");
			break;
		}
	}

	private Button menuButton(String btnName, String fxml) {
		Button btn = new Button(btnName);
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setPrefHeight(50);
		btn.setOnAction((ActionEvent e) -> {
			if (pressedBtn.equals(btn))
				return;
			pressedBtn.setStyle("-fx-background-color: transparent");
			pressedBtn = btn;
			btn.setStyle("-fx-background-color: #BBBBBB");
			try {
				currentNode = FXMLLoader.load(ClientView.class.getResource("fxmls/" + fxml));
				mainSP.getChildren().clear();
				mainSP.getChildren().add(currentNode);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		menuVB.getChildren().add(btn);
		return btn;
	}

	private void initPage(Button firstBtn, String fxmlPath) {
		if (firstBtn != null) {
			firstBtn.setStyle("-fx-background-color: #BBBBBB");
			pressedBtn = firstBtn;
		}

		try {
			currentNode = FXMLLoader.load(ClientView.class.getResource(fxmlPath));
			mainSP.getChildren().clear();
			mainSP.getChildren().add(currentNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadCustomerConsole() {
		ArrayList<Customer> c = (ArrayList<Customer>) MainController.getMyClient().send(MessageType.GET,
				"login/customer", user);
		customer = c.get(0);

		cusCatalogBtn = menuButton("Catalog", "catalog-view.fxml");
		cusCartBtn = menuButton("Cart", "cart-view.fxml");
		cusOrdersBtn = menuButton("Orders", "customer-orders-view.fxml");
		cusComplaintsBtn = menuButton("Complaints", "customer-complaints-view.fxml");

		initPage(cusCatalogBtn, "fxmls/catalog-view.fxml");

	}

	private void loadBranchManagerConsole() {
		bmOrdersBtn = menuButton("Orders", "branch-manager-orders-view.fxml");
		bmCustomersBtn = menuButton("Customers", "branch-manager-accounts-view.fxml");
		bmIReportsBtn = menuButton("Income Reports", "branch-manager-income-reports-view.fxml");
		bmOReportsBtn = menuButton("Order Reports", "branch-manager-order-reports-view.fxml");
	
		initPage(bmOrdersBtn, "fxmls/branch-manager-orders-view.fxml");
	}

	private void loadCEOConsole() {

		ceoIReportsBtn=menuButton("Income Reports", "branch-manager-income-reports-view.fxml");
		ceoOReportsBtn=menuButton("Order Reports", "branch-manager-order-reports-view.fxml");
		ceoCReportsBtn= menuButton("Complaint Reports", "ceo-complaint-reports-view.fxml");
		ceoCReportsBtn= menuButton("Quarter Reports", "ceo-quarter-income-report-view.fxml");
		
		initPage(ceoIReportsBtn, "fxmls/branch-manager-income-reports-view.fxml");
	 
	}

	private void loadStoreWorkerConsole() {
		initPage(null, "fxmls/store-worker-survey-view.fxml");
	}

	private void loadDeliveryWorkerConsole() {
		initPage(null, "fxmls/delivery-view.fxml");
	}

	private void loadCustomerSupportConsole() {
		initPage(null, "fxmls/customer-support-view.fxml");
	}

	private void loadSupportSpecialistConsole() {
		initPage(null,  "fxmls/expert-survey-view.fxml");
	}

	private void loadCatalogManagerConsole() {
		initPage(null, "fxmls/catalog-manager-view.fxml");
	}

	public static class Navigation {

		private static ClientConsoleController clientConsoleController;
		private static Node currentNode;

		public static ClientConsoleController getClientConsoleController() {
			return clientConsoleController;
		}

		public static void setClientConsoleController(ClientConsoleController clientConsoleController1) {
			clientConsoleController = clientConsoleController1;

		}

		public static void navigator(String fxml) {

			try {
				currentNode = FXMLLoader.load(ClientView.class.getResource("fxmls/" + fxml));
				if (clientConsoleController == null)
					System.out.println("clientConsoleController is null");

				clientConsoleController.mainSP.getChildren().clear();
				clientConsoleController.mainSP.getChildren().add(currentNode);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}
}
