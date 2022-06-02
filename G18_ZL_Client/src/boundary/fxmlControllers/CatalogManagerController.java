package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CatalogManagerController implements Initializable {

	private ArrayList<Item> items;
	private ArrayList<Item> filteredItems = new ArrayList<Item>();

	private static HashMap<String, String> categoryType = new HashMap<String, String>();

	public static HashMap<String, String> getCategoryType() {
		return categoryType;
	}

	private Node overlay;
	// FXML Components
	@FXML
	private StackPane mySP;

	@FXML
	private VBox mainVBox;

	@FXML
	private ComboBox<String> categoryCB;

	@FXML
	private VBox itemsVBox;

	@FXML
	private ImageView searchIV;

	@FXML
	private TextField searchTF;

	@FXML
	private ComboBox<String> typeCB;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchIV.setImage(new Image("boundary/media/search-icon.png"));

		typeCB.getItems().add("All Types");
		categoryCB.getItems().add("All Categories");

		ArrayList<String> types = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "type/all",
				null);

		for (String type : types) {
			typeCB.getItems().add(type);

			ArrayList<String> categories = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET,
					"category/by/type/" + type, null);
			for (String category : categories) {
				categoryType.put(category, type);
			}
		}

		typeCB.getSelectionModel().selectFirst();
		categoryCB.getSelectionModel().selectFirst();

		typeCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				if (newValue.equals("All Types")) {
					categoryCB.getSelectionModel().selectFirst();
					categoryCB.setDisable(true);
				} else {

					categoryCB.getItems().clear();
					categoryCB.getItems().add("All Categories");
					categoryCB.getSelectionModel().selectFirst();
					for (String category : categoryType.keySet()) {
						if (categoryType.get(category).equals(newValue))
							categoryCB.getItems().add(category);
					}
					categoryCB.setDisable(false);
				}
				filterItemsAndShow();
			}
		});

		categoryCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				filterItemsAndShow();
			}
		});

		loadAllItems();
	}

	@FXML
	void onAddNewItemPressed() {
		openOverlayAdd();
	}

	@FXML
	void onSearchEnter(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			filterItemsAndShow();
	}

	@FXML
	void onSearchPressed() {
		filterItemsAndShow();
	}

	// NON FXML METHODS

	@SuppressWarnings("unchecked")
	private void loadAllItems() {
		items = (ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);

		filteredItems.clear();
		filteredItems.addAll(items);
		
		showFilteredItems();
	}

	private void filterItemsAndShow() {
		String search = searchTF.getText();
		String type = typeCB.getSelectionModel().getSelectedItem();
		String category = categoryCB.getSelectionModel().getSelectedItem();

		filteredItems.clear();

		for (Item item : items) {
			if (search == null || search.equals("") || item.getName().contains(search)) {
				if (type.equals("All Types") || type.equals(categoryType.get(item.getCategory()))) {
					if (category.equals("All Categories") || category.equals(item.getCategory()))
						filteredItems.add(item);
				}
			}
		}

		showFilteredItems();
	}

	private void showFilteredItems() {
		itemsVBox.getChildren().clear();
		for (Item item : filteredItems) {
			try {
				FXMLLoader loader = new FXMLLoader(
						ClientView.class.getResource("fxmls/catalog-manager-item-view.fxml"));
				Node node = loader.load();
				CatalogManagerItemController cmiCtrl = loader.getController();
				cmiCtrl.setItem(item);
				itemsVBox.getChildren().add(node);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void openOverlayAdd() {
		try {
			mainVBox.setDisable(true);
			FXMLLoader loader = new FXMLLoader(ClientView.class.getResource("fxmls/catalog-manager-add-view.fxml"));
			overlay = loader.load();
			CatalogManagerAddController cmaCtrl = loader.getController();
			cmaCtrl.setCatManCtrl(this);
			mySP.getChildren().add(overlay);
		} catch (Exception e) {
			ClientView.printErr(getClass(), e.getMessage());
		}

	}

	public void closeOverlayAdd() {
		mySP.getChildren().remove(overlay);
		overlay=null;
		mainVBox.setDisable(false);
		loadAllItems();
	}

}
