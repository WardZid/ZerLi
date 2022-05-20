package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ClientConsoleController implements Initializable {

	@FXML
    private Label consoleLbl;

    @FXML
    private ImageView logoIV;

    @FXML
    private StackPane mainSP;

    @FXML
    private Text nameText;

    @FXML
    private Label pathLbl;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}


    @FXML
    void onLogOutPressed() {

    }
}
