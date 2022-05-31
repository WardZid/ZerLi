package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.ClientView;
import boundary.fxmlControllers.ClientConsoleController.Navigation;
import control.MainController;
import entity.Item;
import entity.Item.OrderItem;
import entity.MyMessage.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CatalogController implements Initializable {

	@FXML
	private GridPane grid;
	@FXML
	private ComboBox<String> CategoryComboBox;

	@FXML
	private Label numberItemInOrder;
	
    @FXML
    private VBox vboxViewItemDescription;
    @FXML
    private Label descriptionLable;

    @FXML
    private Label nameItemLable;
    
    @FXML
    private VBox catalogvbox;


	private static ArrayList<Item> items;

	private static ArrayList<String> Category = new ArrayList<String>();

	public void LoadCatalog(String category) throws IOException {
		int column = 0;
		int row = 1;
		int flagForALlItem = 0;
		grid.getChildren().clear();
		for (int i = 0; i < items.size(); i++) {
			flagForALlItem = 0;
			if (category.equals("All Items"))
				flagForALlItem = 1;

			if (flagForALlItem == 1 || category.equals(Category.get(items.get(i).getIdCategory() + 1))) {

				FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/catalog-item-view.fxml"));
				Node node = fXMLLoader.load();

				CatalogItemController catalogItemController = fXMLLoader.getController();
				catalogItemController.setData(items.get(i), this);

				if (column == 4) {
					column = 0;
					row++;
				}

				grid.add(node, column++, row);

			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		this.getvboxViewItemDescription().setVisible(false);
		
		setLabelNumItemInOrderText();

		Category = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "category/all", null);
		Category.add(0, "All Items");
		ObservableList<String> catagory = FXCollections.observableArrayList();
		catagory.setAll(Category);
		CategoryComboBox.setItems(catagory);

		items = (ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);

		try {
			LoadCatalog("All Items");
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	public void GetSelected() {
		CategoryComboBox.setOnAction(e -> {

			try {
				LoadCatalog(CategoryComboBox.getValue());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}

	void setLabelNumItemInOrderText() {

		numberItemInOrder.setText(CartController.getOrderInProcess().getItemInOrder() + "");
	}

}
