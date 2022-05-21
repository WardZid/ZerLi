package boundary.fxmlControllers;

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
import javafx.scene.layout.GridPane;

public class CatalogController implements Initializable {

	@FXML
	private GridPane grid;

	private static ArrayList<Item> items;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		int column = 0;
		int row = 0;
		 
       
		items=(ArrayList<Item>) MainController.getMyClient().send(MessageType.GET, "item/all", null);
		System.out.println(items.toString());
		
		try {
			for (int i = 0; i < items.size(); i++) {

				FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/catalog-item-view.fxml"));
				Node node = fXMLLoader.load();
				
				CatalogItemController catalogItemController = fXMLLoader.getController();
				catalogItemController.setData(items.get(i));

				grid.add(node, column++, row);

				if (column == 3) {
					column = 0;
					row++;
				}

				GridPane.setMargin(node, new Insets(10));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
