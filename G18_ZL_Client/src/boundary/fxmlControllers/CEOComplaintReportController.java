package boundary.fxmlControllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.Quarters;
import entity.Store;
import entity.SurveyQuestion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

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

public class CEOComplaintReportController implements Initializable {
	
	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */

    @FXML
    private Button buttonComplaints;

    @FXML
    private Button buttonQuestions;

    @FXML
    private ChoiceBox<String> choiceBoxQuarters;

    @FXML
    private ChoiceBox<String> choiceBoxSurveyID;

    @FXML
    private ChoiceBox<String> choiceBoxYear;
    
    @FXML
    private BarChart<String, Number> barChartComplaints;

    @FXML
    private PieChart pieChart1;

    @FXML
    private PieChart pieChart2;

    @FXML
    private PieChart pieChart3;

    @FXML
    private PieChart pieChart4;

    @FXML
    private PieChart pieChart5;

    @FXML
    private PieChart pieChart6;

    @FXML
    private Text textStoreName;
    
    @FXML
    private Text error1;

    @FXML
    private Text error2;
    
    @FXML
    private Text textQ1;

    @FXML
    private Text textQ2;

    @FXML
    private Text textQ3;

    @FXML
    private Text textQ4;

    @FXML
    private Text textQ5;

    @FXML
    private Text textQ6;
	
    /* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    // data to add to bar chart
    private XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
    
    // data for pie chart of question 1
    private ObservableList<Data> pc1Data = FXCollections.observableArrayList();
    
    // data for pie chart of question 2
    private ObservableList<Data> pc2Data = FXCollections.observableArrayList();

    // data for pie chart of question 3
    private ObservableList<Data> pc3Data = FXCollections.observableArrayList();
    
    // data for pie chart of question 4
    private ObservableList<Data> pc4Data = FXCollections.observableArrayList();
    
    // data for pie chart of question 5
    private ObservableList<Data> pc5Data = FXCollections.observableArrayList();
    
    // data for pie chart of question 6
    private ObservableList<Data> pc6Data = FXCollections.observableArrayList();

    
    // array list of survey IDs
    private ArrayList<SurveyQuestion> questionsArrayList;
    
    // the months in the selected quarter
    private int m1=1,m2=2,m3=3;
    
    // the selected year
    private String year;
    
    // the selected quarter
    private Quarters Q;
    private String quarter;
    
    // the selected question ID
    private String questionID;
    
    // the store of the survey
    private String store;
    
    // array list of all the answers of the survey in every question
    private ArrayList<SurveyQuestion> sq;
    
    private ArrayList<Integer> AnswersQ1,AnswersQ2,AnswersQ3,AnswersQ4,AnswersQ5,AnswersQ6;
    
    private ArrayList<String> questionsSelected;
    
    // the average answer of every question
    private double avg1,avg2,avg3,avg4,avg5,avg6;
    
    // array list of the survey's question IDs
    private ArrayList<Integer> q;
    
    
    /* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	initChoiceBoxes();
    	choiceBoxSurveyID.setOnAction(this::onQuestionIDSelection);
    	choiceBoxYear.setOnAction(this::onYearSelection);
    	choiceBoxQuarters.setOnAction(this::onQuarterSelection);
	}
    
    /* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
    
    /**
     * Method to do when questions choice box has a selection
     * 
     * @param event 
     */
    public void onQuestionIDSelection(ActionEvent event) {
    	questionID = choiceBoxSurveyID.getSelectionModel().getSelectedItem();
    	for(SurveyQuestion sq : questionsArrayList) {
    		if(questionID.equals(sq.getIdQuestion())) {
    			for(int i = 0 ; i<6 ; i++) {
    				questionsSelected.add(sq.getQuestion().get(i));
    			}
    		}
    	}
    	buttonQuestions.setDisable(false);
    }
    
    /**
     * Method to do when years choice has a selection 
     * 
     * @param event
     */
    public void onYearSelection(ActionEvent event) {
    	year = choiceBoxYear.getSelectionModel().getSelectedItem();
    	if(!choiceBoxQuarters.getSelectionModel().isEmpty()) {
    		buttonComplaints.setDisable(false);
    	}
    }
    
