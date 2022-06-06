package boundary.fxmlControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.Quarters;
import entity.SurveyReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.DirectoryChooser;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*

											1.
"SELECT Count(id_complaint) as count"+
"FROM complaint C"+
"WHERE Year(date_complaint) = "+year+" AND Month(date_complaint) = "+month

GET -> complaint/count/byYearMonth/year/m1 and m2 and m3
	
											2.
"SELECT id_store"+
"FROM survey s"+
"WHERE s.id_survey = "+surveyID

GET -> survey/storeid/surveyID		

											3.
"SELECT *"+
"FROM survey_question SQ"+
"WHERE SQ.id_survey = "+surveyID

GET -> survey_question/by/id_survey/surveyID											
			
											4.
"SELECT question"+
"FROM question Q"+
"WHERE Q.id_question = "+questionID

GET -> question/by/questionID
											
* */

public class CEOComplaintReportsController implements Initializable {

	/* ------------------------------------------------ */
	/* \/ FXML Variables \/ */
	/* ------------------------------------------------ */

	/**
	 * barchart of the complaints in the selected quarter
	 */
	@FXML
	private BarChart<String, Integer> barChartComplaints;

	/**
	 * the button to download the report file
	 */
	@FXML
	private Button buttonDownload;

	/**
	 * the button to show the data on the bar chart
	 */
	@FXML
	private Button buttonShow;

	/**
	 * the choice box of quarters
	 */
	@FXML
	private ChoiceBox<String> choiceBoxQuarterComplaint;

	/**
	 * the choice box of question IDs
	 */
	@FXML
	private ComboBox<String> choiceBoxSurveyPDF;

	/**
	 * the choice box of years of the complaints
	 */
	@FXML
	private ChoiceBox<String> choiceBoxYearComplaint;

	/**
	 * the choice box of the years of the surveys
	 */
	@FXML
	private ComboBox<String> choiceBoxYearPDF;

	/* ------------------------------------------------ */
	/* \/ Help Variables \/ */
	/* ------------------------------------------------ */

	// the selected years
	private String yearComplaint, yearPDF;

	// the selected survey ID
	private String selectedSurveyID;

	// the first month in the selected quarter
	private int firstMonthInQuarter = 1;

	/* XYChart series to insert values in the line chart */
	XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();

