package boundary.fxmlControllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.SurveyQuestion;
import entity.SurveyReport;
import entity.SurveySumAnswers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
/** 
 * This class overrides initialize method 
 * @author Aziz Hamed
 * This class is used in order to give the expert opportunity to show and analyse the results and sending report functionality
 */
public class ExpertAnalysisController implements Initializable {
	
	/**
	 * bytes to save the file as binary file
	 */
	
	private byte[] pdfBytes;
	
	/**
	 * answerBarChart to show the average of answers for each question during specfic year
	 */
	@FXML
	private BarChart<String, Double> answersBarChart;
	
	/**
	 * label that will be visible when file is uploaded and the text will be the name of the file
	 */
	@FXML
	private Label fileNameLbl;
	
	/**
	 * The vbox that contains the file upload area
	 */

	@FXML
	private VBox uploadVBox;
	
	/**
	 * The x axis of the barChart
	 */

	@FXML
	private CategoryAxis x;
	
	/**
	 * The y axis of the barChart
	 */

	@FXML
	private NumberAxis y;
	
	/**
	 * Text area to show the first question of the survey
	 */
	@FXML
	private TextArea q1T;
	
	/**
	 * Text area to show the second question of the survey
	 */

	@FXML
	private TextArea q2T;
	
	/**
	 * Text area to show the third question of the survey
	 */

	@FXML
	private TextArea q3T;
	
	/**
	 * Text area to show the forth question of the survey
	 */

	@FXML
	private TextArea q4T;
	
	/**
	 * Text area to show the fifth question of the survey
	 */

	@FXML
	private TextArea q5T;
	
	/**
	 * Text area to show the sixth question of the survey
	 */

	@FXML
	private TextArea q6T;
	
	/**
	 * Button to send the report
	 */

	@FXML
	private Button sendReportButton;
	
	/**
	 * ComboBox that contains all the years which surveys conducted  
	 */

	@FXML
	private ComboBox<String> surveyYearComboBox;
	/**
	 * ComboBox that contains all the surveys ID the conducted in the selected year
	 */
	@FXML
	private ComboBox<Integer> surveyIdQuestionsComboBox;
	/**
	 * vbox that contains label and the surveyIdQuestionsComboBox 
	 */
	@FXML
	private VBox vb;
	/**
	 * yearIdQuestionsHashMap that contains all the years that surveys have been conducted in them as a key and an internal hashMap to contain the (surveys) as key and all the questions of the survey as a value 
	 */
	private HashMap<String, HashMap<Integer, SurveyQuestion>> yearIdQuestionsHashMap;
	
	/**
	 * initialize function is used to create many listeners and initialize many things
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		yearIdQuestionsHashMap = (HashMap<String, HashMap<Integer, SurveyQuestion>>) MainController.getMyClient().send(MessageType.GET, "survey/date_survey", null);
		surveyYearComboBox.getItems().addAll(yearIdQuestionsHashMap.keySet());
		/**
		 * This Listener for the surveyYearComboBox is added to make the surveyIdQuestionsComboBox visible and to show the suitable surveys in it
		 */
		surveyYearComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				vb.setVisible(true);
				surveyIdQuestionsComboBox.getItems().clear();
				surveyIdQuestionsComboBox.getItems()
						.addAll(yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).keySet());
				answersBarChart.getData().clear();
				sendReportButton.setDisable(true);
				clearQuestions();
			}
		});
		/**
		 * This listener for the surveyIdQuestionsComboBox is used in order to show the expert the averages of each question in specific survey and year and set the suitable questions in the text areas
		 */
		surveyIdQuestionsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				try {
					answersBarChart.getData().clear();
					setQuestions();
					SurveySumAnswers surveySumAnswers = (SurveySumAnswers) MainController.getMyClient()
							.send(MessageType.GET, "survey/by/date_survey && id_question_average/"
									+ surveyIdQuestionsComboBox.getValue() + "/" + surveyYearComboBox.getValue(), null);
					XYChart.Series<String, Double> set1 = new XYChart.Series<>();
					for (int i = 0; i < 6; i++)
						set1.getData().add(new XYChart.Data(i + 1 + "", surveySumAnswers.getAvgAnswers().get(i)));
					answersBarChart.getData().addAll(set1);
					sendReportButton.setDisable(false);
				} catch (NullPointerException e) {
				}
			}

		});
	}
	/**
	 * This function is used in order to clear the text fields of the questions
	 */
	private void clearQuestions() {
		q1T.setText("");
		q2T.setText("");
		q3T.setText("");
		q4T.setText("");
		q5T.setText("");
		q6T.setText("");

	}
	/**
	 * This function is used in order to set the questions in the text areas
	 */

	private void setQuestions() {
		q1T.setText("1. " +yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue())
				.getQuestion().get(0));
		q2T.setText("2. " +yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue())
				.getQuestion().get(1));
		q3T.setText("3. " +yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue())
				.getQuestion().get(2));
		q4T.setText("4. " +yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue())
				.getQuestion().get(3));
		q5T.setText("5. " + yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue())
				.getQuestion().get(4));
		q6T.setText("6. " +yearIdQuestionsHashMap.get(surveyYearComboBox.getValue()).get(surveyIdQuestionsComboBox.getValue())
				.getQuestion().get(5));
	}
	
	/**
	 * This function will start when the expert click on send report button and send the report to the server
	 */

    @FXML
    void onSendReport() {
    	if(pdfBytes==null)
    		return;
    	SurveyReport rep=new SurveyReport(surveyYearComboBox.getValue(), surveyIdQuestionsComboBox.getValue(), pdfBytes);
    	boolean success=(boolean)MainController.getMyClient().send(MessageType.POST,"report",rep);
    	if(!success) {
    		Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Failed");
			errorAlert.setContentText("Report sending failed!");
			errorAlert.showAndWait();
    		return;
    	}
    	clearPage();
    	
    }
    /**
     * This function will start when the expert click on the upload file area to upload file
     */

    @FXML
    void onUploadPressed() {
    	FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
		File f = fc.showOpenDialog(null);
		if(f==null)
			return;
		
		try {
			pdfBytes=Files.readAllBytes(Paths.get(f.getPath()));
			uploadVBox.setVisible(false);
			fileNameLbl.setText(f.getName());
			fileNameLbl.setVisible(true);
			sendReportButton.setDisable(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * This function will start when the expert send the report to clear the page and returns it to the first viewed
     */

    private void clearPage() {
    	pdfBytes=null;
    	uploadVBox.setVisible(true);
    	fileNameLbl.setVisible(false);
    	sendReportButton.setDisable(true);
    	
    	
    }
}