    /**
     * Method to do when quarters choice box has a selection
     * 
     * @param event
     */
    public void onQuarterSelection(ActionEvent event) {
    	quarter = choiceBoxQuarters.getSelectionModel().getSelectedItem();
    	if(!choiceBoxYear.getSelectionModel().isEmpty()) {
    		buttonComplaints.setDisable(false);
    	}
    }
    
    @SuppressWarnings("unchecked")
	@FXML
    /**
     * Action to do when complaints show button is pressed
     * 
     * @param event
     */
    private void buttonComplaintsAction(ActionEvent event) {
    		calcMonthsInQuarter();
    		clearBarChart();
    		year = this.choiceBoxYear.getSelectionModel().getSelectedItem();
    		ArrayList<Integer> c1 = (ArrayList<Integer>)MainController.getMyClient().send(MessageType.GET, "complaint/count/byYearMonth/"+year+"/"+m1, null);
    		ArrayList<Integer> c2 = (ArrayList<Integer>)MainController.getMyClient().send(MessageType.GET, "complaint/count/byYearMonth/"+year+"/"+m2, null);
    		ArrayList<Integer> c3 = (ArrayList<Integer>)MainController.getMyClient().send(MessageType.GET, "complaint/count/byYearMonth/"+year+"/"+m3, null);
    		initBarChartData(c1.get(0),c2.get(0),c3.get(0));
    		this.barChartComplaints.setLegendVisible(false);
    		this.barChartComplaints.setTitle("Number of complaints in every month in "+year+"-"+Q);
    		this.barChartComplaints.getData().add(series);
    		// comment -1-
    }
    
    @SuppressWarnings("unchecked")
	@FXML
    /**
     * Action to do when questions show button is pressed
     * 
     * @param event
     */
    private void buttonQuestions(ActionEvent event) {
    	
    }
    
