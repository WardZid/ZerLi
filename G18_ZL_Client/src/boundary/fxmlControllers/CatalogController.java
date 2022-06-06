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
	/**
	 * to filter the items due to price 
	 */
	@FXML
	private Slider SliderPrice;
	/**
	 * just label to view
	 */
	@FXML
	private Label priceRange;
	/**
	 * to view the items in grid on the catalog 
	 */
	@FXML
	private GridPane grid;
	/**
	 * to insert the category from DB to this combo box 
	 */
	@FXML
	private ComboBox<String> categoryCB;
/**
 * to insert the types from DB to this combo box 
 */
	@FXML
	private ComboBox<String> typeCB;
/**
 * to view the number of the item that he build or selected 
 */
	@FXML
	private Label numberItemInOrder;
	/**
	 *  just a cart image
	 */

    @FXML
    private ImageView cartIV;
/**
 * to view the item with description and photo in the VBox 
 */
	@FXML
	private VBox vboxViewItemDescription;
	/**
	 * to view the description of the item from DB
	 */
	@FXML
	private Label descriptionLable;
/**
 * to view the item name 
 */
	@FXML
	private Label nameItemLable;
	/**
	 * to view the name of the customer 
	 */
	@FXML
	private Label UserNameLable;
/**
 * to view all the catalog details on it and to Arrange all thing on it
 */
	@FXML
	private VBox catalogvbox;
	/**
	 * cart image tom click on it and search
	 */
	@FXML
	private ImageView searchIV;
/**
 * text field to write the item name and to search it in catalog
 */
	@FXML
	private TextField searchTF;
/**
 * to save the number that returns from the slider to filter the items due to this number
 */
	private int PriceRangeCust;
/**
 * to Arrange the items as we want that we get from itemsGetFromDB and use it (Like to show the items that have a sales first)
 */
	private static ArrayList<Item> items=new ArrayList<Item>() ;
	/**
	 * in this ArrayList we get all the items from the DB
	 */
	private static ArrayList<Item> itemsGetFromDB;
	/**
	 * we use this ArrayList to filter the items ArrayList As we want(due to price , name , or category) 
	 * and we saved the the items that we filter on this filteredItems and use it 
	 */
	private ArrayList<Item> filteredItems = new ArrayList<Item>();
/**
 * to save on this hash map the Category as a key and the type of this category like a value
 */
	private static HashMap<String, String> categoryType = new HashMap<String, String>();
/**
 * returns the hash map
 * @return HashMap categoryType
 */
	public static HashMap<String, String> getCategoryType() {
		return categoryType;
	}
/**
 * we set all the labels we wants and set text for all labels we want
 * and we set the image for all the items 
 * and we insert to all the combo box the data from DB (Types , category )
 * we add a listener to the slider and the all combo box to filter the items then we load the items 
 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cartIV.setImage(new Image("boundary/media/cart.png"));
		UserNameLable.setText(ClientConsoleController.getUser().getName());
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
/**
 * 
 * @return VBox vboxViewItemDescription
 */
	public VBox getvboxViewItemDescription() {
		return vboxViewItemDescription;
	}
/**
 * 
 * @return VBox catalogvbox
 */
	public VBox getcatalogvbox() {
		return catalogvbox;
	}
/**
 * 
 * @return Label nameItemLable
 */
	public Label getnameItemLable() {
		return nameItemLable;
	}
/**
 * 
 * @return Label descriptionLable
 */
	public Label getdescriptionLable() {
		return descriptionLable;
	}
/**
 * to move to build item page 
 */
	public void onBuildItemPressed() {

		Navigation.navigator("build-ItemsScene-View.fxml");
	}
/**
 * to return from the Description items VBox to catalog VBox ( vboxViewItemDescription invisible, catalog VBox disable false)
 */
	public void BackBtnFromDesToCata() {
		getvboxViewItemDescription().setVisible(false);
		getcatalogvbox().setDisable(false);
	}
/**
 * parameter we get from the text field to search the item due to his name 
 * @param event :name of the item we put on search text field
 */
	@FXML
	void onSearchEnter(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			filterItemsAndShow();
	}
/*
 * press on cart image to search
 * calling filterItemsAndShow to show item we want due to his name 
 */
	@FXML
	void onSearchPressed() {
		filterItemsAndShow();
	}
/**
 * we Arrange the items to view the items with sale first on catalog then items without sales 
 * and set  the max value on slider due to max item price 
 */
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
/**
 * filter the ArrayLis that contains all the items due to name, or price or category or type or search or all together 
 * and call showFilteredItems function to show them 
 */
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
/**
 * Load all items in the catalog in the way the customer filtered them
 */
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
/**
 * set text the numberItemInOrder due to number of items the customer select 
 */
	void setLabelNumItemInOrderText() {

		numberItemInOrder.setText(CartController.getOrderInProcess().getItemInOrder() + "");
	}

}
