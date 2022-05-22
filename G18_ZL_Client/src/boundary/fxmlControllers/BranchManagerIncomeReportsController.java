package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.Order;
import entity.Receipt;
import entity.Store;
import entity.User;
import entity.DailyIncome;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
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
public class BranchManagerIncomeReportsController implements Initializable {

	@FXML
    private TableColumn<?, ?> dateTableCol;

    @FXML
    private TableColumn<?, ?> incomeTableCol;

    @FXML
    private ListView<String> monthsListView;

    @FXML
    private TableColumn<?, ?> nameTableCol;

    @FXML
    private LineChart<?, ?> reportLineChart;

    @FXML
    private Text reportMonthText;

    @FXML
    private TableView<?> reportTableView;

    @FXML
    private TextField totalIncomeTextField;

    @FXML
    private Button viewReportButton;
    
    /* ------------------------------------------------------------------- */
    
    /* ArrayList to save in the ListView */
    private static ArrayList<String> monthsYears;
    
    /* to save the user info */
    private static User user = ClientConsoleController.getUser();
    
    /* the current branch manager's branch(store) ID */
    private static int branchID;
    
    /* An ArrayList that contains the orders of the branch in a specific month of the year */
    private static ArrayList<Order> ordersArray;
    
    /* An ArrayList that contains the daily incomes of the selected month in this store */
    private ArrayList<DailyIncome> dailyIncomesOfMonth;
    
    /* An ArrayList that contains the customer's receipts of the selected month */
    private ArrayList<Receipt> receiptsOfTheMonth;
    
    
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
    
    
    /* ------------------------------------------------------------------- */
    // TO GET AN ARRAYLIST<INTEGER> OF THE DAILY INCOME OF THIS BRANCH IN THE SELECTED MONTH
    // order/report/sum/income/branchID/month/year
//    SELECT day(O.date_order) as day , sum(O.price_order) as income 
//    FROM assignment3.order O 
//    WHERE O.id_store = 2 AND Month(O.date_order) = 5 AND Year(O.date_order) = 2022
//    GROUP BY Day(O.date_order)
//    ORDER BY day
    
    // TO GET AN ARRAYLIST<reportTable> (NAME , DATE , INCOME) OF THE CURRENT BRANCH IN THE SELECTED MONTH
    // order/report/incomebycustomer/branchID/month/year
//    SELECT C.name_customer as name , O.date_order as date , O.price_order as income
//    FROM assignment3.order O , assignment3.customer C
//    WHERE C.id_customer = O.id_customer AND O.id_store = 2 AND Month(O.date_order) = 5 AND Year(O.date_order) = 2022
    
    /* ------------------------------------------------------------------- */
        
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
	public void setBranchID(int idStore) {
		branchID  = idStore;
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
		dailyIncomesOfMonth = (ArrayList<DailyIncome>)MainController.getMyClient().send(MessageType.GET,"order/report/sum/income/"+branchID+"/"+month+"/"+year, null);
		receiptsOfTheMonth = (ArrayList<Receipt>)MainController.getMyClient().send(MessageType.GET,"order/report/incomebycustomer/"+branchID+"/"+month+"/"+year, null);
	}
	
}
