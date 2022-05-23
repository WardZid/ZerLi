package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.DailyIncome;
import entity.Order;
import entity.Receipt;
import entity.Store;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
public class CEOIncomeReportsController implements Initializable {
	
	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
	@FXML
    private TableColumn<?, ?> dateTableCol;

    @FXML
    private TableColumn<?, ?> incomeTableCol;

    @FXML
    private ChoiceBox<String> branchsChoiceBox;

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
    
    /* the selected month */
    private String month;
    
    /* the selected year */
    private String year;
    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initBranchesChoiceBox();
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
		calculateOverallIncomeOfTheMonth();
		
	}
	
    /* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
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
	public void calculateOverallIncomeOfTheMonth() {
		this.overallIncomeThisMonth = 0;
		for(DailyIncome di : dailyIncomesOfMonth) {
			this.overallIncomeThisMonth += di.getIncome();
		}
	}
	
}
