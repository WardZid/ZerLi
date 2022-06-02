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
	 @FXML
	 private ComboBox<Integer> surviesComB;
 
	private Survey survey;
	private boolean[] isAnsweredQuestion = new boolean[6];
	private boolean isSelectedStore;
	private boolean isSelectedSurvey;
	private String[] questions = { "How much the service was understood to you from 1 - 10 ?",
			"Rate how much the service was comfort from 1 - 10 ", "Rate the workers treatment from 1 - 10",
			"How much the service was complicated in your opinion from 1 - 10 ?\n",
			"Rate the design of the service from 1 - 10 ",
			"How much are you satisfied from the flowers that you bought from 1 - 10 ?\n" };
	ArrayList<Survey> surviesList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		surviesList = (ArrayList<Survey>) MainController.getMyClient().send(MessageType.GET,"question/all", null);
		clearSelectedItemInSurviesComboBox();
		storeComboBox.getItems().clear();
		storeComboBox.getItems().addAll(Store.values());
		isSelectedStore = false;
		isSelectedStore = false;
		storeComboBox.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<Store>(){

			@Override
			public void changed(ObservableValue<? extends Store> observable, Store oldValue, Store newValue) {
				isSelectedStore = true;
				enableEnterAnswerButton();
			}
		});
		for (int i = 0; i < 6; i++)
			isAnsweredQuestion[i] = false;
		enableEnterAnswerButton();
		surviesComB.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<Integer>(){

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				setQuestions(surviesComB.getValue());
				isSelectedSurvey = true;
				try {
					clearSelectedToggles();
				}catch(NullPointerException e) {}
			}

		});
	}

	private void clearSelectedItemInSurviesComboBox() {
		surviesComB.getItems().clear();
		for(int i=0 ; i<surviesList.size() ; i++)
			surviesComB.getItems().add(i);
	}

	private void clearTextFields() {
		question1TA.setText("");
		question2TA.setText("");
		question3TA.setText("");
		question4TA.setText("");
		question5TA.setText("");
		question6TA.setText("");
	}

	private void setQuestions(int surveyNumber) {
		question1TA.setText(surviesList.get(surveyNumber).getSurveyQuestion().getQuestion().get(0));
		question2TA.setText(surviesList.get(surveyNumber).getSurveyQuestion().getQuestion().get(1));
		question3TA.setText(surviesList.get(surveyNumber).getSurveyQuestion().getQuestion().get(2));
		question4TA.setText(surviesList.get(surveyNumber).getSurveyQuestion().getQuestion().get(3));
		question5TA.setText(surviesList.get(surveyNumber).getSurveyQuestion().getQuestion().get(4));
		question6TA.setText(surviesList.get(surveyNumber).getSurveyQuestion().getQuestion().get(5));
	}

	public void onEnterAnswers(ActionEvent event) throws IOException {
		setAnswers();
		surviesList.get(surviesComB.getValue()).setDateSurvey(MainController.currentTime());
		SurveyQueryFromDB(MessageType.POST, surviesList.get(surviesComB.getValue()));
		Dialog<ButtonType> dialog = LoadDialogPane();
		Optional<ButtonType> clickedButton = dialog.showAndWait();
		dialog.close();
		initialize(null, null);
		isSelectedSurvey = false;
		clearTextFields();
		clearSelectedItemInSurviesComboBox();
		clearSelectedToggles();
	}
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

	private Dialog<ButtonType> LoadDialogPane() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(
				ClientView.class.getResource("/boundary/fxmls/survey-dialog-box-view.fxml"));
		DialogPane pane = fxmlLoader.load();
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setDialogPane(pane);
		return dialog;
	}

	private void setAnswers() {
		ArrayList<Integer> answersList = new ArrayList<>();
		answersList.add(Integer.parseInt(((RadioButton) question1TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question2TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question3TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question4TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question5TG.getSelectedToggle()).getText()));
		answersList.add(Integer.parseInt(((RadioButton) question6TG.getSelectedToggle()).getText()));
		surviesList.get(surviesComB.getValue()).getSurveyQuestion().setAnswer(answersList);
	}

	private void SurveyQueryFromDB(MessageType messageType,Survey survey) {
		MainController.getMyClient().send(messageType, "survey/", survey);
	}

	public void onSelectedToggleGroup1(ActionEvent event) {
		if(!selectToggle(0))
			question1TG.getSelectedToggle().setSelected(false);
	}

	public void onSelectedToggleGroup2(ActionEvent event) {
		if(!selectToggle(1))
			question2TG.getSelectedToggle().setSelected(false);
	}

	public void onSelectedToggleGroup3(ActionEvent event) {
		if(!selectToggle(2))
			question3TG.getSelectedToggle().setSelected(false);
	}

	public void onSelectedToggleGroup4(ActionEvent event) {
		if(!selectToggle(3))
			question4TG.getSelectedToggle().setSelected(false);
	}

	public void onSelectedToggleGroup5(ActionEvent event) {
		if(!selectToggle(4))
			question5TG.getSelectedToggle().setSelected(false);
	}

	public void onSelectedToggleGroup6(ActionEvent event) {
		if(!selectToggle(5))
			question6TG.getSelectedToggle().setSelected(false);
	}
	
	private boolean selectToggle(int toggleGroup) {
		if(!isSelectedSurvey)
			return false;
		else {
		isAnsweredQuestion[toggleGroup] = true;
		enableEnterAnswerButton();
		}
		return true;
	}

	public void enableEnterAnswerButton() {
		enterSurveyButton.setDisable(!isAnsweredQuestion[0] || !isAnsweredQuestion[1] || !isAnsweredQuestion[2]
				|| !isAnsweredQuestion[3] || !isAnsweredQuestion[4] || !isAnsweredQuestion[5] ||!isSelectedStore);
	}

}
