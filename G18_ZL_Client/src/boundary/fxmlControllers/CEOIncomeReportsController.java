package boundary.fxmlControllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import entity.Store;
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
    private ListView<?> monthsListView;

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
    private String[] branchsNames = { "NORTH", "WEST" , "EAST" , "SOUTH" };
    
    /* an ArrayList to fill the list view of the months (monthsListView) */
    private List<String> monthsInListView = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.branchsChoiceBox.getItems().addAll(branchsNames);
		this.branchsChoiceBox.setOnAction(this::monthSelectedFromListView);
		
	}
	
	/**
	 * @param event
	 * 
	 * When a line is selected in the monthsListView,
	 * the viewReportButton will be functional. 
	 */
	public void monthSelectedFromListView(ActionEvent event) {
		this.viewReportButton.setDisable(false);
	}
    
	/**
	 * A function to fill the monthsListView according
	 * to the choice selected by the user in the branchChoiceBox.
	 */
    private void fillMonthsListView(Store store) {
    	
    	switch(store) {
    	
    	case NORTH:
    		fillListViewIf_NORTH();
    		break;
    	case WEST:
    		fillListViewIf_WEST();
    		break;
    	case EAST:
    		fillListViewIf_EAST();
    		break;
    	case SOUTH:
    		fillListViewIf_SOUTH();
    		break;
    	default:
    		System.out.println("Store doesn't exist!");
    		break;
    	}
    }
    
    /*
     * fill the monthsListView if "NORTH" is selected
     */
    private void fillListViewIf_NORTH() {
    	
    }
    
    /*
     * fill the monthsListView if "WEST" is selected
     */
    private void fillListViewIf_WEST() {
    	
    }
    
    /*
     * fill the monthsListView if "EAST" is selected
     */
	private void fillListViewIf_EAST() {
	    	
	}
	
	/*
     * fill the monthsListView if "SOUTH" is selected
     */
	private void fillListViewIf_SOUTH() {
		
	}

}
