package boundary.fxmlControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import boundary.ClientView;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*
 *  
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
    private Tab incomeReportTab;

    @FXML
    private StackPane incomeSP;

    @FXML
    private StackPane quarterSP;

    @FXML
    private Tab quarterTab;


    @FXML
    private TabPane tabPane;
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
     
    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 
		FXMLLoader fXMLLoader = null;
		Node node = null;
		try {
			fXMLLoader = new FXMLLoader(ClientView.class.getResource("fxmls/branch-manager-income-reports-view.fxml"));
		    node = fXMLLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tab tab1 =new Tab("Planes",node  );
		BranchManagerIncomeReportsController branchManagerIncomeReportsController = fXMLLoader.getController();
	
		tabPane.getTabs().add(tab1);
	
	}
	
	
    /* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
	
	  
	
    /* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	 
	 
		
	  
}
