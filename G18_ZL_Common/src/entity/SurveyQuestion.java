package entity;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * this class to the Question of the survey 
 *
 */
public class SurveyQuestion implements Serializable{
	
	private static final long serialVersionUID = -4693641523721808821L;
	/**
	 * id questions that the survey belongs to
	 */
	private int idQuestion;
	/**
	 * to save survey questions
	 */
	private ArrayList<String> questions;
	/**
	 * a normal constructor 
	 * @param idQuestion thats belong to survey 
	 * @param question question of the survey
	 * @param answer answer of the survey
	 */
	public SurveyQuestion(int idQuestion, ArrayList<String> question, ArrayList<Integer> answer) {
		this.idQuestion = idQuestion;
		this.questions = question;
	}
	/**
	 * create a questions ArrayList
	 */
	public SurveyQuestion() {
		questions = new ArrayList<String>();
		}
	
	// getters and setters
	public int getIdQuestion() {
		return idQuestion;
	}
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}
	public ArrayList<String> getQuestion() {
		return questions;
	}
	public void setQuestion(ArrayList<String> questions) {
		this.questions = questions;
	}
	@Override
	public String toString() {
		return "SurveyQuestion [idQuestion=" + idQuestion + ", question=" + questions.toString()  + "]";
	}

	
}