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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class StoreWorkerSurveyController implements Initializable {
	@FXML
	private Button enterSurveyButton;

	@FXML
	private TextArea question1TA;

	@FXML
	private ToggleGroup question1TG;

	@FXML
	private TextArea question2TA;

	@FXML
	private ToggleGroup question2TG;

	@FXML
	private TextArea question3TA;

	@FXML
	private ToggleGroup question3TG;

	@FXML
	private TextArea question4TA;

	@FXML
	private ToggleGroup question4TG;

	@FXML
	private TextArea question5TA;

	@FXML
	private ToggleGroup question5TG;

	@FXML
	private TextArea question6TA;

	@FXML
	private ToggleGroup question6TG;
	@FXML
	private ComboBox<Store> storeComboBox;
	private Survey survey;
	private SurveyQuestion[] surveyQuestions = new SurveyQuestion[6];
	private boolean[] isAnsweredQuestion = new boolean[6];
	private boolean isSelectedStore;
	private String[] questions = { "How much the service was understood to you from 1 - 10 ?",
			"Rate how much the service was comfort from 1 - 10 ", "Rate the workers treatment from 1 - 10",
			"How much the service was complicated in your opinion from 1 - 10 ?\n",
			"Rate the design of the service from 1 - 10 ",
			"How much are you satisfied from the flowers that you bought from 1 - 10 ?\n" };

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		storeComboBox.getItems().clear();
		storeComboBox.getItems().addAll(Store.values());
		isSelectedStore = false;
		storeComboBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<Store>(){

			@Override
			public void changed(ObservableValue<? extends Store> observable, Store oldValue, Store newValue) {
				isSelectedStore = true;
				enableEnterAnswerButton();
			}
		});
		for (int i = 0; i < 6; i++) {
			surveyQuestions[i] = new SurveyQuestion(1, null, 0);
			surveyQuestions[i].setQuestion(questions[i]);
		}
		for (int i = 0; i < 6; i++)
			isAnsweredQuestion[i] = false;
		enableEnterAnswerButton();
		setQuestions();
	}

	private void setQuestions() {
		question1TA.setText(surveyQuestions[0].getQuestion());
		question2TA.setText(surveyQuestions[1].getQuestion());
		question3TA.setText(surveyQuestions[2].getQuestion());
		question4TA.setText(surveyQuestions[3].getQuestion());
		question5TA.setText(surveyQuestions[4].getQuestion());
		question6TA.setText(surveyQuestions[5].getQuestion());
	}

	public void onEnterAnswers(ActionEvent event) throws IOException {
		setAnswers();
		initializeSurvey();
		SurveyQueryFromDB(MessageType.POST, survey);
		Dialog<ButtonType> dialog = LoadDialogPane();
		Optional<ButtonType> clickedButton = dialog.showAndWait();
		dialog.close();
		initialize(null, null);
		clearSelectedToggles();
	}

	private void initializeSurvey() {
		HashSet<SurveyQuestion> sq = new HashSet<>();
		for (int i = 0; i < 6; i++)
			sq.add(surveyQuestions[i]);
		survey = new Survey(MainController.currentTime(), storeComboBox.getValue().ordinal());
		survey.setQuestions(sq);
	}

	private void clearSelectedToggles() {
		question1TG.getSelectedToggle().setSelected(false);
		question2TG.getSelectedToggle().setSelected(false);
		question3TG.getSelectedToggle().setSelected(false);
		question4TG.getSelectedToggle().setSelected(false);
		question5TG.getSelectedToggle().setSelected(false);
		question6TG.getSelectedToggle().setSelected(false);
	}

	private Dialog<ButtonType> LoadDialogPane() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(
				ClientView.class.getResource("/boundary/fxmls/survey-dialog-box-view.fxml"));
		DialogPane pane = fxmlLoader.load();
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setDialogPane(pane);
		return dialog;
	}

	private void setAnswers() {
		surveyQuestions[0].setAnswer(Integer.parseInt(((RadioButton) question1TG.getSelectedToggle()).getText()));
		surveyQuestions[1].setAnswer(Integer.parseInt(((RadioButton) question2TG.getSelectedToggle()).getText()));
		surveyQuestions[2].setAnswer(Integer.parseInt(((RadioButton) question3TG.getSelectedToggle()).getText()));
		surveyQuestions[3].setAnswer(Integer.parseInt(((RadioButton) question4TG.getSelectedToggle()).getText()));
		surveyQuestions[4].setAnswer(Integer.parseInt(((RadioButton) question5TG.getSelectedToggle()).getText()));
		surveyQuestions[5].setAnswer(Integer.parseInt(((RadioButton) question6TG.getSelectedToggle()).getText()));
	}

	private void SurveyQueryFromDB(MessageType messageType, Survey survey) {
		MainController.getMyClient().send(messageType, "survey/", survey);
	}

	public void onSelectedToggleGroup1(ActionEvent event) {
		isAnsweredQuestion[0] = true;
		enableEnterAnswerButton();
	}

	public void onSelectedToggleGroup2(ActionEvent event) {
		isAnsweredQuestion[1] = true;
		enableEnterAnswerButton();
	}

	public void onSelectedToggleGroup3(ActionEvent event) {
		isAnsweredQuestion[2] = true;
		enableEnterAnswerButton();
	}

	public void onSelectedToggleGroup4(ActionEvent event) {
		isAnsweredQuestion[3] = true;
		enableEnterAnswerButton();
	}

	public void onSelectedToggleGroup5(ActionEvent event) {
		isAnsweredQuestion[4] = true;
		enableEnterAnswerButton();
	}

	public void onSelectedToggleGroup6(ActionEvent event) {
		isAnsweredQuestion[5] = true;
		enableEnterAnswerButton();
	}

	public void enableEnterAnswerButton() {
		enterSurveyButton.setDisable(!isAnsweredQuestion[0] || !isAnsweredQuestion[1] || !isAnsweredQuestion[2]
				|| !isAnsweredQuestion[3] || !isAnsweredQuestion[4] || !isAnsweredQuestion[5] ||!isSelectedStore);
	}

}