	/* ------------------------------------------------ */
	/* \/ initialize function \/ */
	/* ------------------------------------------------ */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// choiceBoxSurveyPDF.setDisable(true);
		initChoiceBoxes();
		choiceBoxYearComplaint.setOnAction(this::onYearComplaintSelection);
		choiceBoxQuarterComplaint.setOnAction(this::onQuarterComplaintSelection);
		choiceBoxYearPDF.setOnAction(this::onYearPDFSelection);
		choiceBoxSurveyPDF.setOnAction(this::onSurveyIDSelection);
		barChartComplaints.setLegendVisible(false);
	}

	/* ------------------------------------------------ */
	/* \/ Action Methods \/ */
	/* ------------------------------------------------ */

	/**
	 * Action to do when download button is pressed
	 * 
	 * @param event
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void onDownloadPressed(ActionEvent event) throws FileNotFoundException {
		if (choiceBoxYearPDF.getSelectionModel().isEmpty() || choiceBoxSurveyPDF.getSelectionModel().isEmpty()) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You must choose Year and Question ID!");
			errorAlert.showAndWait();
		} else {
			ArrayList<SurveyReport> report = (ArrayList<SurveyReport>) MainController.getMyClient().send(MessageType.GET, "report/" + yearPDF + "/" + selectedSurveyID, null);
			if (report.size() == 0) {
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("There is no report for the selected year and question ID !");
				errorAlert.showAndWait();
			} else {
				DirectoryChooser dirChooser = new DirectoryChooser();
				File chosenDir = dirChooser.showDialog(null);
				if(chosenDir != null) {					
					File newFile = new File(chosenDir.getAbsolutePath()+"\\"+yearPDF+"_"+selectedSurveyID+".pdf");
					FileOutputStream fos = new FileOutputStream(newFile);
					try {
						fos.write(report.get(0).getReportBytes());
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Alert errorAlert = new Alert(AlertType.INFORMATION);
					errorAlert.setHeaderText(null);
					errorAlert.setContentText(yearPDF+"_"+selectedSurveyID+".pdf"+" was downloaded successfully to:\n"+chosenDir.getAbsolutePath());
					errorAlert.showAndWait();
				}
			}
		}
	}

	/**
	 * Action to do when a survey is selected
	 * 
	 * @param event
	 */
	public void onSurveyIDSelection(ActionEvent event) {
		selectedSurveyID = choiceBoxSurveyPDF.getSelectionModel().getSelectedItem();
	}

	/**
	 * Action to do when a year for PDF is selected
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void onYearPDFSelection(ActionEvent event) {
		// choiceBoxSurveyPDF.setDisable(false);
		yearPDF = choiceBoxYearPDF.getSelectionModel().getSelectedItem();
		ArrayList<String> ids = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET,
				"survey/idByYear/" + yearPDF, null);
		choiceBoxSurveyPDF.getItems().clear();
		choiceBoxSurveyPDF.getItems().addAll(ids);
	}

	/**
	 * Action to do when years complaint choice box has a selection
	 * 
	 * @param event
	 */
	public void onYearComplaintSelection(ActionEvent event) {
		yearComplaint = choiceBoxYearComplaint.getSelectionModel().getSelectedItem();
		if (!choiceBoxQuarterComplaint.getSelectionModel().isEmpty()) {
			buttonShow.setDisable(false);
		}
	}

	/**
	 * Action to do when quarter is selected
	 * 
	 * @param event
	 */
	public void onQuarterComplaintSelection(ActionEvent event) {
		Quarters q = Quarters.valueOf(choiceBoxQuarterComplaint.getSelectionModel().getSelectedItem());
		firstMonthInQuarter += 3 * q.ordinal();

		if (!choiceBoxYearComplaint.getSelectionModel().isEmpty()) {
			buttonShow.setDisable(false);
		}
	}

	/**
	 * Action to do when show is pressed
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void onShowPressed(ActionEvent event) {
		if (choiceBoxQuarterComplaint.getSelectionModel().isEmpty()
				|| choiceBoxYearComplaint.getSelectionModel().isEmpty()) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(null);
			errorAlert.setContentText("You must choose Year and Quarter!");
			errorAlert.showAndWait();
		} else {
			barChartComplaints.getData().clear();
			series = new XYChart.Series<String, Integer>();
			ArrayList<Integer> countOfComplaintsInQuarter = (ArrayList<Integer>) MainController.getMyClient().send(
					MessageType.GET, "complaint/count/inQuarter/" + yearComplaint + "/" + firstMonthInQuarter, null);
			for (int i = 0; i < 3; i++) {
				series.getData().add(new XYChart.Data<String, Integer>(firstMonthInQuarter + i + "",
						countOfComplaintsInQuarter.get(i)));
			}
			firstMonthInQuarter = 1;
			barChartComplaints.getData().add(series);
		}
	}

	/* ------------------------------------------------ */
	/* \/ Help Methods \/ */
	/* ------------------------------------------------ */

	/**
	 * initialize all choice boxes
	 */
	private void initChoiceBoxes() {
		initSurveyChoiceBoxes();
		initComplaintsChoiceBoxes();
	}

	/**
	 * initialize choice boxes for downloading PDF report
	 */
	@SuppressWarnings("unchecked")
	private void initSurveyChoiceBoxes() {
		ArrayList<String> yearsOfSurveys = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET,
				"survey/years", null);
		if (yearsOfSurveys.size() == 0)
			return;
		choiceBoxYearPDF.getItems().addAll(yearsOfSurveys);

	}

	/**
	 * initialize all choice boxes for complaints chart
	 */
	@SuppressWarnings("unchecked")
	private void initComplaintsChoiceBoxes() {
		ArrayList<String> yearsToChoose = (ArrayList<String>) MainController.getMyClient().send(MessageType.GET,
				"complaint/years", null);
		choiceBoxYearComplaint.getItems().addAll(yearsToChoose);

		for (Quarters q : Quarters.values()) {
			choiceBoxQuarterComplaint.getItems().add(q.toString());
		}
	}

}
