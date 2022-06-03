package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import boundary.ClientView;
import boundary.fxmlControllers.ClientConsoleController.Navigation;
import control.MainController;
import entity.Item;
import entity.Item.OrderItem;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CatalogController implements Initializable {
	@FXML
	private Slider SliderPrice;
	@FXML
	private Label priceRange;
	@FXML
	private GridPane grid;
	@FXML
	private ComboBox<String> categoryCB;

	@FXML
	private ComboBox<String> typeCB;

	@FXML
	private Label numberItemInOrder;

	@FXML
	private VBox vboxViewItemDescription;
	@FXML
	private Label descriptionLable;

	@FXML
	private Label nameItemLable;
	@FXML
	private Label UserNameLable;

	@FXML
	private VBox catalogvbox;
	@FXML
	private ImageView searchIV;

	@FXML
	private TextField searchTF;

	private int PriceRangeCust;

	private static ArrayList<Item> items=new ArrayList<Item>() ;
	private static ArrayList<Item> itemsGetFromDB;
	private ArrayList<Item> filteredItems = new ArrayList<Item>();

	private static HashMap<String, String> categoryType = new HashMap<String, String>();

	public static HashMap<String, String> getCategoryType() {
		return categoryType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		UserNameLable.setText(ClientConsoleController.getCustomer().getName());
		this.getvboxViewItemDescription().setVisible(false);
		setLabelNumItemInOrderText();

		searchIV.setImage(new Image("boundary/media/search-icon.png"));

		typeCB.getItems().add("All Types");
		categoryCB.getItems().add("All Categories");

		ArrayList<String> types = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "type/all",null);
				

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

		/// slider fix
		SliderPrice.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				PriceRangeCust = (int) SliderPrice.getValue();
				priceRange.setText("(0-" + PriceRangeCust + ")");
				filterItemsAndShow();
			}
		});

		loadAllItems();

	}

	public VBox getvboxViewItemDescription() {
		return vboxViewItemDescription;
	}

	public VBox getcatalogvbox() {
		return catalogvbox;
	}

	public Label getnameItemLable() {
		return nameItemLable;
	}

	public Label getdescriptionLable() {
		return descriptionLable;
	}

	public void onBuildItemPressed() {

		Navigation.navigator("build-ItemsScene-View.fxml");
	}

	public void BackBtnFromDesToCata() {
		getvboxViewItemDescription().setVisible(false);
		getcatalogvbox().setDisable(false);
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

	@SuppressWarnings("unchecked")

	private void loadAllItems() {
		double maxprice = 0;
		items.clear();
		itemsGetFromDB = (ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);
		for (Item item : itemsGetFromDB)
		{
			if(item.getSale()!=0)
				items.add(0,item);
			else
				items.add(item);
		}
		// active sale
		for (Item item : items) {
			item.setPrice(item.getPriceAfterSale());
		}
		filteredItems.addAll(items);
		for (Item item : items) {
			if (item.getPrice() > maxprice)
				maxprice = item.getPrice();
		}
		SliderPrice.setMax(maxprice);
		SliderPrice.setValue(maxprice);
		System.out.println("maxprice=" + maxprice);

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
					if (category.equals("All Categories") || category.equals(item.getCategory())) {
						if (item.getPrice() <= (double) PriceRangeCust)

							filteredItems.add(item);
					}
				}
			}
		}
		System.out.println("PriceRangeCust=" + PriceRangeCust);
		showFilteredItems();
	}

	private void showFilteredItems() {
		int column = 0;
		int row = 1;
		grid.getChildren().clear();
		for (Item item : filteredItems) {
			try {

				FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/catalog-item-view.fxml"));
				Node node = fXMLLoader.load();

				CatalogItemController catalogItemController = fXMLLoader.getController();
				catalogItemController.setData(item, this);

				if (column == 4) {
					column = 0;
					row++;
				}

				grid.add(node, column++, row);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	void setLabelNumItemInOrderText() {

		numberItemInOrder.setText(CartController.getOrderInProcess().getItemInOrder() + "");
	}

}
