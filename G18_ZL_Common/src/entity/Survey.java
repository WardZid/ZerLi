package entity;

import java.io.Serializable;
import java.util.HashSet;

public class Survey implements Serializable {

	private static final long serialVersionUID = 3569646095233196718L;
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

	private int idSurvey;
	private String dateSurvey;
	private int idStore;
	private HashSet<SurveyQuestion> questions=new HashSet<>();
	
	public Survey(int idSurvey, String dateSurvey, int idStore, HashSet<SurveyQuestion> questions) {
		this.idSurvey = idSurvey;
		this.dateSurvey = dateSurvey;
		this.idStore = idStore;
		this.questions = questions;
	}
	
	public Survey(int idSurvey, String dateSurvey, int idStore) {
		this.idSurvey = idSurvey;
		this.dateSurvey = dateSurvey;
		this.idStore = idStore;
	}

	public int getIdSurvey() {
		return idSurvey;
	}

	public void setIdSurvey(int idSurvey) {
		this.idSurvey = idSurvey;
	}

	public String getDateSurvey() {
		return dateSurvey;
	}

	public void setDateSurvey(String dateSurvey) {
		this.dateSurvey = dateSurvey;
	}

	public int getIdStore() {
		return idStore;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}

	public HashSet<SurveyQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(HashSet<SurveyQuestion> questions) {
		this.questions = questions;
	}
	
	//enum for nd from id
	public Store getStore() {
		return Store.getById(idStore);
	}
	
	public void setStore(Store store) {
		idStore=store.ordinal();
	}

	@Override
	public String toString() {
		return "Survey [idSurvey=" + idSurvey + ", dateSurvey=" + dateSurvey + ", idStore=" + idStore + ", questions="
				+ questions + "]";
	}
	
}
