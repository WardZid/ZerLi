package entity;

import java.io.Serializable;
import java.util.Arrays;

public class SurveyReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9058357223797599049L;

	private String year;
	private int idQuestion;
	private byte[] reportBytes;
	public SurveyReport(String year, int idQuestion, byte[] reportBytes) {
		super();
		this.year = year;
		this.idQuestion = idQuestion;
		this.reportBytes = reportBytes;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getIdQuestion() {
		return idQuestion;
	}
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}
	public byte[] getReportBytes() {
		return reportBytes;
	}
	public void setReportBytes(byte[] reportBytes) {
		this.reportBytes = reportBytes;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SurveyReport [year=");
		builder.append(year);
		builder.append(", idQuestion=");
		builder.append(idQuestion);
		builder.append(", reportBytes=");
		builder.append(reportBytes);
		builder.append("]");
		return builder.toString();
	}
	
}
