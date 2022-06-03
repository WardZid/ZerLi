package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class CatalogManagerItemController implements Initializable {
	
	private CatalogManagerController cmc;
	
	
	private Item item;

	public void setCtrlAndItem(CatalogManagerController controller,Item item) {
		cmc = controller;
		
		this.item = item;
		nameText.setText("Name: "+item.getName());
		idText.setText("ID: " + item.getIdItem());
		priceText.setText("$" + item.getPrice());
		saleText.setText("Sale "+item.getSale() + "%");
		typeText.setText(CatalogManagerController.getCategoryType().get(item.getCategory()));
		categoryText.setText(item.getCategory());
		colorText.setText("Color: "+item.getColor());
		descrptionTA.setText(item.getDescription());
		
		if(item.getStatus().equals("SHOWN")) {
			showBtn.setDisable(true);
			hideBtn.setDisable(false);
		} else {
			showBtn.setDisable(false);
			hideBtn.setDisable(true);
		}
			
		
		if (item.getImage() != null)
			itemIV.setImage(item.getImage());
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
	
    @FXML
    private Button showBtn;
    @FXML
    private Button hideBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		hideBtn.setDisable(true);
		showBtn.setDisable(true);
	}

	@FXML
	void onEditPressed() {
		cmc.openOverlayEdit(item);
	}
    @SuppressWarnings("unchecked")
	@FXML
    void onHide() {
    	item.setStatus("HIDDEN");
    	ArrayList<Item> updated=(ArrayList<Item>) MainController.getMyClient().send(MessageType.UPDATE,"item/status",item);
    	if(updated.get(0).getStatus().equals("HIDDEN")) {
    		hideBtn.setDisable(true);
    		showBtn.setDisable(false);
    	}
    }

    @SuppressWarnings("unchecked")
	@FXML
    void onShow() {
    	item.setStatus("SHOWN");
    	ArrayList<Item> updated=(ArrayList<Item>) MainController.getMyClient().send(MessageType.UPDATE,"item/status",item);
    	if(updated.get(0).getStatus().equals("SHOWN")) {
    		hideBtn.setDisable(false);
    		showBtn.setDisable(true);
    	}
    }
}
