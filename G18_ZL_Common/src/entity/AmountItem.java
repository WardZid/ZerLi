package entity;

import java.io.Serializable;

public class AmountItem implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -5669341947434706614L;
	private String name;
	private int amount;
	
	public AmountItem(String name, int amount) {
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "AmountItem [name=" + name + ", amount=" + amount + "]";
	}
	
}
