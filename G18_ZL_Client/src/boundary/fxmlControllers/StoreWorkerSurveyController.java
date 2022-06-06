package boundary.fxmlControllers;

import java.awt.event.ActionListener;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;


import boundary.ClientView;
import control.MainController;
import entity.Complaint;
import entity.Survey;
import entity.SurveyQuestion;
import entity.MyMessage.MessageType;
import entity.Store;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
/** 
 * This class overrides initialize method 
 * @author Aziz Hamed
 * This class is used in order to give the service worker entering survey functionality
 */
public class StoreWorkerSurveyController implements Initializable {
	
	/**
	 * Enter survey Button
	 */
	@FXML
	private Button enterSurveyButton;
	
	/**
	 * Text area to show the first question
	 */

	@FXML
	private TextArea question1TA;
	
	/**
	 * Question 1 Radio Button group
	 */

	@FXML
	private ToggleGroup question1TG;
	
	/**
	 * Text area to show the second question
	 */

	@FXML
	private TextArea question2TA;
	
	/**
	 * Question 2 Radio Button group
	 */

	@FXML
	private ToggleGroup question2TG;
	
	/**
	 * Text area to show the third question
	 */

	@FXML
	private TextArea question3TA;
	
	/**
	 * Question 3 Radio Button group
	 */

	@FXML
	private ToggleGroup question3TG;
	
	/**
	 * Text area to show the forth question
	 */

	@FXML
	private TextArea question4TA;
	
	/**
	 * Question 4 Radio Button group
	 */

	@FXML
	private ToggleGroup question4TG;
	
	/**
	 * Text area to show the fifth question
	 */

	@FXML
	private TextArea question5TA;
	
	/**
	 * Question 5 Radio Button group
	 */

	@FXML
	private ToggleGroup question5TG;
	
	/**
	 * Text area to show the sixth question
	 */

	@FXML
	private TextArea question6TA;
	
	/**
	 * Question 6 Radio Button group
	 */
	
	@FXML
	private ToggleGroup question6TG;
	
	/**
	 * ComboBox that contains all the branches
	 */
	@FXML
	private ComboBox<Store> storeComboBox;
	
	/**
	 * ComboBox that contains all the surveys
	 */
	 @FXML
	 private ComboBox<Integer> surviesComB;
	 /**
	  * Label that contain branch name
	  */
	 @FXML
	   private Label storeL;
	 
