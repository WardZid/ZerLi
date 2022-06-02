package entity;

import java.io.Serializable;
import java.util.HashSet;

public class Survey implements Serializable {

	private static final long serialVersionUID = 3569646095233196718L;

	private int idSurvey;
	private String dateSurvey;
	private int idStore;
	private SurveyQuestion surveyQuestion;
//	private HashSet<SurveyQuestion> questions=new HashSet<>();//
	
//	public Survey(int idSurvey, String dateSurvey, int idStore, HashSet<SurveyQuestion> questions) {
//		this.idSurvey = idSurvey;
//		this.dateSurvey = dateSurvey;
//		this.idStore = idStore;
//		this.questions = questions;
//	}
	
	public Survey(int idSurvey, String dateSurvey, int idStore) {
		this.idSurvey = idSurvey;
		this.dateSurvey = dateSurvey;
		this.idStore = idStore;
	}
	public Survey(String dateSurvey, int idStore) {
		this.dateSurvey = dateSurvey;
		this.idStore = idStore;
	}
	public Survey() {
		surveyQuestion = new SurveyQuestion();
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

//	public HashSet<SurveyQuestion> getQuestions() {
//		return questions;
//	}
//
//	public void setQuestions(HashSet<SurveyQuestion> questions) {
//		this.questions = questions;
//	}
	
	//enum for nd from id
	public Store getStore() {
		return Store.getById(idStore);
	}
	
	public void setStore(Store store) {
		idStore=store.ordinal();
	}
	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}
	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

//	@Override
//	public String toString() {
//		return "Survey [idSurvey=" + idSurvey + ", dateSurvey=" + dateSurvey + ", idStore=" + idStore + ", questions="
//				+ questions + "]";
//	}
	
}
