package boundary.fxmlControllers;

import java.awt.ScrollPane;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class CatalogController implements Initializable {

	@FXML
	private static GridPane grid;

	@FXML
	private ScrollPane scroll;

	private static ArrayList<Item> items;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		int column = 0;
		int row = 0;
		 
       
		items=(ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);
		System.out.println(items.toString());
		
		try {
			for (int i = 0; i < items.size(); i++) {

				FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/catalog-item-view.fxml"));
				//fXMLLoader.setLocation();

				CatalogItemController catalogItemController = fXMLLoader.getController();
                  
				catalogItemController.setData(items.get(i));

				AnchorPane anchorPane = fXMLLoader.load();
				grid.add(anchorPane, column++, row);

				if (column == 3) {
					column = 0;
					row++;
				}

				GridPane.setMargin(anchorPane, new Insets(10));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void setItems(ArrayList<Item> content) {
		int column = 0;
		int row = 0;
		items = content;
		Node currentNode;
		System.out.println(items.toString());
		
		
		/*
		try {
			currentNode = FXMLLoader.load(ClientView.class.getResource("fxmls/catalog-item-view.fxml"));
			grid.getChildren().clear();
			grid.getChildren().add(currentNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		

		 
		try {
			for (int i = 0; i < items.size(); i++) {

				FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/catalog-item-view.fxml"));
				//fXMLLoader.setLocation(ClientView.class.getResource("fxmls/catalog-item-view.fxml"));
				AnchorPane anchorPane = fXMLLoader.load();
				CatalogItemController catalogItemController = fXMLLoader.getController();
				catalogItemController.setData(items.get(i));

				
				grid.add(anchorPane, column++, row);

				if (column == 3) {
					column = 0;
					row++;
				}

				GridPane.setMargin(anchorPane, new Insets(10));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
