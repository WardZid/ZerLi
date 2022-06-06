package entity;

import java.io.Serializable;

public class Receipt implements Serializable, Cloneable {

	private static final long serialVersionUID = 7473241707950981161L;
	private String name;
	private String date;
	private double income;

	/**
	 * 
	 * @param name
	 * @param date
	 * @param income
	 */
	public Receipt(String name, String date, double income) {
		this.name = name;
		this.date = date;
		this.income = income;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the income
	 */
	public double getIncome() {
		return income;
	}

	/**
	 * @param income the income to set
	 */
	public void setIncome(double income) {
		this.income = income;
	}

	@Override
	public String toString() {
		return "Receipt [name=" + name + ", date=" + date + ", income=" + income + "]";
	}

}
