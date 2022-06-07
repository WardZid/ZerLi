package entity;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * a survey class to save all survey details 
 * @author saher
 *
 */
public class Survey implements Serializable {

	private static final long serialVersionUID = 3569646095233196718L;
/**
 * id for the survey
 */
	private int idSurvey;
	/**
	 * the date of the survey
	 */
	private String dateSurvey;
	/**
	 * id of the store that belong to this survey
	 */
	private int idStore;
	/**
	 * answers the questions ArrayList
	 */
	private ArrayList<Integer>  answers;
	
	/// getters and setters 
	public ArrayList<Integer> getAnswers() {
		return answers;
	}
	public void setAnswers(ArrayList<Integer> answers) {
		this.answers = answers;
	}
	public int getIdQuestion() {
		return idQuestion;
	}
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}
	/**
	 * questions id for witch id questions the survey belongs to
	 */
	private int idQuestion;
	/**
	 * object from SurveyQuestion type to call there methods 
	 */
	@SuppressWarnings("unused")
	private SurveyQuestion surveyQuestion;

	/**
	 * constructor with id store parameter and date and store id 
	 * @param idSurvey id for the survey
	 * @param dateSurvey survey date
	 * @param idStore store id 
	 */
	public Survey(int idSurvey, String dateSurvey, int idStore) {
		this.idSurvey = idSurvey;
		this.dateSurvey = dateSurvey;
		this.idStore = idStore;
	}
	/**
	 * constructor with just tow parameters date and store id 
	 * @param dateSurvey survey date
	 * @param idStore store id 
	 */
	public Survey(String dateSurvey, int idStore) {
		this.dateSurvey = dateSurvey;
		this.idStore = idStore;
	}
	/**
	 * create a new survey
	 */
	public Survey() {
		surveyQuestion = new SurveyQuestion();
	}

	//// getters and setters 
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
	
	public Store getStore() {
		return Store.getById(idStore);
	}
	
	public void setStore(Store store) {
		idStore=store.ordinal();
	}
//	public SurveyQuestion getSurveyQuestion() {
//		return surveyQuestion;
//	}
//	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
//		this.surveyQuestion = surveyQuestion;
//	}

	@Override
	public String toString() {
		return "Survey [idSurvey=" + idSurvey + ", dateSurvey=" + dateSurvey + ", idStore=" + idStore + ", questions="
				+ "]";
	}
	
}
