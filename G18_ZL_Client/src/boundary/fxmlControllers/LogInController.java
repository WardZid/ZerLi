package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.MyMessage.MessageType;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
		errorLbl.setVisible(false);
		welcomeIV.setImage(new Image("boundary/media/zli-welcome.png"));
		userIconIv.setImage(new Image("boundary/media/user-icon.png"));
		passIconIV.setImage(new Image("boundary/media/pass-icon.png"));

	}

	/**
	 * checks that the fields arent empty. fetches user with appropriate username
	 * AND pass, if non is found then something is incorrect.
	 * Makes sure the user can't log in if he is logged in somewhere else
	 * 
	 */
	public void onLogInPressed() {
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

		if (!(boolean) MainController.getMyClient().send(MessageType.INFO, "log/in", user)) {
			errorLbl.setVisible(true);
			errorLbl.setText("*User is already logged in");
			return;
		}

		ClientConsoleController.setUser(user);
		ClientView.setUpClientConsole();
	}

}
