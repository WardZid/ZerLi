package entity;

public class Receipt {
	
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
	
	
}
