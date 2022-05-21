package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.User;
import entity.MyMessage.MessageType;
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
    
    /* the current branch manager's branch ID */
    private static int branchID;
    
    /* ------------------------------------------------------------------- */

    @Override
	public void initialize(URL location, ResourceBundle resources) {
		MainController.getMyClient().send(MessageType.GET, "", null);
		MainController.getMyClient().send(MessageType.GET, "order/report/sale/months/"+branchID, null);
		monthsListView.getItems().addAll(monthsYears);
		
	}
    
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
	public void setBranchID(int branchId) {
		branchID = branchId;
	}
	
}
