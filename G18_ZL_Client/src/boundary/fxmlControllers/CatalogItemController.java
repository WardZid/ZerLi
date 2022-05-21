package boundary.fxmlControllers;

import java.awt.ScrollPane;
import java.net.URL;
import java.util.ResourceBundle;

import entity.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CatalogItemController implements Initializable{

	

    @FXML
    private Label namelabel;

    @FXML
    private Label pricelabel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	public void setData(Item item) {
		System.out.println("here setData"+item);
		namelabel.setText(item.getName());
		pricelabel.setText(item.getPrice()+"");
	}
}
