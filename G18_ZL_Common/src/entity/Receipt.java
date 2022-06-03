package entity;

import java.io.Serializable;

public class Receipt implements Serializable, Cloneable{

	private static final long serialVersionUID = 7473241707950981161L;
	private String name;
	private String date;
	private double income;
	
	public Receipt(String name, String date, double income) {
		this.name = name;
		this.date = date;
		this.income = income;  
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	@Override
	public String toString() {
		return "Receipt [name=" + name + ", date=" + date + ", income=" + income + "]";
	}
	
	
	
}
