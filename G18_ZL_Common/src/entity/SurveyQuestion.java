package entity;

import java.io.Serializable;

public class SurveyQuestion implements Serializable{
	
	private static final long serialVersionUID = -4693641523721808821L;
	
	private int idQuestion;
	private String question;
	private int answer;
	public SurveyQuestion(int idQuestion, String question, int answer) {
		this.idQuestion = idQuestion;
		this.question = question;
		this.answer = answer;
	}
	public int getIdQuestion() {
		return idQuestion;
	}
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	@Override
	public String toString() {
		return "SurveyQuestion [idQuestion=" + idQuestion + ", question=" + question + ", answer=" + answer + "]";
	}

	
}