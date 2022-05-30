package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.AmountItem;
import entity.Store;
import entity.MyMessage.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class CEOOrderReportsController implements Initializable {

	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
	@FXML
    private TableView<AmountItem> reportTableView;

    @FXML
    private TableColumn<AmountItem, Integer> amountSoldTableCol;

    @FXML
    private TableColumn<AmountItem, String> itemNameTableCol;

    @FXML
    private ChoiceBox<String> branchsChoiceBox;

    @FXML
    private ListView<String> monthsListView;

    @FXML
    private PieChart reportPieChart;

    @FXML
    private Text reportMonthText;

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

    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    /* to save data for the table */
    private ObservableList<AmountItem> tableData = FXCollections.observableArrayList();
    
    /* to save data for the pie chart */
    private ObservableList<Data> pieChartData = FXCollections.observableArrayList();
    
    /* array of the names of the branches */
    private static ArrayList<String> branchsNames;
    
    /* an ArrayList to fill the list view of the months (monthsListView) */
    private  static ArrayList<String> monthsYears = null;
    
    /* the selected branch's ID */
    private static int branchID;
    
    /* An ArrayList that contains the orders of the branch in a specific month of the year */
    private static ArrayList<AmountItem> amountOfItems;
    
    /* the selected month */
    private String month;
    
    /* the selected year */
    private String year;
    
    /* To save the overall income of the selected month */
    private double overallSoldItemsThisMonth;
    
    /* to save the values that will be set in Text */
    private double max,min,avg;
    
    /* names of the max and min sold items */
    private String maxI,minI;
    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
	 * Action when a line is selected in the monthsListView. 
	 */
	public void monthSelectedFromListView() {
		saveDate();
		this.viewReportButton.setDisable(false);
		getDataAfterMonthIsChosen();
		initDataForPieChart();
		initDataForTable();
	}
	
	/**
	 * When the "View Report" Button is pressed, this method starts,
	 * it will show all the information (Table, Chart, Texts) on 
	 * the report screen.
	 * 
	 * @param event
	 */
	public void viewReportButtonAction(ActionEvent event) {
		reportMonthText.setText(month+"-"+year+" Report");
		reportTableView.setItems(tableData);
		reportPieChart.setData(pieChartData);
		this.totalItemsSoldText.setText(overallSoldItemsThisMonth+"");
		this.averageText.setText(avg+"");
		this.maxText.setText(max+" ("+maxI+")");
		this.minText.setText(min+" ("+minI+")");
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
		this.avg = this.overallSoldItemsThisMonth/amountOfItems.size();	
	}
	
	/**
	 * Method to initialize date for the PieChart.
	 */
	private void initDataForPieChart() {
		reportPieChart.setLegendVisible(false);
		pieChartData.clear();
		for(AmountItem ai : amountOfItems) {
			pieChartData.add(new PieChart.Data(ai.getName(), ai.getAmount()));
		}
	}
	
	/**
	 * To initialize the data for the table.
	 */
	private void initDataForTable() {
		tableData.clear();
		tableData.addAll(amountOfItems);
	}
	
}
