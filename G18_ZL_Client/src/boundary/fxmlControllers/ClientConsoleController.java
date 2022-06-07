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
import entity.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Main client page that switches all the user interface panels inside of it
 *
 */
public class ClientConsoleController implements Initializable {

	/**
	 * the logged in person's user account
	 */
	private static User user;
	/**
	 * customer account if user is customer
	 */
	private static Customer customer;
	/**
	 * worker account if user is worker
	 */
	private static Worker worker;

	/**
	 * @return the user
	 */
	public static User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public static void setUser(User user) {
		ClientConsoleController.user = user;
	}

	/**
	 * @return the customer
	 */
	public static Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public static void setCustomer(Customer customer) {
		ClientConsoleController.customer = customer;
	}

	/**
	 * @return the worker
	 */
	public static Worker getWorker() {
		return worker;
	}

	/**
	 * @param worker the worker to set
	 */
	public static void setWorker(Worker worker) {
		ClientConsoleController.worker = worker;
	}

	@FXML
	private Label consoleLbl;

    @FXML
    private Label storeLbl;

	@FXML
	private ImageView logoIV;

	@FXML
	private StackPane mainSP;

	@FXML
	private Text nameText;

	@FXML
	private VBox menuVB;

	// Current page
	/**
	 * Node in the main panel that contains a user's main-purpose functionality purpose base pages
	 */
	private Node currentNode;
	/**
	 * the side panel menu button that is currently pressed
	 */
	private static Button pressedBtn;

	
	@FXML
	/**
	 * action listener to when a button is pressed, user is logge out and the scene is swithced back to the log in scene
	 * @param event
	 */
	void onLogOutPressed() {
		MainController.getMyClient().send(MessageType.INFO,"log/out",user);
		user = null;
		customer = null;
		ClientView.setUpLogIn();
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * init main fxmls and loads appropriate user-type oriented consoles
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {

		Navigation.setClientConsoleController(this);
		logoIV.setImage(new Image("boundary/media/zli-logo.png"));
		menuVB.getStylesheets().add("boundary/fxmlControllers/menu.css");
		if (user == null)
			return;

		consoleLbl.setText(user.getUserType().toString().replace('_', ' ') + " CONSOLE");
		nameText.setText(user.getName());
		
		if(user.getUserType().isWorker()) {
			worker=((ArrayList<Worker>)MainController.getMyClient().send(MessageType.GET, "worker/by/id_worker/"+user.getIdWorker(), null)).get(0);
			storeLbl.setText("Store: "+Store.getById(worker.getIdStore()));
			storeLbl.setVisible(true);
		}

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
		case SURVEY_WORKER:
			loadSurveyWorkerConsole();
			break;
		default:
			ClientView.printErr(getClass(), "no such user type");
			break;
		}
	}

	/**
	 * creates a menu button and adds it the meny and gives it a listener to go to the fxml given
	 * @param btnName name of menu button
	 * @param fxml target of action on the button is to open the button
	 * @return the built button
	 */
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

	/**
	 * sets a button to look pressed and opens page as first
	 * @param firstBtn btn to be pressed
	 * @param fxmlPath	fxml path to be opened as first page
	 */
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
			e.printStackTrace();
		}
	}

	/**
	 * loads appropriate customer buttons with the corresponding fxmls
	 */
	private void loadCustomerConsole() {

		Button cusCatalogBtn = menuButton("Catalog", "catalog-view.fxml");
		menuButton("Cart", "cart-view.fxml");
		menuButton("Orders", "customer-orders-view.fxml");
		menuButton("Complaints", "customer-complaints-view.fxml");

		initPage(cusCatalogBtn, "fxmls/catalog-view.fxml");

	}
	
	/**
	 * loads appropriate branch manager buttons with the corresponding fxmls
	 */
	private void loadBranchManagerConsole() {
		Button bmOrdersBtn = menuButton("Orders", "branch-manager-orders-view.fxml");
		menuButton("Customer Management", "branch-manager-customers-view.fxml");
		menuButton("Worker Management", "branch-manager-workers-view.fxml");
		menuButton("Income Reports", "branch-manager-income-reports-view.fxml");
		menuButton("Order Reports", "branch-manager-order-reports-view.fxml");
	
		initPage(bmOrdersBtn, "fxmls/branch-manager-orders-view.fxml");
	}

	/**
	 * loads appropriate ceo buttons with the corresponding fxmls
	 */
	private void loadCEOConsole() {

		Button ceoIReportsBtn=menuButton("Income Reports", "branch-manager-income-reports-view.fxml");
		menuButton("Order Reports", "branch-manager-order-reports-view.fxml");
		menuButton("Complaint Reports", "ceo-complaint-reports-view.fxml");
		menuButton("Quarter Reports", "ceo-quarter-income-report-view.fxml");
		
		initPage(ceoIReportsBtn, "fxmls/branch-manager-income-reports-view.fxml");
	 
	}

	/**
	 * a normal store worker has no pages, they will see nothing except for what branch they work at
	 */
	private void loadStoreWorkerConsole() {
	}

	/**
	 * loads appropriate delivery worker fxml
	 */
	private void loadDeliveryWorkerConsole() {
		initPage(null, "fxmls/delivery-view.fxml");
	}

	/**
	 * loads appropriate customer support fxml
	 */
	private void loadCustomerSupportConsole() {
		initPage(null, "fxmls/customer-support-view.fxml");
	}

	/**
	 * loads appropriate support specialist support fxml
	 */
	private void loadSupportSpecialistConsole() {
		initPage(null, "fxmls/expert-survey-view.fxml");
	}

	/**
	 * loads appropriate catalog manager fxml
	 */
	private void loadCatalogManagerConsole() {
		initPage(null, "fxmls/catalog-manager-view.fxml");
	}
	
	/**
	 * loads appropriate customer support fxml
	 */
	private void loadSurveyWorkerConsole(){
		initPage(null, "fxmls/store-worker-survey-view.fxml");
	}
	
	/**
	 * class for easily navigating panes
	 *
	 */
	public static class Navigation {

		/**
		 * client console in which to switch
		 */
		private static ClientConsoleController clientConsoleController;
		/**
		 * main node panel to add to ccc
		 */
		private static Node currentNode;

		/**
		 * gets the ClientConsoleController
		 */
		public static ClientConsoleController getClientConsoleController() {
			return clientConsoleController;
		}

		/**
		 * sets the ClientConsoleController
		 * @param clientConsoleController1
		 */
		public static void setClientConsoleController(ClientConsoleController clientConsoleController1) {
			clientConsoleController = clientConsoleController1;

		}

		/**
		 * the navigation method that takes an fxml and navigates to it
		 * @param fxml
		 */
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
