package entity;

import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Class variables
	private int idOrder;
	private int idCustomer;
	private int idStore;
	private int idOrderStatus;
	private double price;
	private String orderDate;
	private String deliveryDate;
	private String address;
	private String description;
	private String greetingCard;
	
	
	// main Constructor
	public Order(int idOrder, int idCustomer, int idStore, int idOrderStatus, double price, String orderDate,
			String deliveryDate, String address, String description, String greetingCard) {
		super();
		this.idOrder = idOrder;
		this.idCustomer = idCustomer;
		this.idStore = idStore;
		this.idOrderStatus = idOrderStatus;
		this.price = price;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.address = address;
		this.description = description;
		this.greetingCard = greetingCard;
	}

	
	// Getters and Setters
	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public int getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	public int getIdStore() {
		return idStore;
	}

	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}

	public int getIdOrderStatus() {
		return idOrderStatus;
	}

	public void setIdOrderStatus(int idOrderStatus) {
		this.idOrderStatus = idOrderStatus;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGreetingCard() {
		return greetingCard;
	}

	public void setGreetingCard(String greetingCard) {
		this.greetingCard = greetingCard;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

 

	
	 


	
	
	// only the color delivery date (date) can be changed

	
}
