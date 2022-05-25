package entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable, Cloneable {

	private static final long serialVersionUID = -3217273749865937459L;

	public enum OrderStatus {
		WAITING_APPROVAL, PROCESSING, DELIVERED, CANCELLED, UNAPPROVED,WAITING_CANCELLATION;

		public static OrderStatus getById(int id) {

			for (OrderStatus status : values()) {
				if (status.ordinal() == id)
					return status;
			}
			return null;
		}
	}

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

	// Products in the order are enetered on request only as to minimize load to the
	// server
	private ArrayList<BuildItem> buildItems;
	private ArrayList<Item> items;

	// main Constructor
	public Order(int idOrder, int idCustomer, int idStore, int idOrderStatus, double price, String orderDate,
			String deliveryDate, String address, String description, String greetingCard) {
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

	public ArrayList<BuildItem> getBuildItems() {
		return buildItems;
	}

	public void setBuildItems(ArrayList<BuildItem> buildItems) {
		this.buildItems = buildItems;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	// handling id as enum
	public Store getStore() {
		return Store.getById(idStore);
	}

	public void setStore(Store store) {
		idStore = store.ordinal();
	}

	public OrderStatus getOrderStatus() {
		return OrderStatus.getById(idOrderStatus);
	}

	public void setOrderStatus(OrderStatus status) {
		idOrderStatus = status.ordinal();
	}

	@Override
	public String toString() {
		return "Order [idOrder=" + idOrder + ", idCustomer=" + idCustomer + ", Store=(" + idStore + ") " + getStore()
				+ ", Order Status=(" + idOrderStatus + ") " + getOrderStatus() + ", price=" + price + ", orderDate="
				+ orderDate + ", deliveryDate=" + deliveryDate + ", address=" + address + ", description=" + description
				+ ", greetingCard=" + greetingCard + "]";
	}

}
