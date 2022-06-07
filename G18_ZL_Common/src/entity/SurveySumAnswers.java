package entity;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * this class to the answers of the survey 
 * @author saher
 *
 */
public class SurveySumAnswers  implements Serializable {

	private static final long serialVersionUID = 8505837124591041487L;
	/**
	 * question number
	 */
	private int questionNumber;
	/**
	 * int parameter to sum the answers for every question
	 */
	private int sum;
	/**
	 * ArrayList to save the average for the answers to every question on the survey 
	 */
	private ArrayList<Double> avgAnswersList = new ArrayList<>();
	
	
	
	
	/**
	 * getters and setters 
	 */
	
	
	
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
