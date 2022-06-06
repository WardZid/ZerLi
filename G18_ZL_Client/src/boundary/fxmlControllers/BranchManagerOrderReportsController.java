package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.AmountItem;
import entity.MyMessage.MessageType;
import entity.User.UserType;
import entity.Store;
import entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*
	
										1.

	We may need to change our SQL statements so they can be also filtered by the status of the order
	like APPROVED or UNAPPROVED, etc...
	
										2.
										
										

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
    private TableColumn<AmountItem, Integer> amountSoldTableCol;

    @FXML
    private TableColumn<AmountItem, String> itemNameTableCol;

    @FXML
    private ListView<String> monthsListView;

    @FXML
    private PieChart reportPieChart;

    @FXML
    private Text reportMonthText;

    @FXML
    private TableView<AmountItem> reportTableView;

    @FXML
    private Text totalItemsSoldText;
    
    @FXML
    private Text averageText;
    
    @FXML
    private Text maxText;

    @FXML
    private Text minText;

    @FXML
    private Button viewReportButton;
    
    @FXML
	private ComboBox<String> ComboBoxbranches;
    
    
    
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    /* to save data for the table */
    private ObservableList<AmountItem> tableData = FXCollections.observableArrayList();
    
    /* to save data for the pie chart */
    private ObservableList<Data> pieChartData = FXCollections.observableArrayList();
    
    /* ArrayList to save in the ListView */
    private static ArrayList<String> monthsYears;
    
    /* to save the user info */
    private static User user = ClientConsoleController.getUser();
    
    /* the current branch manager's branch ID */
    private static int branchID;
    
    /* An ArrayList that contains the orders of the branch in a specific month of the year */
    private static ArrayList<AmountItem> amountOfItems;
    
    /* the selected month */
    private String month;
    
    /* the selected year */
    private String year;
    
    /* To save the overall income of the selected month */
    private int overallSoldItemsThisMonth;
    
    /* to save the values that will be set in Text */
    private int max,min;
    
    // the average of the selected items in the selected month
    private double avg;
    
    /* names of the max and min sold items */
    private String maxI,minI;
    
    /* number of day in every month of the year */
	private HashMap<String, Integer> daysOfMonth = new HashMap<String, Integer>();
    
	private static ArrayList<String> StoreAddressName = new ArrayList<String>();
	private String AcountType = "BranchManager";

    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		//create comboBox
				ComboBoxbranches.setOnAction(this::onBranchselection);
				if (ClientConsoleController.getUser().getUserType().ordinal() == UserType.CEO.ordinal()) {
					ComboBoxbranches.setVisible(true);
					StoreAddressName = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "store/all",null);
					ObservableList<String> storeAddress = FXCollections.observableArrayList();
					storeAddress.setAll(StoreAddressName);
					ComboBoxbranches.setItems(storeAddress);
					AcountType = "CEO";
				}
		initHelpVariables();
		setBranchID();
		initMonthsListView();
		initTableCols();
		setActionOnListView();
	}
	
	
	/* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
	
	
	
	@SuppressWarnings("unchecked")
	public void onBranchselection(ActionEvent event) {
		monthsListView.getItems().clear();
		branchID=Store.valueOf( ComboBoxbranches.getSelectionModel().getSelectedItem()).ordinal();
		
		monthsYears = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "order/report/sale/months/"+branchID , null);
		monthsListView.getItems().addAll(monthsYears);
		
	
	}
	
	
	/**
	 * Action when a line is selected in the monthsListView. 
	 */
	public void monthSelectedFromListView() {
		if(!monthsListView.getSelectionModel().isEmpty()) {
			saveDate();
			this.viewReportButton.setDisable(false);
		}
	}
	
	/**
	 * When the "View Report" Button is pressed, this method starts,
	 * it will show all the information (Table, Chart, Texts) on 
	 * the report screen.
	 * 
	 * @param event
	 */
	public void viewReportButtonAction(ActionEvent event) {
		if(monthsListView.getSelectionModel().isEmpty()) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You must select a month and year!");
			errorAlert.showAndWait();
		}
		else {
			getDataAfterMonthIsChosen();
			calculateTextValues();
			initDataForPieChart();
			initDataForTable();
			System.out.println(overallSoldItemsThisMonth+" "+avg+" "+max+" "+min);
			reportMonthText.setText(month+"-"+year+" Report");
			reportTableView.setItems(tableData);
			reportPieChart.setData(pieChartData);
			this.totalItemsSoldText.setText(overallSoldItemsThisMonth+"");
			this.averageText.setText(String.format("%.2f", avg));
			this.maxText.setText(max+" ("+maxI+")");
			this.minText.setText(min+" ("+minI+")");
		}
	}
	
	/* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * To initialize the table columns.
	 */
	private void initTableCols() {
		itemNameTableCol.setCellValueFactory(new PropertyValueFactory<AmountItem,String>("name"));
		amountSoldTableCol.setCellValueFactory(new PropertyValueFactory<AmountItem,Integer>("amount"));
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
		if (AcountType != "CEO") {
		monthsYears = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET, "order/report/sale/months/"+branchID , null);
		monthsListView.getItems().addAll(monthsYears);
	 }
	}
	
	/**
	 * Method that asks the server to get the ID of the branch respectively with the current user.
	 */
	@SuppressWarnings("unchecked")
	public void setBranchID() {
		ArrayList<Store> stores = (ArrayList<Store>)MainController.getMyClient().send(MessageType.GET, "store/by/id_user/"+user.getIdUser(), null);
		if(stores.size()!=0)
		branchID = stores.get(0).ordinal();
	}
	
	/**
	 * Method to get data from Server after selection from ListView.
	 */
	@SuppressWarnings("unchecked")
	private void getDataAfterMonthIsChosen() {
		amountOfItems = (ArrayList<AmountItem>)MainController.getMyClient().send(MessageType.GET,"item/amount/"+branchID+"/"+month+"/"+year, null);
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
	 * To calculate the values of the texts in report.
	 */
	public void calculateTextValues() {
		this.overallSoldItemsThisMonth = 0;
		max = amountOfItems.get(0).getAmount();
		maxI = amountOfItems.get(0).getName();
		min = amountOfItems.get(0).getAmount();
		minI = amountOfItems.get(0).getName();
		for(AmountItem ai : amountOfItems) {
			this.overallSoldItemsThisMonth += ai.getAmount();
			if(ai.getAmount() > max) {
				max = ai.getAmount();
				maxI = ai.getName();
			}
			if(ai.getAmount() < min) {
				min = ai.getAmount();
				minI = ai.getName();
			}	
		}
		this.avg = this.overallSoldItemsThisMonth/(double)daysOfMonth.get(month);
	} 
	
	/**
	 * Method to initialize date for the PieChart.
	 */
	private void initDataForPieChart() {
		reportPieChart.setLegendVisible(false);
		pieChartData.clear();
		for(AmountItem ai : amountOfItems) {
			double persent=(ai.getAmount()/(double)overallSoldItemsThisMonth)*100;
			pieChartData.add(new PieChart.Data(ai.getName()+" - "+  String.format("%.2f", persent)+"%"  , ai.getAmount()));
		}
	}
	
	/**
	 * To initialize the data for the table.
	 */
	private void initDataForTable() {
		tableData.clear();
		tableData.addAll(amountOfItems);
	}
	
	/**
	 * To initialize the help variables.
	 */
	private void initHelpVariables() {
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
	
}
