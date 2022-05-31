package boundary.fxmlControllers;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.AnswersDistribution;
import entity.Survey;
import entity.Survey.SurveyQuestion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
public class ExpertAnalysisController implements Initializable {
	 @FXML
	    private BarChart<?,?> answersBarChart;

	    @FXML
	    private TableColumn<AnswersDistribution, Integer> answersTableColumn;

	    @FXML
	    private TableView<AnswersDistribution> answersTableView;

	    @FXML
	    private TableColumn<AnswersDistribution,Integer> distributionTableColumn;

	    @FXML
	    private ComboBox<Integer> questionsComboBox;

	    @FXML
	    private Button sendReportButton;
	    @FXML
	    private ComboBox<Date> surveyDatesComboBox;

	    @FXML
	    private ComboBox<Integer> surveyIdComboBox;
	    @FXML
	    private TextArea questionT;
	    @FXML
	    private CategoryAxis x;

	    @FXML
	    private NumberAxis y;
	    private ObservableList<AnswersDistribution> answersList = FXCollections.observableArrayList(
	    		new AnswersDistribution(1, 5),new AnswersDistribution(2, 10),new AnswersDistribution(3, 50));

		@Override
		public void initialize(URL location, ResourceBundle resources) {	
			//send to server 
			answersTableColumn.setCellValueFactory(new PropertyValueFactory<AnswersDistribution,Integer>("Answer"));
			distributionTableColumn.setCellValueFactory(new PropertyValueFactory<AnswersDistribution,Integer>("Quantity"));
			questionsComboBox.setValue(1);
			answersTableView.setItems(answersList);
			XYChart.Series chart = new XYChart.Series<>();
			chart.getData().add(new XYChart.Data("1",5));
			chart.getData().add(new XYChart.Data("2",10));
			chart.getData().add(new XYChart.Data("3",50));
			chart.getData().add(new XYChart.Data("4",25));
			//chart.getData().add(new XYChart.Data("1",5));
			//chart.getData().add(new XYChart.Data("1",5));
			answersBarChart.getData().addAll(chart);
		}

}
