package entity;

import java.io.Serializable;

public class DailyIncome implements Serializable, Cloneable {
	
	private int day;
	private double income;
	
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
