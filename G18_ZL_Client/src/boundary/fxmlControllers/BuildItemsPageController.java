package boundary.fxmlControllers;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.ClientView;
import boundary.fxmlControllers.ClientConsoleController.Navigation;
import control.MainController;
import entity.BuildItem;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

public class BuildItemsPageController implements Initializable {

	@FXML
	private Button buildItemBtn;

	@FXML
	private Button backBtn;

	
	
	@FXML
	private Label namelabel;
	
	@FXML
	private GridPane grid;

	private static ArrayList<Item> items=new ArrayList<Item>();
	private static ArrayList<Item>itemsGetFromDB;
	private static ArrayList<Item> Checkeditems;

	private int numberOfColumn = 4;
	private static BuildItem buildSelected ;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buildSelected = new BuildItem();
		int column = 0;
		int row = 1;
		setLabelNumItemInOrderText();

		itemsGetFromDB = (ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);
		items.clear();
		for (Item item : itemsGetFromDB)
		{
			if(item.getSale()!=0)
				items.add(0,item);
			else
				items.add(item);
		}
		Checkeditems = new ArrayList<Item>();
		try {
			for (int i = 0; i < items.size(); i++) {

				FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/build-item-view.fxml"));

				Node node = fXMLLoader.load();
				
				 
				 items.get(i).setPrice(items.get(i).getPriceAfterSale());
				 

				BuildItemController buildItemController = fXMLLoader.getController();
				if (buildItemController == null)
					System.out.println("buildItemController null");

				buildItemController.setData(items.get(i) , this);

				grid.add(node, column++, row);

				if (column == numberOfColumn) {
					column = 0;
					row++;
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    	}else {
    		Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You should pick item for build");
			errorAlert.showAndWait();
    	}
	}
	

}
