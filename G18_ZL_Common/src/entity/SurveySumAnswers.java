package entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveySumAnswers  implements Serializable {
	private static final long serialVersionUID = 8505837124591041487L;
	private int questionNumber;
	private int sum;
	private ArrayList<Double> avgAnswersList = new ArrayList<>();;
	
	public ArrayList<Double> getAvgAnswers() {
		return avgAnswersList;
	}
	public void setAvgAnswers(ArrayList<Double> avgAnswersList ) {
		this.avgAnswersList = avgAnswersList;
	}
	public SurveySumAnswers(int questionNumber, int sum) {
		this.questionNumber = questionNumber;
		this.sum = sum;
	}
	public SurveySumAnswers() {
	}
	public int getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}

}
