package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.BuildItem;
import entity.Item;
import entity.Order;
import entity.Item.ItemInBuild;
import entity.Item.OrderItem;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class orderItemController implements Initializable {

	@FXML
	private Label amountLbl;

	@FXML
	private Label nameOrderLbl;

	@FXML
	private Label priceLbl;

	@FXML
	private Button viewBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	Item orderItem;
	BuildItem buildItem;
	CustomerOrdersController customerOrdersController;

	public void setData(OrderItem orderItem, BuildItem buildItem, CustomerOrdersController customerOrdersController) {
		this.orderItem = orderItem;
		this.buildItem = buildItem;
		this.customerOrdersController = customerOrdersController;

		if (orderItem != null) {
			nameOrderLbl.setText("" + orderItem.getName());
			amountLbl.setText("x" + orderItem.getAmount());
			priceLbl.setText(orderItem.getPrice() + "");
		} else {
			nameOrderLbl.setText("Custom Item");
			amountLbl.setText("x" + buildItem.getAmount());
			priceLbl.setText(buildItem.getPrice() + "");
			viewBtn.setVisible(true);
		}

	}

	public void onViewCustomItemPressed() {

		customerOrdersController.showInnerVbox();

		try {

			loaditemtoitemsViewVbox();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loaditemtoitemsViewVbox() throws IOException {

		customerOrdersController.getItemsViewVbox().getChildren().clear();

		// load items
		for (ItemInBuild itemInBuild : buildItem.getItemsInBuild().values()) {
			FXMLLoader fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/orderItem-view.fxml"));
			Node node = fXMLLoader.load();

			orderItemController orderItemController = fXMLLoader.getController();
			orderItemController.setDataforVbox(itemInBuild);

			customerOrdersController.getItemsViewVbox().getChildren().add(node);

		}

	}

	private void setDataforVbox(ItemInBuild itemInBuild) {
		nameOrderLbl.setText("" + itemInBuild.getName());
		amountLbl.setText("x" + itemInBuild.getAmount());
		priceLbl.setText(itemInBuild.getPrice() + "");

	}

}
