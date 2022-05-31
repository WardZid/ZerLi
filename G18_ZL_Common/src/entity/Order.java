package entity;

import java.io.Serializable;
import java.util.ArrayList;

import entity.Item.ItemInBuild;
import entity.Item.OrderItem;

public class Order implements Serializable, Cloneable {

	private static final long serialVersionUID = -3217273749865937459L;

	public enum OrderStatus {
		WAITING_APPROVAL, PROCESSING, DELIVERED, CANCELLED, UNAPPROVED, WAITING_CANCELLATION;

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
	private String cancelDate;
	private String address;
	private String description;
	private String greetingCard;

	private int ItemInOrder;
	
	public int getItemInOrder() {
		return ItemInOrder;
	}

	// Products in the order are enetered on request only as to minimize load to the
	// server
	private ArrayList<BuildItem> buildItems=new ArrayList<>();
	private ArrayList<OrderItem> items=new ArrayList<>();

	// main Constructor
	public Order(int idOrder, int idCustomer, int idStore, int idOrderStatus, double price, String orderDate,
			String deliveryDate, String cancelDate, String address, String description, String greetingCard) {
		this.idOrder = idOrder;
		this.idCustomer = idCustomer;
		this.idStore = idStore;
		this.idOrderStatus = idOrderStatus;
		this.price = price;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.cancelDate = cancelDate;
		this.address = address;
		this.description = description;
		this.greetingCard = greetingCard;
	}

	public Order() {
		this.ItemInOrder = 0;
		items = new ArrayList<OrderItem>();
	}

	// add item to order

	public void addItemtoOrder(Item item) {
		if (item == null)
			System.out.println("item is null when i add");

		int flagNotfoundItem = 0;
		if (items.size() == 0) {
			OrderItem itemToAdd = item.new OrderItem(item, 1);
			items.add(itemToAdd);
			System.out.println("Add to array " + itemToAdd);
			flagNotfoundItem = 1;
		} else
			for (OrderItem orderItem : items) {
				if (orderItem.getIdItem() == item.getIdItem()) {
					orderItem.addOne();
					System.out.println("update Amount" + orderItem);
					flagNotfoundItem = 1;
				}
			}

		if (flagNotfoundItem == 0) {

			OrderItem itemToAdd = item.new OrderItem(item, 1);
			items.add(itemToAdd);
			System.out.println("Add to array " + itemToAdd);
		}

		ItemInOrder++;
		price += item.getPrice();

	}

	public void DeleteItemtoOrder(OrderItem item) {

		int amount;
		ItemInOrder--;
		amount = item.getAmount();
		for (OrderItem orderItem : items) {
			if (orderItem.getIdItem() == item.getIdItem()) {
				if (amount > 1) {
					item.setAmount(amount--);
					orderItem.setAmount(amount--);
					setPrice(getPrice() - item.getPrice());
					return;
				} else {
					break;
				}

			}

		}
		if (item.getAmount() == 1) {
			items.remove(item);

			setPrice(getPrice() - item.getPrice());
		}

	}

	// add build item to cart

	// add item to Order (to Cart)
	public void addItemtoOrder(BuildItem buildItem) {
		int Amount = 0;
		for (BuildItem searchItem : buildItems) {
			Amount = buildItem.getAmount();
			System.out.println("amout " + Amount);
			if (searchItem.getIdBuildItem() == buildItem.getIdBuildItem()) {
				buildItem.setAmount(Amount++);
				searchItem.setAmount(Amount++);
				System.out.println("new amount " + searchItem.getAmount());
				setPrice(getPrice() + buildItem.getPrice());
				buildItem.setPrice(buildItem.getPrice());
				return;
			}
		}

	}

	// create build item and add it to arr-> build item-view
	public void addIBuildtemtoOrder(BuildItem buildItem) {
		if(buildItem==null)
			System.out.println("sssssssssssssssssss");
		buildItems.add(buildItem);
		ItemInOrder++;
		price += buildItem.getPrice();

	}

	public void DeleteItemtoOrder(BuildItem buildItem) {
		ItemInOrder--;

		if (buildItems.size() == 0) {
			return;
		}
		if (buildItems == null) {
			return;
		}
		if (buildItem == null) {
			return;
		}
		int Amount = buildItem.getAmount();
		for (BuildItem searchItem : buildItems) {
			if (searchItem.getIdBuildItem() == buildItem.getIdBuildItem()) {

				if (Amount > 1) {
					buildItem.setAmount(Amount--);
					searchItem.setAmount(Amount--);
					setPrice(getPrice() - buildItem.getPrice());

					return;
				} else {
					break;

				}

			}
		}
		setPrice(getPrice() - buildItem.getPrice());
		buildItems.remove(buildItem);

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

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
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

	public ArrayList<OrderItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<OrderItem> items) {
		this.items = items;
	}

	//adding full item arrays
	
	public void addOrderItems(ArrayList<OrderItem> orderItems) {
		this.items.addAll(orderItems);
	}
	
	public void addBuildItems(ArrayList<BuildItem> buildItems) {
		this.buildItems.addAll(buildItems);
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

	public void addPrice(double itemPrice, BuildItem buildItem) {

		this.setPrice(this.getPrice() + (itemPrice * buildItem.getAmount()));
	}

	public void deletePrice(double itemPrice, BuildItem buildItem) {

		this.setPrice(this.getPrice() - (itemPrice * buildItem.getAmount()));
	}

	public void deleteItem(ItemInBuild itemInBuild) {
		// TODO Auto-generated method stub

	}

	public void deletePrice(double price2, int i) {
		this.setPrice(this.getPrice() - (price2));

	}
	public void addPriceForShipping( ) {
		this.setPrice(this.getPrice()+20);

	}

	@Override
	public String toString() {
		return "Order [idOrder=" + idOrder + ", idCustomer=" + idCustomer + ", Store=(" + idStore + ") " + getStore()
				+ ", Order Status=(" + idOrderStatus + ") " + getOrderStatus() + ", price=" + price + ", orderDate="
				+ orderDate + ", deliveryDate=" + deliveryDate + ", address=" + address + ", description=" + description
				+ ", greetingCard=" + greetingCard + "]";
	}

}
