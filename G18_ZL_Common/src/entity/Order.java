package entity;

import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Class variables
	private int orderNum;
	private double price;
	private String branch;
	private String orderDate;
	private String deliveryDate;
	private String color;
	private String description;
	private String greetingCard;

	// main Constructor
	public Order(int orderNum, double price, String branch, String orderDate, String deliveryDate, String color,
			String description, String greetingCard) {
		this.orderNum = orderNum;
		this.price = price;
		this.branch = branch;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.color = color;
		this.description = description;
		this.greetingCard = greetingCard;
	}

	// Getters and Setters
	// only the color delivery date (date) can be changed

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public double getPrice() {
		return price;
	}

	public String getBranch() {
		return branch;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getDescription() {
		return description;
	}

	public String getGreetingCard() {
		return greetingCard;
	}
	
	
	//Delivery date getters and setters (divided)
	//YYYY-MM-DD HH:MM:SS
	public String getDeliveryDateDate() {
		return deliveryDate.substring(0,10);
	}
	
	public void setDeliveryDateDate(String date) {
		deliveryDate=date+deliveryDate.substring(10);
	}
	
	public String getDeliveryDateHour() {
		return deliveryDate.substring(11,13);
	}
	
	public void setDeliveryDateHour(String hour) {
		deliveryDate=deliveryDate.substring(0,11)+hour+deliveryDate.substring(13);
	}
	
	public String getDeliveryDateMinute() {
		return deliveryDate.substring(14,16);
	}
	
	public void setDeliveryDateMinute(String minute) {
		deliveryDate=deliveryDate.substring(0,14)+minute+deliveryDate.substring(16);
	}
	
	//Overridden Methods

	@Override
	// hash function only hashes order num
	public int hashCode() {
		return Objects.hash(orderNum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(color, other.color) && Objects.equals(deliveryDate, other.deliveryDate)
				&& Objects.equals(description, other.description) && Objects.equals(greetingCard, other.greetingCard)
				&& Objects.equals(orderDate, other.orderDate) && orderNum == other.orderNum
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(branch, other.branch);
	}

	@Override
	public Order clone() {
		return new Order(orderNum, price, branch, orderDate, deliveryDate, color, description, greetingCard);
	}

	@Override
	public String toString() {
		return "Order [orderNum=" + orderNum + ", price=" + price + ", branch=" + branch + ", orderDate=" + orderDate
				+ ", deliveryDate=" + deliveryDate + ", color=" + color + ", description=" + description
				+ ", greetingCard=" + greetingCard + "]";
	}

}
