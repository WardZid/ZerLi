package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.DailyIncome;
import entity.Order;
import entity.Receipt;
import entity.Store;
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
import javafx.scene.control.ChoiceBox;
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
 * 1.
 * */

/**
 * @author hamza
 *
 */
public class CEOIncomeReportsController implements Initializable {
	
	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
	@FXML
    private TableColumn<Receipt, String> dateTableCol;

    @FXML
    private TableColumn<Receipt, Double> incomeTableCol;

    @FXML
    private ChoiceBox<String> branchsChoiceBox;

    @FXML
    private ListView<String> monthsListView;

    @FXML
    private TableColumn<Receipt, String> nameTableCol;

    @FXML
    private LineChart<Integer, Double> reportLineChart;

    @FXML
    private Text reportMonthText;

    @FXML
    private TableView<Receipt> reportTableView;

    @FXML
    private Text totalIncomeText;
    
    @FXML
    private Text averageText;
	
	@FXML
    private Text maxText;

    @FXML
    private Text minText;

    @FXML
    private Button viewReportButton;
    
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    /* XYChart series to insert values in the line chart */
    XYChart.Series<Integer, Double> series = new XYChart.Series<Integer, Double>();
    
    /* array of the names of the branches */
    private static ArrayList<String> branchsNames;
    
    /* an ArrayList to fill the list view of the months (monthsListView) */
    private  static ArrayList<String> monthsYears;
    
    /* the selected branch's ID */
    private static int branchID;
    
    /* An ArrayList that contains the orders of the branch in a specific month of the year */
    private static ArrayList<Order> ordersArray;
    
    /* An ArrayList that contains the daily incomes of the selected month in this store */
    private ArrayList<DailyIncome> dailyIncomesOfMonth;
    
    /* An ArrayList that contains the customer's receipts of the selected month */
    private ArrayList<Receipt> receiptsOfTheMonth;
    
    /* To save the overall income of the selected month */
    private double overallIncomeThisMonth;
    
    /* to save the values that will be set in Text */
    private double max,min,avg;
    
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		initHelpVariables();
		initBranchesChoiceBox();
		initTableCols();
		this.branchsChoiceBox.setOnAction(this::afterBranchSelected);
		monthsListView.getItems().addAll(monthsYears);
		setActionOnListView();
		
	}
	
	
    /* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * @param event
	 * 
	 * Method to do after we select a branch from branchsChoiceBox.
	 */
	public void afterBranchSelected(ActionEvent event) {
		setBranchID();
		initMonthsListView();
		
	}
	
	/**
	 * @param event
	 * 
	 * Action when a line is selected in the monthsListView. 
	 */
	public void monthSelectedFromListView() {
		saveDate();
		this.viewReportButton.setDisable(false);
		getDataAfterMonthIsChosen();
		calculateTextValues();
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
		totalIncomeText.setText(overallIncomeThisMonth+"");
		averageText.setText(avg+"");
		minText.setText(min+"");
		maxText.setText(max+"");
		fillReceiptsTable();
	}
	
    /* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * To initialize the table columns.
	 */
	private void initTableCols() {
		nameTableCol.setCellValueFactory(new PropertyValueFactory<Receipt,String>("name"));
		dateTableCol.setCellValueFactory(new PropertyValueFactory<Receipt,String>("date"));
		incomeTableCol.setCellValueFactory(new PropertyValueFactory<Receipt,Double>("income"));
	}
	
	/**
	 * Method to set the values in the table
	 */
	private void fillReceiptsTable() {
		ObservableList<Receipt> ol = FXCollections.observableArrayList(receiptsOfTheMonth);
		reportTableView.setItems(ol);
	}
	
	/**
	 * Method to initialize the choiceBox of the branches
	 */
	private void initBranchesChoiceBox() {
		setBranchNamesInArrayList();
		this.branchsChoiceBox.getItems().addAll(branchsNames);
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
	 * Method to set the branch names in an ArrayList,
	 * so we can show them in the ChoiceBox.
	 */
	private void setBranchNamesInArrayList() {
		for(Store s : Store.values()) {
			branchsNames.add(s.toString());
		}
	}

	/**
	 * @param branchId		the ID we want to set
	 * 
	 *  Method to set the branchID according to the selected branch.
	 */
	public void setBranchID() {
		branchID = Store.valueOf(branchsChoiceBox.getValue()).ordinal();
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
	public void calculateTextValues() {
		this.overallIncomeThisMonth = 0;
		this.max = incomesOfMonth.get(0);
		this.min = incomesOfMonth.get(0);
		for(Double d : incomesOfMonth) {
			this.overallIncomeThisMonth += d;
			if(d > max)
				max = d;
			if(d < min)
				min = d;
		}
		this.avg = this.overallIncomeThisMonth/incomesOfMonth.size();
		
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
