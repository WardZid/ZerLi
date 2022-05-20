package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.Customer;
import entity.MyMessage.MessageType;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    private VBox navVB;

    @FXML
    private Label pathLbl;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println(user.toString());
		logoIV.setImage(new Image("boundary/media/zli-logo.png"));
		System.out.println("222");
		if (user != null) {
			consoleLbl.setText(user.getUserType().toString().replace('_', ' ') + " CONSOLE");
			nameText.setText(user.getUsername());
		}
		System.out.println("333");
		
		switch (user.getUserType()) {
		case CUSTOMER:
			loadCustomerConsole();
			break;
		case BRANCH_MANAGER:

			break;
		case CEO:

			break;
		case STORE_WORKER:

			break;
		case DELIVERY_WORKER:

			break;
		case CUSTOMER_SUPPORT:

			break;
		case SUPPORT_SPECIALIST:

			break;
		case CATALOG_MANAGER:

			break;

		default:
			MainController.printErr(getClass(), "no such user type");
			break;
		}
	}
	@FXML
    void onLogOutPressed(MouseEvent event) {
		user = null;
		customer = null;
		ClientView.setUpLogIn();
    }

	
	private void loadCustomerConsole() {
		MainController.getMyClient().send(MessageType.GET, "/login/customer", user);
		try {
			Node node =FXMLLoader.load(ClientView.class.getResource("fxmls/catalog-view.fxml"));
			mainSP.getChildren().add(node);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
