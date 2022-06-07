package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import entity.Item.ItemInBuild;
import entity.Item.OrderItem;
/**
 * BuildItem to save all the items of the products that the customer built
 * 
 * @author saher
 *
 */
public class BuildItem implements Serializable {

	private static final long serialVersionUID = 4594039631520296979L;
/**
 * id for build item
 */
	private int idBuildItem;
	/**
	 * id of the order
	 */
	private int idOrder;
	/**
	 * amount for the build item 
	 */
	private int amount;
	/**
	 * description for the build item 
	 */
	private String description;
	/**
	 * hash map with id of the ItemInBuild as key and the ItemInBuild itself as value
	 */
	private HashMap<Integer, ItemInBuild> itemsInBuild = new HashMap<>();

	//non sql variables
	private double price;
	@SuppressWarnings("unused")
	private static int idBuildItemAutomatic = 0;

	// Constructors
	public BuildItem() {
		amount = 1;
		idBuildItemAutomatic++;
		idOrder = 0;
	}
/**
 * constructor with to parameters 
 * @param idBuildItem build item id 
 * @param idOrder order id 
 */
	public BuildItem(int idBuildItem, int idOrder) {
		this.idBuildItem = idBuildItem;
		this.idOrder = idOrder;
	}

	/**
	 * Constructor FOR BUILD_ITEM
	 * 
	 * @param idBuildItem
	 * @param idOrder
	 * @param amount
	 */
	public BuildItem(int idBuildItem, int idOrder, int amount) {
		this.idBuildItem = idBuildItem;
		this.idOrder = idOrder;
		this.amount = amount;
	}

	

	// Getters and Setters
	public int getIdBuildItem() {
		return idBuildItem;
	}

