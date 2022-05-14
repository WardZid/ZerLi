package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LogInViewController implements Initializable {


    @FXML
    private ImageView logInBackgroundIV;

    @FXML
    private Button logInBtn;

    @FXML
    private PasswordField passwordPF;

    @FXML
    private TextField usernameTF;

    @FXML
    private ImageView welcomeIV;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logInBackgroundIV.setImage(new Image("boundary/media/flowers-login.jpg"));
	}

}
