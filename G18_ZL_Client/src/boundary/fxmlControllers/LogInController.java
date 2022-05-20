package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.ClientController;
import control.MainController;
import entity.MyMessage.MessageType;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
		welcomeIV.setImage(new Image("boundary/media/zli-welcome.png"));
		userIconIv.setImage(new Image("boundary/media/user-icon.png"));
		passIconIV.setImage(new Image("boundary/media/pass-icon.png"));

	}
	
	public void onLogInPressed() {
		if(usernameTF.getText().isEmpty() || passwordPF.getText().isEmpty()) {
			errorLbl.setText("*Please fill the missing fields");
			return;
		}
		MainController.getMyClient().send(MessageType.GET, "/user", new User(usernameTF.getText(),passwordPF.getText()));
		
		if(ClientConsoleController.getUser()==null) {
			errorLbl.setText("*Incorrect user or pass.");
			return;
		}
		
		ClientView.setUpClientConsole();
	}

}
