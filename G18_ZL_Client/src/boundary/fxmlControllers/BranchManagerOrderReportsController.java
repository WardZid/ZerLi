package boundary.fxmlControllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.DailyIncome;
import entity.Order;
import entity.Receipt;
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

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*
									1.

SELECT I.name , sum(OI.amount) as amount
FROM item I , assignment3.order_item OI , assignment3.order O
WHERE I.id_item = OI.id_item AND OI.id_order IN (
	SELECT O.id_order
	WHERE O.id_store = 2 AND Month(O.date_order) = 5 AND Year(O.date_order) = 2022 
) 
GROUP BY I.name

class AmountItem(String name , int amount)

GET -> "item/amount/branchID/month/year

public static ArrayList<AmountItem> getAmountOfEveryItem(String branch_id, String month, String year){
		ArrayList<AmountItem> amounts = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery(" #XYZ# ");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				amounts.add(new AmountItem(
						rs.getString("name"),
						rs.getInt("amount")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amounts;
}
	
										2.

	We may need to change our SQL statements so they can be also filtered by the status of the order
	like APPROVED or UNAPPROVED, etc...
	
										3.
										
										

 * */

/**
 * @author hamza
 *
 */
public class BranchManagerOrderReportsController implements Initializable {

	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
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
    
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    /* ArrayList to save in the ListView */
    private static ArrayList<String> monthsYears;
    
    /* to save the user info */
    private static User user = ClientConsoleController.getUser();
    
    /* the current branch manager's branch ID */
    private static int branchID;
    
    /* An ArrayList that contains the orders of the branch in a specific month of the year */
    private static ArrayList<Order> ordersArray;
    
    /* the selected month */
    private String month;
    
    /* the selected year */
    private String year;
    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setBranchID();
		initMonthsListView();
		setActionOnListView();
		
	}
	
	
	/* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * Action when a line is selected in the monthsListView. 
	 */
	public void monthSelectedFromListView() {
		saveDate();
		this.viewReportButton.setDisable(false);
		getDataAfterMonthIsChosen();
		
//		SELECT count(OI.amount)
//		FROM order_item OI , assignment3.order O
//		WHERE O.id_order IN (
//			SELECT id_order
//			FROM assignment3.order 
//			WHERE id_store = 2 AND (Month(O.date_order)) = 5 AND (Year(O.date_order)) = 2022
//		)
		
	}
	
	/* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * Method to do when a month is selected from ListView
	 */
	private void setActionOnListView() {
		monthsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			// this method has the main Action that happens when selection accrues on ListView
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				monthSelectedFromListView();
			}
		});
	}
	
	/**
	 * Method to initialize the months ListView
	 */
	@SuppressWarnings("unchecked")
	private void initMonthsListView() {
		monthsYears = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "order/report/sale/months/"+branchID , null);
		monthsListView.getItems().addAll(monthsYears);
	}
	
	/**
	 * Method that asks the server to get the ID of the branch respectively with the current user.
	 */
	@SuppressWarnings("unchecked")
	public void setBranchID() {
		ArrayList<Store> stores = (ArrayList<Store>)MainController.getMyClient().send(MessageType.GET, "store/by/id_user/"+user.getIdUser(), null);
		branchID = stores.get(0).ordinal();
	}
	
	/**
	 * Method to get data from Server after selection from ListView.
	 */
	@SuppressWarnings("unchecked")
	private void getDataAfterMonthIsChosen() {
		ordersArray = (ArrayList<Order>)MainController.getMyClient().send(MessageType.GET,"order/byBranchMonth/"+branchID+"/"+month+"/"+year, null);
	}
	
	/**
	 * Method to save the selected date.
	 */
	private void saveDate() {
		String[] splitedDate = monthsListView.getSelectionModel().getSelectedItem().split("/");
		month = splitedDate[0];
		year = splitedDate[1];
	}
	
}
