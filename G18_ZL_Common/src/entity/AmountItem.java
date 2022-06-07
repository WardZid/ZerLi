package entity;

import java.io.Serializable;

/**
 * Class representing an item and how much of it was sold in a month,
 * it was used to save data for the table that represents every item and the amount of it that was sold
 * 
 * @author hamza
 *
 */
public class AmountItem implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -5669341947434706614L;
	
	/**
	 * the name of the item
	 */
	private String name;
	
	/**
	 * the amount that was sold in a month of that item
	 */
	private int amount;
	
	/**
	 * the constructor, it takes the name of the item and the amount that was sold in the month
	 * 
	 * @param name String representing the name of the item
	 * @param amount integer representing the amount
	 */
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
