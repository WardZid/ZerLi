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


/**
 * @author hamza
 *
 */
public class CEOIncomeReportsController implements Initializable {
	
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
    
    /*-------------------------------------------------*/
    
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
    
    /* ------------------------------------------------ */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setBranchNamesInArrayList();
		this.branchsChoiceBox.getItems().addAll(branchsNames);
		this.branchsChoiceBox.setOnAction(this::afterBranchSelected);
		monthsListView.getItems().addAll(monthsYears);
		
		
		
		monthsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				monthSelectedFromListView();
			}
		});
		
	}
	
	
	/* ----------------------------------------------------------------- */
	
	
	/**
	 * Function to set the branch names in an ArrayList,
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
	 *  Function to set the branchID according to the selected branch.
	 */
	public void setBranchID(int branchId) {
		branchID = branchId;
	}
	
	/**
	 * @param event
	 * 
	 * Function to do after we select a branch from branchsChoiceBox.
	 */
	@SuppressWarnings("unchecked")
	public void afterBranchSelected(ActionEvent event) {
		setBranchID(Store.valueOf(branchsChoiceBox.getValue()).ordinal());
		monthsYears = (ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "order/report/sale/months/"+branchID, null);
		monthsListView.getItems().addAll(monthsYears);
	}
	
	/**
	 * Function to set the monthsYearsArrayList into monthsYears,
	 * So we can show them in ListView.
	 */
	public static void setMonthsYears(ArrayList<String> monthsYearsArrayList) {
		monthsYears = monthsYearsArrayList;
	}

	
	/**
	 * @param event
	 * 
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
