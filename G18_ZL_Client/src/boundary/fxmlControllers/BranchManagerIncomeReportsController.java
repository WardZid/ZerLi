package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*

										1.
										
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
    private TableView<Receipt> reportTableView;
	
    @FXML
    private TableColumn<Receipt, String> nameTableCol;
	
	@FXML
    private TableColumn<Receipt, String> dateTableCol;

    @FXML
    private TableColumn<Receipt, Double> incomeTableCol;

    @FXML
    private ListView<String> monthsListView;

    @FXML
    private LineChart<Integer, Double> reportLineChart;

    @FXML
    private Text reportMonthText;

    @FXML
    private TextField totalIncomeTextField;

    @FXML
    private Button viewReportButton;
    
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    /* XYChart series to insert values in the line chart */
    XYChart.Series<Integer, Double> series = new XYChart.Series<Integer, Double>();
    
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
    
    /* number of day in every month of the year */
    private HashMap<String, Integer> daysOfMonth = new HashMap<String,Integer>();
    
    /* the incomes of a month */
    private ArrayList<Double> incomesOfMonth = new ArrayList<>();
    
    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	initHelpVariables();
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
		initLineChartVars();
	}
	
	/**
	 * Actions to do when viewReport Button is pressed.
	 * 
	 * @param event
	 */
	public void viewReportButtonAction(ActionEvent event) {
		reportMonthText.setText("Report of "+month+"-"+year);
		reportLineChart.setTitle("Daily Incomes Of "+month+"-"+year);
		reportLineChart.getData().add(series);
		totalIncomeTextField.setText(overallIncomeThisMonth+"");
		fillReceiptsTable();
	}
    
    /* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * Method to set the values in the table
	 */
	private void fillReceiptsTable() {
		nameTableCol.setCellValueFactory(new PropertyValueFactory<Receipt,String>("name"));
		dateTableCol.setCellValueFactory(new PropertyValueFactory<Receipt,String>("date"));
		incomeTableCol.setCellValueFactory(new PropertyValueFactory<Receipt,Double>("income"));
		ObservableList<Receipt> ol = FXCollections.observableArrayList(receiptsOfTheMonth);
		reportTableView.setItems(ol);
	}
    
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
	
	/**
	 * To initialize the help variables.
	 */
	private void initHelpVariables(){
		daysOfMonth.put("1", 31);
		daysOfMonth.put("2", 29);
		daysOfMonth.put("3", 31);
		daysOfMonth.put("4", 30);
		daysOfMonth.put("5", 31);
		daysOfMonth.put("6", 30);
		daysOfMonth.put("7", 31);
		daysOfMonth.put("8", 31);
		daysOfMonth.put("9", 30);
		daysOfMonth.put("10", 31);
		daysOfMonth.put("11", 30);
		daysOfMonth.put("12", 31);
	}
	
	/**
	 * Method to initialize the helpful variables of the line chart
	 */
	private void initLineChartVars() {
		incomesOfMonth.clear();
		for(int i = 0 ; i < daysOfMonth.get(month) ; i++)
			incomesOfMonth.add(0.0);
		for(int j = 0 ; j < dailyIncomesOfMonth.size() ; j++) {
			int dayIndex = dailyIncomesOfMonth.get(j).getDay();
			double income = dailyIncomesOfMonth.get(j).getIncome();
			incomesOfMonth.set(dayIndex, incomesOfMonth.get(dayIndex)+income);
		}
		int d=0;
		for(Double income : incomesOfMonth) {
			series.getData().add(new XYChart.Data<Integer, Double>(d, income));
			d++;
		}
		
	}
	
}
