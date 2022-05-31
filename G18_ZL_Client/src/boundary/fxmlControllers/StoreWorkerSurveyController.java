package boundary.fxmlControllers;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import boundary.ClientView;
import control.MainController;
import entity.Complaint;
import entity.Survey;
import entity.MyMessage.MessageType;
import entity.Survey.SurveyQuestion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class StoreWorkerSurveyController implements Initializable {
	    @FXML
	    private Button enterSurveyButton;

	    @FXML
	    private TextArea question10TA;

	    @FXML
	    private ToggleGroup question10TG;

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
	    private TextArea question7TA;

	    @FXML
	    private ToggleGroup question7TG;

	    @FXML
	    private TextArea question8TA;

	    @FXML
	    private ToggleGroup question8TG;

	    @FXML
	    private TextArea question9TA;

	    @FXML
	    private ToggleGroup question9TG;
        private Survey survey;
        private SurveyQuestion [] surveyQuestions = new SurveyQuestion[10];
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		enterSurveyButton.setStyle("  -fx-background-color:\r\n"
				+ "        linear-gradient(#f0ff35, #a9ff00),\r\n"
				+ "        radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);\r\n"
				+ "    -fx-background-radius: 6, 5;\r\n"
				+ "    -fx-background-insets: 0, 1;\r\n"
				+ "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\r\n"
				+ "    -fx-text-fill: #395306;");
		//senD to server
		 //SurveyQueryFromDB(MessageType.GET, null);
		//for(int i=0 ; i<10 ; i++)
			//surveyQuestions[i] = survey.getQuestions().iterator().next();
 		//setQuestions();
	}
	private void setQuestions() {
		question1TA.setText(surveyQuestions[0].getQuestion());
		question2TA.setText(surveyQuestions[1].getQuestion());
		question3TA.setText(surveyQuestions[2].getQuestion());
		question4TA.setText(surveyQuestions[3].getQuestion());
		question5TA.setText(surveyQuestions[4].getQuestion());
		question6TA.setText(surveyQuestions[5].getQuestion());
		question7TA.setText(surveyQuestions[6].getQuestion());
		question8TA.setText(surveyQuestions[7].getQuestion());
		question9TA.setText(surveyQuestions[8].getQuestion());
		question10TA.setText(surveyQuestions[9].getQuestion());
	}
	public void onEnterAnswers(ActionEvent event) throws IOException {
			//setAnswers();
			//SurveyQueryFromDB(MessageType.POST, survey);
			Dialog<ButtonType> dialog = LoadDialogPane();
			Optional<ButtonType> clickedButton = dialog.showAndWait();
			if (clickedButton.get() == ButtonType.NO) {
				dialog.close();
				((Node)event.getSource()).getScene().getWindow().hide();
				} else {
				dialog.close();
				initialize(null, null);
				clearSelectedToggles();
				}
	}
	private void clearSelectedToggles() {
		if(question1TG.getSelectedToggle()!=null)
		question1TG.getSelectedToggle().setSelected(false);
		if(question2TG.getSelectedToggle()!=null)
		question2TG.getSelectedToggle().setSelected(false);
		if(question3TG.getSelectedToggle()!=null)
		question3TG.getSelectedToggle().setSelected(false);
		if(question4TG.getSelectedToggle()!=null)
		question4TG.getSelectedToggle().setSelected(false);
		if(question5TG.getSelectedToggle()!=null)
		question5TG.getSelectedToggle().setSelected(false);
		if(question6TG.getSelectedToggle()!=null)
		question6TG.getSelectedToggle().setSelected(false);
		if(question7TG.getSelectedToggle()!=null)
		question7TG.getSelectedToggle().setSelected(false);
		if(question8TG.getSelectedToggle()!=null)
		question8TG.getSelectedToggle().setSelected(false);
		if(question9TG.getSelectedToggle()!=null)
		question9TG.getSelectedToggle().setSelected(false);
		if(question10TG.getSelectedToggle()!=null)
		question10TG.getSelectedToggle().setSelected(false);
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
		if(question1TG.getSelectedToggle()!=null)
		surveyQuestions[0].setAnswer(Integer.parseInt(question1TG.getSelectedToggle().toString()));
		if(question2TG.getSelectedToggle()!=null)
		surveyQuestions[1].setAnswer(Integer.parseInt(question2TG.getSelectedToggle().toString()));
		if(question3TG.getSelectedToggle()!=null)
		surveyQuestions[2].setAnswer(Integer.parseInt(question3TG.getSelectedToggle().toString()));
		if(question4TG.getSelectedToggle()!=null)
		surveyQuestions[3].setAnswer(Integer.parseInt(question4TG.getSelectedToggle().toString()));
		if(question5TG.getSelectedToggle()!=null)
		surveyQuestions[4].setAnswer(Integer.parseInt(question5TG.getSelectedToggle().toString()));
		if(question6TG.getSelectedToggle()!=null)
		surveyQuestions[5].setAnswer(Integer.parseInt(question6TG.getSelectedToggle().toString()));
		if(question7TG.getSelectedToggle()!=null)
		surveyQuestions[6].setAnswer(Integer.parseInt(question7TG.getSelectedToggle().toString()));
		if(question8TG.getSelectedToggle()!=null)
		surveyQuestions[7].setAnswer(Integer.parseInt(question8TG.getSelectedToggle().toString()));
		if(question9TG.getSelectedToggle()!=null)
		surveyQuestions[8].setAnswer(Integer.parseInt(question9TG.getSelectedToggle().toString()));
		if(question10TG.getSelectedToggle()!=null)
		surveyQuestions[9].setAnswer(Integer.parseInt(question10TG.getSelectedToggle().toString()));
	}
	private ArrayList<Complaint> SurveyQueryFromDB(MessageType messageType, Survey survey){
		return (ArrayList<Complaint>) MainController.getMyClient().send(messageType, "Survey/"+ ClientConsoleController.getCustomer().getIdCustomer(),survey);
	}
    public Survey getSurvey() {
    	return survey;
    }
    public void setSurvey(Survey survey) {
    	this.survey = survey;
    }
    }
