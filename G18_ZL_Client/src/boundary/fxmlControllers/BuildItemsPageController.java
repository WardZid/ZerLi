package boundary.fxmlControllers;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import boundary.ClientView;
import boundary.fxmlControllers.ClientConsoleController.Navigation;
import control.MainController;
import entity.BuildItem;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
/**
 * in this class we build the products from items that the customer selected 
 * and we can filter the items 
 * @author saher
 *
 */
public class BuildItemsPageController implements Initializable {
/**
 * to build the product after selected all the items with the amount 
 */
	@FXML
	private Button buildItemBtn;
/**
 * to back to catalog page
 */
	@FXML
	private Button backBtn;
/**
 * to filter the items due to price we selected from slider 
 */
    @FXML
    private Slider SliderPrice;
    /**
     * to insert all the category on this combo box  from DB
     */
	@FXML
	private ComboBox<String> categoryCB;
/**
 * to insert all the types from DB to this combo box
 */
	@FXML
	private ComboBox<String> typeCB;
/**
 * just label
 */
    @FXML
    private Label priceRange;
/**
 * image to click on it and search item due to name 
 */
    @FXML
    private ImageView searchIV;
/**
 * to search item due to name we insert in text field
 */
    @FXML
    private TextField searchTF;
	/**
	 * to view the item name
	 */
	@FXML
	private Label namelabel;
	/**
	 * to arrange the items in grid pane 
	 */
	@FXML
	private GridPane grid;
	/**
	 * parameter that saved the number that returns from the slider
	 * to filter the items due to the price the we get from slider 
	 */
	private int PriceRangeCust;
/**
 * number of the items that we wants to be in the row 
 */
	private int numberOfColumn = 4;
	/**
	 * parameter to save all the build items details from a BuildItem type
	 */
	private static BuildItem buildSelected ;
	/**
	 * to Arrange the items as we want that we get from itemsGetFromDB and use it (Like to show the items that have a sales first)
	 */
	private static ArrayList<Item> items=new ArrayList<Item>();
	/**
	 * in this ArrayList we get all the items from the DB
	 */
	private static ArrayList<Item>itemsGetFromDB;
	/**
	 * we use this ArrayList to filter the items ArrayList As we want(due to price , name , or category) 
	 * and we saved the the items that we filter on this filteredItems and use it  
	 */
	private ArrayList<Item> filteredItems = new ArrayList<Item>();
	private static ArrayList<Item> Checkeditems;
	/**
	 *  to save on this hash map the Category as a key and the type of this category like a value
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		typeCB.getItems().add("All Types");
		categoryCB.getItems().add("All Categories");
		searchIV.setImage(new Image("boundary/media/search-icon.png"));
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
		buildSelected = new BuildItem();
		setLabelNumItemInOrderText();
		double maxprice = 0;
		itemsGetFromDB = (ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);
		items.clear();
		for (Item item : itemsGetFromDB)
		{
			if(item.getSale()!=0)
				items.add(0,item);
			else
				items.add(item);
		}
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
		

		
		Checkeditems = new ArrayList<Item>();
		
		grid.getChildren().clear();
		try {
			
			for (int i = 0; i < filteredItems.size(); i++) {

				FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/build-item-view.fxml"));

				Node node = fXMLLoader.load();
				
				 
			//	filteredItems.get(i).setPrice(filteredItems.get(i).getPriceAfterSale());
				 

				BuildItemController buildItemController = fXMLLoader.getController();
				if (buildItemController == null)
					System.out.println("buildItemController null");

				buildItemController.setData(filteredItems.get(i) , this);

				grid.add(node, column++, row);

				if (column == numberOfColumn) {
					column = 0;
					row++;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	
	private void setLabelNumItemInOrderText() {
 		//countItemInOrder.setText(CartController.getOrderInProcess().getItemInOrder() + "");

	}
/**
 * return to catalog page
 */
	public void onBackButtonPressed() {
//
		Navigation.navigator("catalog-view.fxml");
	}
/**
 * 
 * @return ArrayList<Item> Checkeditems
 */
	public static ArrayList<Item> getCheckeditems() {
 		return Checkeditems;
	}

	/**
	 *  set
	 * @param checkeditems from ArrayList<Item> type
	 */
	public static void setCheckeditems(ArrayList<Item> checkeditems) {
		Checkeditems = checkeditems;
	}

	/**
	 * to set the item  price in setItem method in BuildItem Class 
	 * @param presseditem from Item type
	 * @param Amount int  parameter
	 */
	public static void SelectedItem(Item presseditem, int Amount) {
 
		buildSelected.setItem(presseditem,Amount); 
		System.out.println(buildSelected);

	}
/**
 * delete the item from the hash map
 * @param presseditem from Item type
 */
	public static void deleteItemIsNotselected(Item presseditem) {

 		buildSelected.DeleteItem(presseditem);
 		System.out.println(buildSelected);
	}
	/**
	 * we build the product after the customer selected the items with the amount
	 * and we returns to the catalog else he will get alert message
	 */
    public void onBuildItemPressed() {
    	if(buildSelected.getItemsInBuild().size()!=0) {
    	 CartController.getOrderInProcess().addIBuildtemtoOrder(buildSelected);
    	 buildSelected= new BuildItem();
    	 Navigation.navigator("catalog-view.fxml");
    	}
    	else {
    		Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You should pick item for build");
			errorAlert.showAndWait();
    	}
	}
	

}
