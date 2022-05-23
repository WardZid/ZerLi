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

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/* ------------------------------------------------ */
/*
 * 1. 
 * */

/**
 * @author hamza
 *
 */
public class BranchManagerIncomeReportsController implements Initializable {

	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
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
    
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
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
    
    /* To save the overall income of the selected month */
    private double overallIncomeThisMonth;
    
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
		calculateOverallOfTheMonth();
		
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
		dailyIncomesOfMonth = (ArrayList<DailyIncome>)MainController.getMyClient().send(MessageType.GET,"order/report/sum/income/"+branchID+"/"+month+"/"+year, null);
		receiptsOfTheMonth = (ArrayList<Receipt>)MainController.getMyClient().send(MessageType.GET,"order/report/incomebycustomer/"+branchID+"/"+month+"/"+year, null);
	}
	
	/**
	 * Method to save the selected date.
	 */
	private void saveDate() {
		String[] splitedDate = monthsListView.getSelectionModel().getSelectedItem().split("/");
		month = splitedDate[0];
		year = splitedDate[1];
	}
	
	/**
	 * To calculate the overall income value of the selected month of this branch
	 */
	public void calculateOverallOfTheMonth() {
		this.overallIncomeThisMonth = 0;
		for(DailyIncome di : dailyIncomesOfMonth) {
			this.overallIncomeThisMonth += di.getIncome();
		}
	}
	
}
