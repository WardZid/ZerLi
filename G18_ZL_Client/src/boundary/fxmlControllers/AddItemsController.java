package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


import javafx.fxml.Initializable;

public class AddItemsController implements Initializable {
	  @FXML
	    private Button addNewItemButton;

	    @FXML
	    private Label amountL;

	    @FXML
	    private TextField amountT;

	    @FXML
	    private Label categoryL;

	    @FXML
	    private TextField categoryT;

	    @FXML
	    private TextField colorT;

	    @FXML
	    private Label colorL;

	    @FXML
	    private TextArea descriptionT;

	    @FXML
	    private Label descriptionL;

	    @FXML
	    private VBox hb;

	    @FXML
	    private ImageView image;

	    @FXML
	    private Label imageL;

	    @FXML
	    private Label itemIdL;

	    @FXML
	    private ListView<Integer> itemsL;

	    @FXML
	    private Label nameL;

	    @FXML
	    private TextField nameT;
	    @FXML
	    private TextField itemIdT;

	    @FXML
	    private Label priceL;

	    @FXML
	    private TextField priceT;

	    @FXML
	    private Label saleL;

	    @FXML
	    private TextField saleT;
	    private HashMap<Integer,Item> itemsHashMap = new HashMap<>();
	    private Item selectedItem;
	    private int selectedItemId;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ArrayList<Item> itemsList = (ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);
		itemIdT.setEditable(false);
		categoryT.setEditable(false);
		nameT.setEditable(false);
		priceT.setEditable(false);
		saleT.setEditable(false);
		colorT.setEditable(false);
		descriptionT.setEditable(false);
		amountT.setEditable(false);
		for(int i=0 ; i<itemsList.size() ; i++)
			itemsHashMap.put(itemsList.get(i).getIdItem(),itemsList.get(i));
		itemsL.getItems().addAll(itemsHashMap.keySet());
		itemsL.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>(){

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				selectedItemId = itemsL.getSelectionModel().getSelectedItem();
				selectedItem = itemsHashMap.get(selectedItemId);
				image.setImage(selectedItem.getImage());
				itemIdT.setText(Integer.toString(selectedItem.getIdItem()));
				categoryT.setText(Integer.toString(selectedItem.getIdCategory()));
				nameT.setText(selectedItem.getName());
				priceT.setText(Double.toString(selectedItem.getPrice()));
				saleT.setText(Integer.toString(selectedItem.getSale()));
				colorT.setText(selectedItem.getColor());
				descriptionT.setText(selectedItem.getDescription());
			//	amountT.setText(Integer.toString(selectedItem.ge));
			}
			
		});
	}

}
