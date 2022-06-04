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

public class BuildItemsPageController implements Initializable {

	@FXML
	private Button buildItemBtn;

	@FXML
	private Button backBtn;

    @FXML
    private Slider SliderPrice;
	@FXML
	private ComboBox<String> categoryCB;

	@FXML
	private ComboBox<String> typeCB;

    @FXML
    private Label priceRange;

    @FXML
    private ImageView searchIV;

    @FXML
    private TextField searchTF;
	
	@FXML
	private Label namelabel;
	
	@FXML
	private GridPane grid;
	
	private int PriceRangeCust;

	private int numberOfColumn = 4;
	private static BuildItem buildSelected ;
	
	private static ArrayList<Item> items=new ArrayList<Item>();
	private static ArrayList<Item>itemsGetFromDB;
	private ArrayList<Item> filteredItems = new ArrayList<Item>();
	private static ArrayList<Item> Checkeditems;
	private static HashMap<String, String> categoryType = new HashMap<String, String>();

	public static HashMap<String, String> getCategoryType() {
		return categoryType;
	}



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

	public void onBackButtonPressed() {
//
		Navigation.navigator("catalog-view.fxml");
	}

	public static ArrayList<Item> getCheckeditems() {
 		return Checkeditems;
	}

	public static void setCheckeditems(ArrayList<Item> checkeditems) {
		Checkeditems = checkeditems;
	}

	
	public static void SelectedItem(Item presseditem, int Amount) {
 
		buildSelected.setItem(presseditem,Amount); 
		System.out.println(buildSelected);

	}

	public static void deleteItemIsNotselected(Item presseditem) {

 		buildSelected.DeleteItem(presseditem);
 		System.out.println(buildSelected);
	}
	
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
