package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.Order;
import entity.Store;
import entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


/**
 * @author hamza
 *
 */
public class BranchManagerOrderReportsController implements Initializable {

	@FXML
    private TableColumn<?, ?> amountSoldTableCol;

    @FXML
    private TableColumn<?, ?> itemNameTableCol;

    @FXML
    private ListView<String> monthsListView;

    @FXML
    private BarChart<?, ?> reportBarChart;

    @FXML
    private Text reportMonthText;

    @FXML
    private TableView<?> reportTableView;

    @FXML
    private TextField totalItemsSoldTextField;

    @FXML
    private Button viewReportButton;
    
    /* ------------------------------------------------------------------- */
    
    /* ArrayList to save in the ListView */
    private static ArrayList<String> monthsYears;
    
    /* to save the user info */
    private static User user = ClientConsoleController.getUser();
    
    /* the current branch manager's branch ID */
    private static int branchID;
    
    /* An ArrayList that contains the orders of the branch in a specific month of the year */
    private static ArrayList<Order> ordersArray;
    
    /* ------------------------------------------------------------------- */

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ArrayList<Store> stores = (ArrayList<Store>)MainController.getMyClient().send(MessageType.GET, "store/by/id_user/"+user.getIdUser(), null);
		branchID = stores.get(0).ordinal();
		monthsYears = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "order/report/sale/months/"+branchID , null);
		monthsListView.getItems().addAll(monthsYears);
		
		monthsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				monthSelectedFromListView();
			}
		});
		
	}
	
	
	/* ----------------------------------------------------------------- */
	
	
//	public static ArrayList<Order> getOrdersByBranchMonthYear(String branch,String month, String year){
//		ArrayList<Order> orders = new ArrayList<>();
//		ResultSet rs;
//		try {
//			rs = statement.executeQuery("SELECT * FROM assignment3.order O WHERE id_store = "+branch+" AND (Month(O.date_order)) = "+month+" AND (Year(O.date_order)) = "+ year);
//			rs.beforeFirst(); // ---move back to first row
//			while (rs.next()) {
//				orders.add(new Order(
//						rs.getInt("id_order"),
//						rs.getInt("id_customer"),
//						rs.getInt("id_store"),
//						rs.getInt("id_order_status"),
//						rs.getDouble("price_order"),
//						rs.getString("date_order"),
//						rs.getString("delivery_date_order"),
//						rs.getString("address_order"),
//						rs.getString("greeting_order"),
//						rs.getString("description_order")));
//			}
//			return orders;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return orders;
//	}
	
	
	/* ----------------------------------------------------------------------------*/
	
	
	/**
	 * Function to set the monthsYearsArrayList into monthsYears,
	 * So we can show them in ListView.
	 */
	public static void setMonthsYears(ArrayList<String> monthsYearsArrayList) {
		monthsYears = monthsYearsArrayList;
	}
	
	/**
	 * Function to set the current branch manager branchID.
	 */
	public void setBranchID(int branchId) {
		branchID = branchId;
	}
	
	/**
	 * Action when a line is selected in the monthsListView. 
	 */
	@SuppressWarnings("unchecked")
	public void monthSelectedFromListView() {
		String[] splitedDate;
		String month, year;
		splitedDate = monthsListView.getSelectionModel().getSelectedItem().split("/");
		month = splitedDate[0];
		year = splitedDate[1];
		this.viewReportButton.setDisable(false);
		ordersArray = (ArrayList<Order>)MainController.getMyClient().send(MessageType.GET,"order/byBranchMonth/"+branchID+"/"+month+"/"+year, null);
		   
//		SELECT count(OI.amount)
//		FROM order_item OI , assignment3.order O
//		WHERE O.id_order IN (
//			SELECT id_order
//			FROM assignment3.order 
//			WHERE id_store = 2 AND (Month(O.date_order)) = 5 AND (Year(O.date_order)) = 2022
//		)
		
	}
	
	
}
