package entity;

import java.io.Serializable;
import java.util.ArrayList;

import entity.Item.ItemInBuild;
import entity.Item.OrderItem;
/**
 * 
 * @author saher
 *
 */
public class Order implements Serializable, Cloneable {

	private static final long serialVersionUID = -3217273749865937459L;

	public enum OrderStatus {
		WAITING_APPROVAL, PROCESSING, DELIVERED, CANCELLED, UNAPPROVED, WAITING_CANCELLATION, REFUNDED;

		public static OrderStatus getById(int id) {

			for (OrderStatus status : values()) {
				if (status.ordinal() == id)
					return status;
			}
			return null;
		}
	}

/**
 * id for order 
 */
	private int idOrder;
	/**
	 * id for customer
	 */
	private int idCustomer;
	/**
	 * id for store
	 */
	private int idStore;
	/**
	 * id for order status
	 */
	private int idOrderStatus;
	/**
	 * order price
	 */
	private double price;
	/**
	 * order date
	 */
	private String orderDate;
	/**
	 * order deliveryDate
	 */
	private String deliveryDate;
	/**
	 * order cancelDate
	 */
	private String cancelDate;
	/**
	 * customer address
	 */
	private String address;
	/**
	 * order description
	 */
	private String description;
	/**
	 * order greetingCard
	 */
	private String greetingCard;
/**
 * number the items 
 */
	private int ItemInOrder;

	public int getItemInOrder() {
		return ItemInOrder;
	}
/**
 * to save the build items to this order
 */
	private ArrayList<BuildItem> buildItems = new ArrayList<>();
	/**
 * to save the  items to this order
	 */
	private ArrayList<OrderItem> items = new ArrayList<>();


/**
 * normal constructor for every thing 
 */
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

	/**
	 * adding a item to order and update the amount and the price and update the number of the items in the order
	 * @param item that we want to add 
	 */

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
	/**
	 * delete item update the amount if the amount 0 then we removed it from the Array List 
	 * then we update the price 
	 * we check how much we want to delete 
	 * @param item item we wants to delete 
	 * @param amountForDelete how much we want to delete 
	 */
	public void DeleteItemtoOrder(OrderItem item,  int amountForDelete) {
		int amountAfterDelete=0;
		 int FlagRemove=0;
		ItemInOrder-=amountForDelete;
		int amount = item.getAmount();
		for (OrderItem orderItem : items) {
			if (orderItem.getIdItem() == item.getIdItem()) {
				    amountAfterDelete=amount - amountForDelete;
					item.setAmount(amountAfterDelete);
					orderItem.setAmount(amountAfterDelete);
					setPrice(getPrice() - ((orderItem.getPrice()) * amountForDelete));
					if(amountAfterDelete==0)
						FlagRemove=1;
					break;
				 
				 

			}

		}
		if(FlagRemove==1  ) 
			items.remove(item);

	}
/**
 * delete buildItem update the amount if the amount 0 then we removed it from the Array List 
 * then we update the price 
 * we check how much we want to delete 
 * @param buildItem item we wants to delete 
 * @param amountForDelete how much we want to delete 
 */
	public void DeleteItemtoOrder(BuildItem buildItem, int amountForDelete) {
		ItemInOrder-=amountForDelete;
        int FlagRemove=0;
        int amount=0;
        int amountAfterDelete=0;
		if (buildItems.size() == 0) {
			return;
		}
		if (buildItems == null) {
			return;
		}
		if (buildItem == null) {
			return;
		}

		for (BuildItem searchItem : buildItems) {
			if (searchItem.getIdBuildItem() == buildItem.getIdBuildItem()) {
			    amount = searchItem.getAmount();
			    amountAfterDelete=amount - amountForDelete;
				buildItem.setAmount(amountAfterDelete);
				searchItem.setAmount(amountAfterDelete);
				setPrice(getPrice() - ((buildItem.getPrice()) * amountForDelete));
				if(amountAfterDelete==0)
					FlagRemove=1;
				break;
			}
		}
		if(FlagRemove==1  ) {
		buildItems.remove(buildItem);
		
		}
		 
	}
	
	 


	

	/**
	 * adding a build item to order with update price , amount , and number of the items in order
	 * @param buildItem
	 */
	public void addItemtoOrder(BuildItem buildItem) {
		ItemInOrder++;
		int amount = 0;
		for (BuildItem searchItem : buildItems) {

			if (searchItem.getIdBuildItem() == buildItem.getIdBuildItem()) {
				amount = searchItem.getAmount();
				buildItem.setAmount(amount++);
				searchItem.setAmount(amount++);

				setPrice(getPrice() + buildItem.getPrice());
				buildItem.setPrice(buildItem.getPrice());

				return;
			}

		}

	}

	/**
	 * create build item and add it to ArrayList-> build item-view
	 * @param buildItem
	 */
	public void addIBuildtemtoOrder(BuildItem buildItem) {
		 
		buildItems.add(buildItem);
		ItemInOrder++;
		price += buildItem.getPrice();
		System.out.println("OrderPrice=" + getPrice());
	}

	/**
	 * remove the item from the order and update the order price
	 * @param item from OrderItem type to remove it 
	 */
	public void DeleteItemFromScroll(OrderItem item) {
		System.out.println("items========" + items);
		if (items.size() == 0)
			return;
		if (items == null)
			return;

		setPrice(getPrice() - (item.getPrice() * item.getAmount()));
		ItemInOrder = ItemInOrder - item.getAmount();
		System.out.println("OrderPrice=" + getPrice());

		items.remove(item);
	}

 

	/**
	 *  Getters and Setters for all the parameters 
	 * @return
	 */
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

	/**
	 *  adding full item arrays
	 * @param orderItems
	 */

	public void addOrderItems(ArrayList<OrderItem> orderItems) {
		this.items.addAll(orderItems);
	}
/**
 * add the ArryaList buildItems to buildItems
 * @param buildItems
 */
	public void addBuildItems(ArrayList<BuildItem> buildItems) {
		this.buildItems.addAll(buildItems);
	}

	/**
	 *  handling id as enum
	 * @return Store by idStore
	 */
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
/**
 * add for the price buildItem price * amount 
 * @param itemPrice
 * @param buildItem
 */
	public void addPrice(double itemPrice, BuildItem buildItem) {

		this.setPrice(this.getPrice() + (itemPrice * buildItem.getAmount()));
	}
/**
 * decrease from the price a build item price * his amount
 * @param itemPrice
 * @param buildItem
 */
	public void deletePrice(double itemPrice, BuildItem buildItem) {

		this.setPrice(this.getPrice() - (itemPrice * buildItem.getAmount()));
	}

	public void deleteItem(ItemInBuild itemInBuild) {

	}

	public void deletePrice(double price2, int i) {
		this.setPrice(this.getPrice() - (price2));

	}
/**
 * adding 20$ to the price for the shipping 
 */
	public void addPriceForShipping() {
		this.setPrice(this.getPrice() + 20);

	}

	@Override
	public String toString() {
		return "Order [idOrder=" + idOrder + ", idCustomer=" + idCustomer + ", Store=(" + idStore + ") " + getStore()
				+ ", Order Status=(" + idOrderStatus + ") " + getOrderStatus() + ", price=" + price + ", orderDate="
				+ orderDate + ", deliveryDate=" + deliveryDate + ", address=" + address + ", description=" + description
				+ ", greetingCard=" + greetingCard + "]";
	}

}
