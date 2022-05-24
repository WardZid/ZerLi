package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class CustomerSurveyController implements Initializable {

    @FXML
    private RadioButton RB101;

    @FXML
    private RadioButton RB102;

    @FXML
    private RadioButton RB103;

    @FXML
    private RadioButton RB104;

    @FXML
    private RadioButton RB105;

    @FXML
    private RadioButton RB106;

    @FXML
    private RadioButton RB11;

    @FXML
    private RadioButton RB12;

    @FXML
    private RadioButton RB13;

    @FXML
    private RadioButton RB14;

    @FXML
    private RadioButton RB15;

    @FXML
    private RadioButton RB16;

    @FXML
    private RadioButton RB21;

    @FXML
    private RadioButton RB22;

    @FXML
    private RadioButton RB23;

    @FXML
    private RadioButton RB24;

    @FXML
    private RadioButton RB25;

    @FXML
    private RadioButton RB26;

    @FXML
    private RadioButton RB31;

    @FXML
    private RadioButton RB32;

    @FXML
    private RadioButton RB33;

    @FXML
    private RadioButton RB34;

    @FXML
    private RadioButton RB35;

    @FXML
    private RadioButton RB36;

    @FXML
    private RadioButton RB41;

    @FXML
    private RadioButton RB42;

    @FXML
    private RadioButton RB43;

    @FXML
    private RadioButton RB44;

    @FXML
    private RadioButton RB45;

    @FXML
    private RadioButton RB46;

    @FXML
    private RadioButton RB51;

    @FXML
    private RadioButton RB52;

    @FXML
    private RadioButton RB53;

    @FXML
    private RadioButton RB54;

    @FXML
    private RadioButton RB55;

    @FXML
    private RadioButton RB56;

    @FXML
    private RadioButton RB61;

    @FXML
    private RadioButton RB62;

    @FXML
    private RadioButton RB63;

    @FXML
    private RadioButton RB64;

    @FXML
    private RadioButton RB65;

    @FXML
    private RadioButton RB66;

    @FXML
    private RadioButton RB71;

    @FXML
    private RadioButton RB72;

    @FXML
    private RadioButton RB73;

    @FXML
    private RadioButton RB74;

    @FXML
    private RadioButton RB75;

    @FXML
    private RadioButton RB76;

    @FXML
    private RadioButton RB81;

    @FXML
    private RadioButton RB82;

    @FXML
    private RadioButton RB83;

    @FXML
    private RadioButton RB84;

    @FXML
    private RadioButton RB85;

    @FXML
    private RadioButton RB86;

    @FXML
    private RadioButton RB91;

    @FXML
    private RadioButton RB92;

    @FXML
    private RadioButton RB93;

    @FXML
    private RadioButton RB94;

    @FXML
    private RadioButton RB95;

    @FXML
    private RadioButton RB96;

    @FXML
    private ComboBox<?> ShopComB;

    @FXML
    private Label ShopL;

    @FXML
    private Button sendButton;

    @FXML
    private TextField textF1;

    @FXML
    private TextField textF10;

    @FXML
    private TextField textF2;

    @FXML
    private TextField textF3;

    @FXML
    private TextField textF4;

    @FXML
    private TextField textF5;

    @FXML
    private TextField textF6;

    @FXML
    private TextField textF7;

    @FXML
    private TextField textF8;

    @FXML
    private TextField textF9;
    private RadioButton [] radios = new RadioButton[10];
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for(int i=0 ; i<10 ; i++)
		    radios[i] = new RadioButton();
	}
    public void onClickRadioButton1Line(ActionEvent event) {
		System.out.println(1);

    	if(radios[0]==null) {
    		System.out.println(1);
    		radios[0] = ((RadioButton)event.getSource());
    		System.out.println(2);
    	}
    	else {
    		radios[0].setSelected(false);
    		radios[0] =  ((RadioButton)event.getSource());
    	}
    }
}
