package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.MyMessage.MessageType;
import entity.User;
import entity.User.UserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LogInController implements Initializable {

	@FXML
	private ImageView logInBackgroundIV;

	@FXML
	private ImageView welcomeIV;

	@FXML
	private ImageView userIconIv;

	@FXML
	private ImageView passIconIV;

	@FXML
	private PasswordField passwordPF;

	@FXML
	private TextField usernameTF;

	@FXML
	private Label errorLbl;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CartController.NewOrder();
		errorLbl.setVisible(false);
		logInBackgroundIV.setImage(new Image("boundary/media/login-bg.jpg"));
		welcomeIV.setImage(new Image("boundary/media/zli-welcome.png"));
		userIconIv.setImage(new Image("boundary/media/user-icon.png"));
		passIconIV.setImage(new Image("boundary/media/pass-icon.png"));

	}
	
    @FXML
    void onLogInKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER)
    		logIn();
    }

	
	public void onLogInPressed() {
		logIn();
	}
	
	/**
	 * checks that the fields arent empty. fetches user with appropriate username
	 * AND pass, if non is found then something is incorrect.
	 * Makes sure the user can't log in if he is logged in somewhere else
	 * 
	 */
	private void logIn() {
		if (usernameTF.getText().isEmpty() || passwordPF.getText().isEmpty()) {
			errorLbl.setVisible(true);
			errorLbl.setText("*Please fill the missing fields");
			return;
		}

		User user = (User) MainController.getMyClient().send(MessageType.GET, "login/user",
				new User(usernameTF.getText(), passwordPF.getText()));

		if (user == null) {
			errorLbl.setVisible(true);
			errorLbl.setText("*Incorrect user or password");
			return;
		}

		if(user.getUserType()==UserType.CUSTOMER && user.getIdCustomer()<=0) {
			errorLbl.setVisible(true);
			errorLbl.setText("*Your account hasn't been confirmed");
			return;
		}
			
		if (!(boolean) MainController.getMyClient().send(MessageType.INFO, "log/in", user)) {
			errorLbl.setVisible(true);
			errorLbl.setText("*User is already logged in");
			return;
		}

		ClientConsoleController.setUser(user);
		ClientView.setUpClientConsole();
	}

}