	public void setIdBuildItem(int idBuildItem) {
		this.idBuildItem = idBuildItem;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap<Integer, ItemInBuild> getItemsInBuild() {
		return itemsInBuild;
	}

	public void setItemsInBuild(HashMap<Integer, ItemInBuild> itemsInBuild) {
		this.itemsInBuild = itemsInBuild;
	}

	
	
	public void setPrice(double price) {
		this.price=price;
	}
	
	public double getPrice() {
		return price;
	}
	/**
	 * update the build item price thats contains items with price
	 * (update full price and return it )
	 * @return double fullPrice
	 */
	public double getFullPrice() {
		double fullPrice=0;
		for (ItemInBuild iInB : itemsInBuild.values()) {
			fullPrice+=iInB.getCalculatedPrice();
		}
		return fullPrice;
	}

	@Override
	public String toString() {
		return "BuildItem [idBuildItem=" + idBuildItem + ", idOrder=" + idOrder + ", amount="
				+ amount + ", description=" + description + ", itemsInBuild=" + itemsInBuild + "]";
	}
	/**
	 * string that saved all the build item details 
	 * @return String builder
	 */
	public String infoString() {
		StringBuilder builder = new StringBuilder();
		builder.append("idBuildItem=");
		builder.append(idBuildItem);
		builder.append("\n idOrder=");
		builder.append(idOrder);
		builder.append("\n amount=");
		builder.append(amount);
		builder.append("\n description=");
		builder.append(description);
		builder.append("\n price=");
		builder.append(price);
		builder.append("\n itemsInBuild=");
		for (ItemInBuild item : itemsInBuild.values()) {
			builder.append("\n\t item="+item.getName());
			builder.append("\n\t\t amount="+item.getAmount());
		}
		return builder.toString();
	}


/**
 * increase the amount of the build item
 * @return amount 
 */
	public int addOne() {
		return ++amount;
	}
/**
 * decrease the amount of the build item
 * @return amount 
 */
	public int deductOne() {
		if (amount > 0)
			return --amount;
		return amount;
	}

	/**
	 * 
	 * @param item
	 * @return true if successfully added
	 */
	public boolean addItem(Item item) {
		if (item instanceof OrderItem)
			return false;
		ItemInBuild itemToAdd = item.new ItemInBuild(item);// ************************************SUS
		if (itemsInBuild.containsKey(itemToAdd.getIdItem()))
			itemsInBuild.get(itemToAdd.getIdItem()).addAmount(itemToAdd.getAmount());
		else
			itemsInBuild.put(itemToAdd.getIdItem(), itemToAdd);
		return true;
	}

	/**
	 * 
	 * @param item
	 * @return true if successfully added
	 */
	public boolean addItem(Item item, int amountInBuild) {
		if (item instanceof OrderItem)
			return false;
		ItemInBuild itemToAdd = item.new ItemInBuild(item, amountInBuild);// ************************************SUS
		if (itemsInBuild.containsKey(itemToAdd.getIdItem()))
			itemsInBuild.get(itemToAdd.getIdItem()).addAmount(itemToAdd.getAmount());
		else
			itemsInBuild.put(itemToAdd.getIdItem(), itemToAdd);
		return true;
	}

	/**
	 * For each item in the given collection, it checks if it exists in the HashMap,
	 * and also checks if it is an ItemInBuild or just an item and adds to HashMap
	 * accordingly
	 * 
	 * @param items to be added
	 */
	public void addItemsInBuild(Collection<Item> items) {
		for (Item newItem : items) {
			addItem(newItem);
		}
	}

 
	/**
	 *  add itemInBuild to build item and update amount and price 
	 * @param itemInBuild item to set amount for itemInBuild
	 */
	public void addItem(ItemInBuild itemInBuild) {

		int amount = itemInBuild.getAmount();
		itemsInBuild.get(itemInBuild.getIdItem()).setAmount(amount++);
		itemInBuild.setAmount(amount++);
		this.setPrice(this.getPrice() + itemInBuild.getPrice());

	}
/**
 * delete itemInBuild to build item and update amount and price 
 * @param itemInBuild item to set amount for itemInBuild
 */
	public void deleteItem(ItemInBuild itemInBuild) {

		int amount = itemInBuild.getAmount();
		if (amount > 1) {
			// update amount
			this.getItemsInBuild().get(itemInBuild.getIdItem()).setAmount(amount--);
			itemInBuild.setAmount(amount--);

		} else {
			// amount == 1
			this.getItemsInBuild().remove(itemInBuild.getIdItem());

		}

		// update price
		this.setPrice(this.getPrice() - itemInBuild.getPrice());

	}
/**
 * remove item from itemsInBuild and set the price 
 * @param item
 * @return
 */
	public boolean DeleteItem(Item item) {
		if (item instanceof OrderItem)
			return false;
		if (itemsInBuild.get(item.getIdItem()) != null)
			this.setPrice(this.getPrice() - itemsInBuild.get(item.getIdItem()).getAmount() * item.getPrice());
		itemsInBuild.remove(item.getIdItem());
		return true;
	}

	/**
	 * For each item in the given collection, it checks if it exists in the HashMap,
	 * and also checks if it is an ItemInBuild or just an item and adds to HashMap
	 * accordingly
	 * 
	 * @param items to be added
	 */

	public boolean setItem(Item item, int amountInBuild) {
		int oldAmount;
		int newAmount = amountInBuild;
		if (item instanceof OrderItem)
			return false;
		ItemInBuild itemToAdd = item.new ItemInBuild(item, amountInBuild);// ******SUS
		if (itemsInBuild.containsKey(itemToAdd.getIdItem())) {

			oldAmount = itemsInBuild.get(itemToAdd.getIdItem()).getAmount();
			this.setPrice(this.getPrice() + (newAmount - oldAmount) * item.getPrice());

			itemToAdd.setAmount(amountInBuild);
			itemsInBuild.get(itemToAdd.getIdItem()).setAmount(itemToAdd.getAmount());
		} else {
			this.setPrice(this.getPrice() + item.getPrice() * amountInBuild);
			itemToAdd.setAmount(amountInBuild);
			itemsInBuild.put(itemToAdd.getIdItem(), itemToAdd);
		}
		return true;
	}
}