	 /**
	  * survey object to enter to the data base
	  */
	private Survey survey;
	/**
	 * boolean array with length 6, "i" index contains true if the "i" question is answered and false if not
	 */
	private boolean[] isAnsweredQuestion = new boolean[6];
	/**
	 * isSelectedSurvey = true if survey has been selected and false if not
	 */
	private boolean isSelectedSurvey;
//	private String[] questions = { "How much the service was understood to you from 1 - 10 ?",
//			"Rate how much the service was comfort from 1 - 10 ", "Rate the workers treatment from 1 - 10",
//			"How much the service was complicated in your opinion from 1 - 10 ?\n",
//			"Rate the design of the service from 1 - 10 ",
//			"How much are you satisfied from the flowers that you bought from 1 - 10 ?\n" };
	private ArrayList<SurveyQuestion> surviesList;
	/**
	 * initialize function to create many listeners and initialize many things
	 */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		storeL.setText("");//TODO get worker
		survey = new Survey();
		surviesList = (ArrayList<SurveyQuestion>) MainController.getMyClient().send(MessageType.GET,"questions/all", null);
		clearSelectedItemInSurviesComboBox();
		storeComboBox.getItems().clear();
		storeComboBox.getItems().addAll(Store.values());
		for (int i = 0; i < 6; i++)
			isAnsweredQuestion[i] = false;
		enableEnterAnswerButton();
		/**
		 * This Listener is used to tell the system that store has been selected
		 */
		storeComboBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<Store>(){

			@Override
			public void changed(ObservableValue<? extends Store> observable, Store oldValue, Store newValue) {
				enableEnterAnswerButton();
			}
		});
		/**
		 * This Listener is used to tell the system that survey has been selected and to show the suitable questions
		 */
		surviesComB.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<Integer>(){

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				try {
					setQuestions(surviesComB.getValue());
					isSelectedSurvey = true;
					clearSelectedToggles();
				}catch(NullPointerException e) {}
			}

		});
	}
	
	/**
	 * This function is used to clear selection from surveys comboBox
	 */

	private void clearSelectedItemInSurviesComboBox() {
		surviesComB.getItems().clear();
		for(int i=0 ; i<surviesList.size() ; i++)
			surviesComB.getItems().add(i);
	}
	/**
	 * This function is used in order to clear questions text areas
	 */

	private void clearTextFields() {
		question1TA.setText("");
		question2TA.setText("");
		question3TA.setText("");
		question4TA.setText("");
		question5TA.setText("");
		question6TA.setText("");
	}
	/**
	 * This function is used to show the suitable questions in the text areas
	 * @param surveyNumber contains the selected survey 
	 */

	private void setQuestions(int surveyNumber) {
		question1TA.setText(surviesList.get(surveyNumber).getQuestion().get(0));
		question2TA.setText(surviesList.get(surveyNumber).getQuestion().get(1));
		question3TA.setText(surviesList.get(surveyNumber).getQuestion().get(2));
		question4TA.setText(surviesList.get(surveyNumber).getQuestion().get(3));
		question5TA.setText(surviesList.get(surveyNumber).getQuestion().get(4));
		question6TA.setText(surviesList.get(surveyNumber).getQuestion().get(5));
	}
	
	/**
	 * This function will start when the expert click on Enter Answer button and send the answers to the server 
	 * @param event 
	 * @throws IOException is thrown if dialog box load fail
	 */

	public void onEnterAnswers(ActionEvent event) throws IOException {
		setAnswers();
		survey.setIdQuestion(surviesComB.getValue() + 1);
		survey.setDateSurvey(MainController.currentTime());
		survey.setIdStore(storeComboBox.getValue().ordinal());
		SurveyQueryFromDB(MessageType.POST,survey);
		Dialog<ButtonType> dialog = LoadDialogPane();
		Optional<ButtonType> clickedButton = dialog.showAndWait();
		dialog.close();
		initialize(null, null);
		isSelectedSurvey = false;
		clearTextFields();
		clearSelectedItemInSurviesComboBox();
		clearSelectedToggles();
	}
	
	/**
	 * This function is used to clear the selected Radio Buttons after Enter Answer button click
	 */
	private void clearSelectedToggles() {
		question1TG.getSelectedToggle().setSelected(false);
		question2TG.getSelectedToggle().setSelected(false);
		question3TG.getSelectedToggle().setSelected(false);
		question4TG.getSelectedToggle().setSelected(false);
		question5TG.getSelectedToggle().setSelected(false);
		question6TG.getSelectedToggle().setSelected(false);
		for(int i=0 ; i<6 ; i++)
			isAnsweredQuestion[i] = false;
	}
	/**
	 * This function is used to load the dialog pane that shows that the Success of entering answers
	 * @return dialog Box
	 * @throws IOException thrown if dialog box load fail
	 */

	private Dialog<ButtonType> LoadDialogPane() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(
				ClientView.class.getResource("/boundary/fxmls/survey-dialog-box-view.fxml"));
		DialogPane pane = fxmlLoader.load();
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setDialogPane(pane);
		return dialog;
	}
	
	/**
	 * This function is used in order to set the answers in survey object
	 */

	private void setAnswers() {
		ArrayList<Integer> answersList = new ArrayList<>();
		answersList.add(Integer.parseInt(((RadioButton) question1TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question2TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question3TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question4TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question5TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question6TG.getSelectedToggle()).getText()));
		//surviesList.get(surviesComB.getValue()).setAnswers(answersList);
		survey.setAnswers(answersList);
	}
	
	/**
	 * This function is used to ask for some specific query from the data base
	 * @param messageType what we want to do (UPDATE, POST , GET)
	 * @param survey object with answers
	 */

	private void SurveyQueryFromDB(MessageType messageType,Survey survey) {
		MainController.getMyClient().send(messageType, "survey/", survey);
	}
	
	/**
	 * This function is started when the expert select radio button from question1TG
	 * @param event
	 */

	public void onSelectedToggleGroup1(ActionEvent event) {
		if(!selectToggle(0))
			question1TG.getSelectedToggle().setSelected(false);
	}
	
	/**
	 * This function is started when the expert select radio button from question2TG
	 * @param event
	 */

	public void onSelectedToggleGroup2(ActionEvent event) {
		if(!selectToggle(1))
			question2TG.getSelectedToggle().setSelected(false);
	}
	
	/**
	 * This function is started when the expert select radio button from question3TG
	 * @param event
	 */

	public void onSelectedToggleGroup3(ActionEvent event) {
		if(!selectToggle(2))
			question3TG.getSelectedToggle().setSelected(false);
	}
	
	/**
	 * This function is started when the expert select radio button from question4TG
	 * @param event
	 */

	public void onSelectedToggleGroup4(ActionEvent event) {
		if(!selectToggle(3))
			question4TG.getSelectedToggle().setSelected(false);
	}
	
	/**
	 * This function is started when the expert select radio button from question5TG
	 * @param event
	 */

	public void onSelectedToggleGroup5(ActionEvent event) {
		if(!selectToggle(4))
			question5TG.getSelectedToggle().setSelected(false);
	}
	
	/**
	 * This function is started when the expert select radio button from question6TG
	 * @param event
	 */

	public void onSelectedToggleGroup6(ActionEvent event) {
		if(!selectToggle(5))
			question6TG.getSelectedToggle().setSelected(false);
	}
	
	/**
	 * This function update the selected Radio button array in the toggleGroup index to true (Question "toggleGroup" is Answered) if survey selected 
	 * @param toggleGroup
	 * @return
	 */
	
	private boolean selectToggle(int toggleGroup) {
		if(!isSelectedSurvey)
			return false;
		else {
		isAnsweredQuestion[toggleGroup] = true;
		enableEnterAnswerButton();
		}
		return true;
	}
	
	/**
	 * This function is used to decide to enable or disable Enter Answers button 
	 */

	public void enableEnterAnswerButton() {
		enterSurveyButton.setDisable(!isAnsweredQuestion[0] || !isAnsweredQuestion[1] || !isAnsweredQuestion[2]
				|| !isAnsweredQuestion[3] || !isAnsweredQuestion[4] || !isAnsweredQuestion[5]);
	}

}
