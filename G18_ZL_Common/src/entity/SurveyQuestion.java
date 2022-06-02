package entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyQuestion implements Serializable{
	
	private static final long serialVersionUID = -4693641523721808821L;
	
	private int idQuestion;
	private ArrayList<String> questions;
	private ArrayList<Integer>  answers;
	public SurveyQuestion(int idQuestion, ArrayList<String> question, ArrayList<Integer> answer) {
		this.idQuestion = idQuestion;
		this.questions = question;
		this.answers = answer;
	}
	public SurveyQuestion() {
		questions = new ArrayList<String>();
		answers = new ArrayList<Integer>();
		}
	public int getIdQuestion() {
		return idQuestion;
	}
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}
	public ArrayList<String> getQuestion() {
		return questions;
	}
	public void setQuestion(String question) {
		this.questions = questions;
	}
	public ArrayList<Integer> getAnswer() {
		return answers;
	}
	public void setAnswer(ArrayList<Integer> answer) {
		this.answers = answers;
	}
	@Override
	public String toString() {
		return "SurveyQuestion [idQuestion=" + idQuestion + ", question=" + questions.toString() + ", answer=" + answers.toString() + "]";
	}

	
}