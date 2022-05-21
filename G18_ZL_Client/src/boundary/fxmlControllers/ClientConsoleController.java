package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
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
	
	//Current page
	private Node currentNode;
	private static Button pressedBtn;

	// MENU BUTTONS**************

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
	
	//Customer support
	
	private Button csComplaintsBtn;
	private Button csReportsBtn;

	
	@FXML
	void onLogOutPressed(MouseEvent event) {
		user = null;
		customer = null;
		ClientView.setUpLogIn();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		logoIV.setImage(new Image("boundary/media/zli-logo.png"));
		menuVB.getStylesheets().add("boundary/fxmlControllers/menu.css");
		if (user == null)
			return;
		
		consoleLbl.setText(user.getUserType().toString().replace('_', ' ') + " CONSOLE");
		nameText.setText(user.getUsername());

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
			MainController.printErr(getClass(), "no such user type");
			break;
		}
	}

	private Button menuButton(String btnName,String fxml) {
		Button btn=new Button(btnName);
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setOnAction((ActionEvent e) -> {
			if(pressedBtn.equals(btn))
				return;
			pressedBtn.setStyle("-fx-background-color: #e5e5e5");
			pressedBtn=btn;
			btn.setStyle("-fx-background-color: #3AAED8");
			try {
				currentNode = FXMLLoader.load(ClientView.class.getResource("fxmls/"+fxml));
				mainSP.getChildren().clear();
				mainSP.getChildren().add(currentNode);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		menuVB.getChildren().add(btn);
		return btn;
	}
	
	private void loadCustomerConsole() {
		customer = (Customer) MainController.getMyClient().send(MessageType.GET, "login/customer", user);
		
		
		cusCatalogBtn=menuButton("Catalog", "catalog-view.fxml");
		cusCartBtn=menuButton("Cart", "cart-view.fxml");   
		cusOrdersBtn=menuButton("Orders", "customer-orders-view.fxml");  
		cusComplaintsBtn=menuButton("Complaints", "customer-complaints-view.fxml");
		
		cusCatalogBtn.setStyle("-fx-background-color: #3AAED8");
		pressedBtn=cusCatalogBtn;
		
		try {
			currentNode = FXMLLoader.load(ClientView.class.getResource("fxmls/catalog-view.fxml"));
			mainSP.getChildren().clear();
			mainSP.getChildren().add(currentNode);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void loadBranchManagerConsole() {
		bmOrdersBtn=menuButton("Orders", "branch-manager-orders-view.fxml"); 
		bmCustomersBtn=menuButton("Customers", "branch-manager-customers-view.fxml");
		bmIReportsBtn=menuButton("Income Reports", "branch-manager-income-reports-view.fxml");
		bmOReportsBtn=menuButton("Order Reports", "branch-manager-order-reports-view.fxml");
		
		bmOrdersBtn.setStyle("-fx-background-color: #3AAED8");
		pressedBtn=bmOrdersBtn;
		try {
			currentNode = FXMLLoader.load(ClientView.class.getResource("fxmls/branch-manager-orders-view.fxml"));
			mainSP.getChildren().clear();
			mainSP.getChildren().add(currentNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadCEOConsole() {
		
	}
	
	private void loadStoreWorkerConsole() {
		
	}
	
	private void loadDeliveryWorkerConsole() {
		
	}
	
	private void loadCustomerSupportConsole() {
		csComplaintsBtn=menuButton("Complaints","customer-support-view.fxml");
		//csReportsBtn=new Button("Reports");
		
		csComplaintsBtn.setStyle("-fx-background-color: #3AAED8");
		pressedBtn=csComplaintsBtn;
		
		try {
			currentNode = FXMLLoader.load(ClientView.class.getResource("fxmls/customer-support-view.fxml"));
			mainSP.getChildren().clear();
			mainSP.getChildren().add(currentNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSupportSpecialistConsole() {
		
	}
	
	private void loadCatalogManagerConsole() {
		
	}
	
}
