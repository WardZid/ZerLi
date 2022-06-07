package entity;

import java.io.Serializable;

/**
 * Class representing a day and the income of the store in the this day,
 * it was used to represent a table.
 * 
 * @author hamza
 *
 */
public class DailyIncome implements Serializable, Cloneable {
	

	private static final long serialVersionUID = -8170751013755224783L;
	
	/**
	 * the day 
	 */
	private int day;
	
	/**
	 * the income in this day
	 */
	private double income;
	
	/**
	 * the constructor, it takes the day and the income in the day
	 * 
	 * @param day integer representing the day
	 * @param income double representing the day
	 */
	public DailyIncome(int day, double income) {
		this.day = day;
		this.income = income;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	@Override
	public String toString() {
		return "DailyIncome [day=" + day + ", income=" + income + "]";
	}
	
	
	
	
}