    /* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
    
    /**
     * Method to hide the pie chart legends
     */
    private void hidePieChartsLegend() {
    	this.pieChart1.setLegendVisible(false);
    	this.pieChart2.setLegendVisible(false);
    	this.pieChart3.setLegendVisible(false);
    	this.pieChart4.setLegendVisible(false);
    	this.pieChart5.setLegendVisible(false);
    	this.pieChart6.setLegendVisible(false);
    }
    
//    /**
//     * Method to set the questions as the title of every pie chart
//     */
//    private void setPieChartTitles() {
//    	this.pieChart1.setTitle(questions.get(0));
//    	this.pieChart2.setTitle(questions.get(1));
//    	this.pieChart3.setTitle(questions.get(2));
//    	this.pieChart4.setTitle(questions.get(3));
//    	this.pieChart5.setTitle(questions.get(4));
//    	this.pieChart6.setTitle(questions.get(5));
//    }
//    
//    @SuppressWarnings("unchecked")
//	private void getQuestions() {
//    	questions.add(((ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "question/by/"+q.get(0), null)).get(0));
//    	questions.add(((ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "question/by/"+q.get(1), null)).get(0));
//    	questions.add(((ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "question/by/"+q.get(2), null)).get(0));
//    	questions.add(((ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "question/by/"+q.get(3), null)).get(0));
//    	questions.add(((ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "question/by/"+q.get(4), null)).get(0));
//    	questions.add(((ArrayList<String>)MainController.getMyClient().send(MessageType.GET, "question/by/"+q.get(5), null)).get(0));
//    	// comment -4-
//    }
//    
//    /**
//     * Method to initialize the averages
//     */
//    private void initAverages() {
//    	avg1=0;
//    	avg2=0;
//    	avg3=0;
//    	avg4=0;
//    	avg5=0;
//    	avg6=0;
//    }
//    
//    /**
//     * Method to calculate the average answer of every question
//     */
//    private void calcAverages() {
//    	initAverages();
//    	for(PieChart.Data d : pc1Data) {
//    		avg1 += d.getPieValue();
//    	}
//    }
//    
//    /**
//     * Method to set all the texts
//     */
//    private void setAllTexts() {
//    	clearAllTexts();
//    	this.textQ1.setText(avg1+"");
//    	this.textQ2.setText(avg2+"");
//    	this.textQ3.setText(avg3+"");
//    	this.textQ4.setText(avg4+"");
//    	this.textQ5.setText(avg5+"");
//    	this.textQ6.setText(avg6+"");
//    	this.textStoreName.setText(store);
//    }
//    
//    /**
//     * Method to clear all the texts
//     */
//    private void clearAllTexts() {
//    	this.textQ1.setText("N/A");
//    	this.textQ2.setText("N/A");
//    	this.textQ3.setText("N/A");
//    	this.textQ4.setText("N/A");
//    	this.textQ5.setText("N/A");
//    	this.textQ6.setText("N/A");
//    	this.textStoreName.setText("N/A");
//    }
//    
//    /**
//     * Method to set all the pie charts
//     */
//    private void setAllPieCharts() {
//    	clearPieCharts();
//    	pieChart1.getData().addAll(pc1Data);
//    	pieChart2.getData().addAll(pc2Data);
//    	pieChart3.getData().addAll(pc3Data);
//    	pieChart4.getData().addAll(pc4Data);
//    	pieChart5.getData().addAll(pc5Data);
//    	pieChart6.getData().addAll(pc6Data);
//    }
//    
//    private void initACounters() {
//    	a1=0;
//    	a2=0;
//    	a3=0;
//    	a4=0;
//    	a5=0;
//    	a6=0;
//    	a7=0;
//    	a8=0;
//    	a9=0;
//    	a10=0;
//    }
//    
//    /**
//     * Method to save all the questions id in the selected survey
//     */
//    private void findQuestions() {
//    	for(SurveyQuestion s : sq) {
//    		if(!q.contains(s.getIdQuestion())) {
//    			q.add(s.getIdQuestion());
//    		}
//    	}
//    }
//    
//    /**
//     * Method to initialize the series of question 1
//     */
//    private void initSeries1() {
//    	int counter = 0;
//    	initACounters();
//    	for(SurveyQuestion s : sq) {
//    		if(s.getIdQuestion() == q.get(0)) {
//    			counter++;
//    			switch(s.getAnswer()) {
//    			case 1:
//    				a1++;
//    				break;
//    			case 2:
//    				a2++;
//    				break;
//    			case 3:
//    				a3++;
//    				break;
//    			case 4:
//    				a4++;
//    				break;
//    			case 5:
//    				a5++;
//    				break;
//    			case 6:
//    				a6++;
//    				break;
//    			case 7:
//    				a7++;
//    				break;
//    			case 8:
//    				a8++;
//    				break;
//    			case 9:
//    				a9++;
//    				break;
//    			case 10:
//    				a10++;
//    				break;
//    			}
//    		}
//    	}
//    	pc1Data.add(new PieChart.Data("1 - ("+(a1/counter)*100+"%)", a1));
//    	pc1Data.add(new PieChart.Data("2 - ("+(a1/counter)*100+"%)", a2));
//    	pc1Data.add(new PieChart.Data("3 - ("+(a1/counter)*100+"%)", a3));
//    	pc1Data.add(new PieChart.Data("4 - ("+(a1/counter)*100+"%)", a4));
//    	pc1Data.add(new PieChart.Data("5 - ("+(a1/counter)*100+"%)", a5));
//    	pc1Data.add(new PieChart.Data("6 - ("+(a1/counter)*100+"%)", a6));
//    	pc1Data.add(new PieChart.Data("7 - ("+(a1/counter)*100+"%)", a7));
//    	pc1Data.add(new PieChart.Data("8 - ("+(a1/counter)*100+"%)", a8));
//    	pc1Data.add(new PieChart.Data("9 - ("+(a1/counter)*100+"%)", a9));
//    	pc1Data.add(new PieChart.Data("10 - ("+(a1/counter)*100+"%)", a10));
//    }
//    /**
//     * Method to initialize the series of question 2
//     */
//    private void initSeries2() {
//    	int counter = 0;
//    	initACounters();
//    	for(SurveyQuestion s : sq) {
//    		if(s.getIdQuestion() == q.get(1)) {
//    			counter++;
//    			switch(s.getAnswer()) {
//    			case 1:
//    				a1++;
//    				break;
//    			case 2:
//    				a2++;
//    				break;
//    			case 3:
//    				a3++;
//    				break;
//    			case 4:
//    				a4++;
//    				break;
//    			case 5:
//    				a5++;
//    				break;
//    			case 6:
//    				a6++;
//    				break;
//    			case 7:
//    				a7++;
//    				break;
//    			case 8:
//    				a8++;
//    				break;
//    			case 9:
//    				a9++;
//    				break;
//    			case 10:
//    				a10++;
//    				break;
//    			}
//    		}
//    	}
//    	pc2Data.add(new PieChart.Data("1 - ("+(a1/counter)*100+"%)", a1));
//    	pc2Data.add(new PieChart.Data("2 - ("+(a1/counter)*100+"%)", a2));
//    	pc2Data.add(new PieChart.Data("3 - ("+(a1/counter)*100+"%)", a3));
//    	pc2Data.add(new PieChart.Data("4 - ("+(a1/counter)*100+"%)", a4));
//    	pc2Data.add(new PieChart.Data("5 - ("+(a1/counter)*100+"%)", a5));
//    	pc2Data.add(new PieChart.Data("6 - ("+(a1/counter)*100+"%)", a6));
//    	pc2Data.add(new PieChart.Data("7 - ("+(a1/counter)*100+"%)", a7));
//    	pc2Data.add(new PieChart.Data("8 - ("+(a1/counter)*100+"%)", a8));
//    	pc2Data.add(new PieChart.Data("9 - ("+(a1/counter)*100+"%)", a9));
//    	pc2Data.add(new PieChart.Data("10 - ("+(a1/counter)*100+"%)", a10));
//    }
//    /**
//     * Method to initialize the series of question 3
//     */
//    private void initSeries3() {
//    	int counter = 0;
//    	initACounters();
//    	for(SurveyQuestion s : sq) {
//    		if(s.getIdQuestion() == q.get(2)) {
//    			counter++;
//    			switch(s.getAnswer()) {
//    			case 1:
//    				a1++;
//    				break;
//    			case 2:
//    				a2++;
//    				break;
//    			case 3:
//    				a3++;
//    				break;
//    			case 4:
//    				a4++;
//    				break;
//    			case 5:
//    				a5++;
//    				break;
//    			case 6:
//    				a6++;
//    				break;
//    			case 7:
//    				a7++;
//    				break;
//    			case 8:
//    				a8++;
//    				break;
//    			case 9:
//    				a9++;
//    				break;
//    			case 10:
//    				a10++;
//    				break;
//    			}
//    		}
//    	}
//    	pc3Data.add(new PieChart.Data("1 - ("+(a1/counter)*100+"%)", a1));
//    	pc3Data.add(new PieChart.Data("2 - ("+(a1/counter)*100+"%)", a2));
//    	pc3Data.add(new PieChart.Data("3 - ("+(a1/counter)*100+"%)", a3));
//    	pc3Data.add(new PieChart.Data("4 - ("+(a1/counter)*100+"%)", a4));
//    	pc3Data.add(new PieChart.Data("5 - ("+(a1/counter)*100+"%)", a5));
//    	pc3Data.add(new PieChart.Data("6 - ("+(a1/counter)*100+"%)", a6));
//    	pc3Data.add(new PieChart.Data("7 - ("+(a1/counter)*100+"%)", a7));
//    	pc3Data.add(new PieChart.Data("8 - ("+(a1/counter)*100+"%)", a8));
//    	pc3Data.add(new PieChart.Data("9 - ("+(a1/counter)*100+"%)", a9));
//    	pc3Data.add(new PieChart.Data("10 - ("+(a1/counter)*100+"%)", a10));
//    }
//    /**
//     * Method to initialize the series of question 4
//     */
//    private void initSeries4() {
//    	int counter = 0;
//    	initACounters();
//    	for(SurveyQuestion s : sq) {
//    		if(s.getIdQuestion() == q.get(3)) {
//    			counter++;
//    			switch(s.getAnswer()) {
//    			case 1:
//    				a1++;
//    				break;
//    			case 2:
//    				a2++;
//    				break;
//    			case 3:
//    				a3++;
//    				break;
//    			case 4:
//    				a4++;
//    				break;
//    			case 5:
//    				a5++;
//    				break;
//    			case 6:
//    				a6++;
//    				break;
//    			case 7:
//    				a7++;
//    				break;
//    			case 8:
//    				a8++;
//    				break;
//    			case 9:
//    				a9++;
//    				break;
//    			case 10:
//    				a10++;
//    				break;
//    			}
//    		}
//    	}
//    	pc4Data.add(new PieChart.Data("1 - ("+(a1/counter)*100+"%)", a1));
//    	pc4Data.add(new PieChart.Data("2 - ("+(a1/counter)*100+"%)", a2));
//    	pc4Data.add(new PieChart.Data("3 - ("+(a1/counter)*100+"%)", a3));
//    	pc4Data.add(new PieChart.Data("4 - ("+(a1/counter)*100+"%)", a4));
//    	pc4Data.add(new PieChart.Data("5 - ("+(a1/counter)*100+"%)", a5));
//    	pc4Data.add(new PieChart.Data("6 - ("+(a1/counter)*100+"%)", a6));
//    	pc4Data.add(new PieChart.Data("7 - ("+(a1/counter)*100+"%)", a7));
//    	pc4Data.add(new PieChart.Data("8 - ("+(a1/counter)*100+"%)", a8));
//    	pc4Data.add(new PieChart.Data("9 - ("+(a1/counter)*100+"%)", a9));
//    	pc4Data.add(new PieChart.Data("10 - ("+(a1/counter)*100+"%)", a10));
//    }
//    /**
//     * Method to initialize the series of question 5
//     */
//    private void initSeries5() {
//    	int counter = 0;
//    	initACounters();
//    	for(SurveyQuestion s : sq) {
//    		if(s.getIdQuestion() == q.get(4)) {
//    			counter++;
//    			switch(s.getAnswer()) {
//    			case 1:
//    				a1++;
//    				break;
//    			case 2:
//    				a2++;
//    				break;
//    			case 3:
//    				a3++;
//    				break;
//    			case 4:
//    				a4++;
//    				break;
//    			case 5:
//    				a5++;
//    				break;
//    			case 6:
//    				a6++;
//    				break;
//    			case 7:
//    				a7++;
//    				break;
//    			case 8:
//    				a8++;
//    				break;
//    			case 9:
//    				a9++;
//    				break;
//    			case 10:
//    				a10++;
//    				break;
//    			}
//    		}
//    	}
//    	pc5Data.add(new PieChart.Data("1 - ("+(a1/counter)*100+"%)", a1));
//    	pc5Data.add(new PieChart.Data("2 - ("+(a1/counter)*100+"%)", a2));
//    	pc5Data.add(new PieChart.Data("3 - ("+(a1/counter)*100+"%)", a3));
//    	pc5Data.add(new PieChart.Data("4 - ("+(a1/counter)*100+"%)", a4));
//    	pc5Data.add(new PieChart.Data("5 - ("+(a1/counter)*100+"%)", a5));
//    	pc5Data.add(new PieChart.Data("6 - ("+(a1/counter)*100+"%)", a6));
//    	pc5Data.add(new PieChart.Data("7 - ("+(a1/counter)*100+"%)", a7));
//    	pc5Data.add(new PieChart.Data("8 - ("+(a1/counter)*100+"%)", a8));
//    	pc5Data.add(new PieChart.Data("9 - ("+(a1/counter)*100+"%)", a9));
//    	pc5Data.add(new PieChart.Data("10 - ("+(a1/counter)*100+"%)", a10));
//    }
//    /**
//     * Method to initialize the series of question 6
//     */
//    private void initSeries6() {
//    	int counter = 0;
//    	initACounters();
//    	for(SurveyQuestion s : sq) {
//    		if(s.getIdQuestion() == q.get(5)) {
//    			counter++;
//    			switch(s.getAnswer()) {
//    			case 1:
//    				a1++;
//    				break;
//    			case 2:
//    				a2++;
//    				break;
//    			case 3:
//    				a3++;
//    				break;
//    			case 4:
//    				a4++;
//    				break;
//    			case 5:
//    				a5++;
//    				break;
//    			case 6:
//    				a6++;
//    				break;
//    			case 7:
//    				a7++;
//    				break;
//    			case 8:
//    				a8++;
//    				break;
//    			case 9:
//    				a9++;
//    				break;
//    			case 10:
//    				a10++;
//    				break;
//    			}
//    		}
//    	}
//    	pc6Data.add(new PieChart.Data("1 - ("+(a1/counter)*100+"%)", a1));
//    	pc6Data.add(new PieChart.Data("2 - ("+(a1/counter)*100+"%)", a2));
//    	pc6Data.add(new PieChart.Data("3 - ("+(a1/counter)*100+"%)", a3));
//    	pc6Data.add(new PieChart.Data("4 - ("+(a1/counter)*100+"%)", a4));
//    	pc6Data.add(new PieChart.Data("5 - ("+(a1/counter)*100+"%)", a5));
//    	pc6Data.add(new PieChart.Data("6 - ("+(a1/counter)*100+"%)", a6));
//    	pc6Data.add(new PieChart.Data("7 - ("+(a1/counter)*100+"%)", a7));
//    	pc6Data.add(new PieChart.Data("8 - ("+(a1/counter)*100+"%)", a8));
//    	pc6Data.add(new PieChart.Data("9 - ("+(a1/counter)*100+"%)", a9));
//    	pc6Data.add(new PieChart.Data("10 - ("+(a1/counter)*100+"%)", a10));
//    }
//    
    @SuppressWarnings("unchecked")
	private void getStoreBySurvey() {
    	questionID = this.choiceBoxSurveyID.getSelectionModel().getSelectedItem();
    	ArrayList<Integer> sID = (ArrayList<Integer>)MainController.getMyClient().send(MessageType.GET, "survey/storeid/"+questionID, null);
    	store = Store.getById(sID.get(0)).toString();
    	// comment -2-
    }
    
