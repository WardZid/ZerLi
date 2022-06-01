package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class CatalogManagerItemController implements Initializable {

	private Item item;
	public void setItem(Item item) {
		this.item=item;
		nameText.setText(item.getName());
		idText.setText("ID: "+item.getIdItem());
		priceText.setText("$"+item.getPrice());
		saleText.setText(item.getSale()+"%");
		typeText.setText(CatalogManagerController.getCategoryType().get(item.getCategory()));
		categoryText.setText(item.getCategory());
		colorText.setText(item.getColor());
		descrptionTA.setText(item.getDescription());
	}
	
    @FXML
    private Text categoryText;

    @FXML
    private Text colorText;

    @FXML
    private TextArea descrptionTA;

    @FXML
    private Text idText;

    @FXML
    private ImageView itemIV;

    @FXML
    private Text nameText;

    @FXML
    private Text priceText;

    @FXML
    private Text saleText;

    @FXML
    private Text typeText;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

    @FXML
    void onDeletePressed() {

    }

    @FXML
    void onEditPressed() {

    }
}
