package boundary.fxmlControllers;
import java.net.URL;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.Store;
import entity.Survey;
import entity.SurveyQuestion;
import entity.SurveySumAnswers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.VBox;
public class ExpertAnalysisController implements Initializable {
	  @FXML
	    private BarChart<String, Double> answersBarChart;
	  @FXML
	    private CategoryAxis x;

	    @FXML
	    private NumberAxis y;
	    @FXML
	    private TextArea q1T;

	    @FXML
	    private TextArea q2T;

	    @FXML
	    private TextArea q3T;

	    @FXML
	    private TextArea q4T;

	    @FXML
	    private TextArea q5T;

	    @FXML
	    private TextArea q6T;

	    @FXML
	    private Button sendReportButton;

	    @FXML
	    private ComboBox<String> surveyYearComboBox;
	    @FXML
	    private ComboBox<Integer> surveyIdQuestionsComboBox;
	    @FXML
	    private VBox vb;
	    private ArrayList<String> dateList;
	    private HashMap<String,HashMap<Integer,SurveyQuestion>> yearIdQuestionsHashMap;
	    //private HashMap<Integer,ArrayList<Survey>> surveyHashMap;
	    //private ArrayList<Integer> surveyIdQuestion;
	 //  private ObservableList<SurveySumAnswers> sumList = FXCollections.observableArrayList();
	 //  private HashMap<Integer,ArrayList<SurveyQuestion>> surveyMap;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		yearIdQuestionsHashMap =  (HashMap<String,HashMap<Integer,SurveyQuestion>>) MainController.getMyClient().send(MessageType.GET,"survey/date_survey", null);
		surveyYearComboBox.getItems().addAll(yearIdQuestionsHashMap.keySet());
		surveyYearComboBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				//surveyIdQuestion = (ArrayList<Integer>) MainController.getMyClient().send(MessageType.GET,"questions/by/date_survey/"+ surveyYearComboBox.getValue(), null);
				vb.setVisible(true);
				surveyIdQuestionsComboBox.getItems().clear();
				surveyIdQuestionsComboBox.getItems().addAll(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).keySet());
				answersBarChart.getData().clear();
			}
		});
		surveyIdQuestionsComboBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<Integer>(){

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				try {
				answersBarChart.getData().clear();
				setQuestions();
				SurveySumAnswers surveySumAnswers = (SurveySumAnswers) MainController.getMyClient().send(MessageType.GET,"survey/by/date_survey && id_question_average/"+ surveyIdQuestionsComboBox.getValue() + "/" + surveyYearComboBox.getValue(), null); 
				XYChart.Series<String,Double> set1 = new XYChart.Series<>();
				for(int i=0; i <6 ; i++)
				set1.getData().add(new XYChart.Data(i+1+"", surveySumAnswers.getAvgAnswers().get(i)));
				answersBarChart.getData().addAll(set1);
				}catch(NullPointerException e) {}
			}

		});
	}
	private void setQuestions() {
		q1T.setText(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue()).getQuestion().get(0));
		q2T.setText(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue()).getQuestion().get(1));
		q3T.setText(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue()).getQuestion().get(2));
		q4T.setText(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue()).getQuestion().get(3));
		q5T.setText(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue()).getQuestion().get(4));
		q6T.setText(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue()).getQuestion().get(5));
	}
}