    /**
     * Method to find the months in the selected quarter
     */
    private void calcMonthsInQuarter() {
    	Q = Quarters.valueOf(this.choiceBoxQuarters.getSelectionModel().getSelectedItem());
    	int i = Q.ordinal();
		m1 += 3*i;
		m2 += 3*i;
		m3 += 3*i;
    }
    
    /**
     * Method to hide all the error messages
     */
    private void hideErrorMessages() {
    	this.error1.setVisible(false);
    	this.error2.setVisible(false);
    }
    
    /**
     * Method to clean the complaints Bar chart
     */
    private void clearBarChart() {
    	this.barChartComplaints.getData().clear();
    }
    
    private void clearPieCharts() {
    	this.pieChart1.getData().clear();
    	this.pieChart2.getData().clear();
    	this.pieChart3.getData().clear();
    	this.pieChart4.getData().clear();
    	this.pieChart5.getData().clear();
    	this.pieChart6.getData().clear();
    }
    
    /**
     * Method to initialize all the choice boxes
     */
    private void initChoiceBoxes() {
    	initChoiceBoxSurveyID();
    	initChoiceBoxQuarters();
    	initChoiceBoxYear();
    }
    
    /**
     * Method to initialize the survey ID choice box
     */
    @SuppressWarnings("unchecked")
	private void initChoiceBoxSurveyID() {
    	questionsArrayList = (ArrayList<SurveyQuestion>)MainController.getMyClient().send(MessageType.GET, "questions/all", null);
    	for(SurveyQuestion sq : questionsArrayList) {
    		choiceBoxSurveyID.getItems().add(sq.getIdQuestion()+"");
    	}
    }
    
    /**
     * Method to initialize the months choice box
     */
    private void initChoiceBoxQuarters() {
    	for(Quarters q : Quarters.values()) {
    		this.choiceBoxQuarters.getItems().add(q.toString());
    	}
    }
    
    /**
     * Method to initialize the years choice box
     */
    private void initChoiceBoxYear() {
    	for(int i = 2017 ; i < 2030 ; i++) {
    		this.choiceBoxYear.getItems().add(i+"");
    	}
    }
    
    
    
    /**
     * Method to add the data for bar chart in a series
     * 
     * @param x1 number of complaints in the first month of quarter
     * @param x2 number of complaints in the second month of quarter
     * @param x3 number of complaints in the third month of quarter
     */
    private void initBarChartData(int x1, int x2, int x3) {
    	series.getData().add(new XYChart.Data<String, Number>(m1+"",x1));
    	series.getData().add(new XYChart.Data<String, Number>(m2+"",x2));
    	series.getData().add(new XYChart.Data<String, Number>(m3+"",x3));
    }
    
}
