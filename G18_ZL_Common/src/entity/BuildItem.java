package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import entity.Item.ItemInBuild;
import entity.Item.OrderItem;

public class BuildItem implements Serializable {

	private static final long serialVersionUID = 4594039631520296979L;
	
	private int idBuildItem;
	private int idOrder;
	private String name;
	private int amount;
	private String description;
	private HashMap<Integer, ItemInBuild> itemsInBuild = new HashMap<>();

	// Constructors
	public BuildItem(int idBuildItem, int idOrder) {
		this.idBuildItem = idBuildItem;
		this.idOrder = idOrder;
	}

	/**
	 * Constructor FOR BUILD_ITEM
	 * @param idBuildItem
	 * @param idOrder
	 * @param amount
	 */
	public BuildItem(int idBuildItem, int idOrder, int amount) {
		this.idBuildItem = idBuildItem;
		this.idOrder = idOrder;
		this.amount = amount;
	}

	/**
	 * Constructor without items in build FOR CATALOG
	 * @param idBuildItem
	 * @param idOrder
	 * @param amount
	 */
	public BuildItem(int idBuildItem,String name, int amount,String description) {
		this.idBuildItem = idBuildItem;
		this.name=name;
		this.amount = amount;
		this.description=description;
	}

	/**
	 * Full constructor FOR CATALOG
	 * @param idBuildItem
	 * @param idOrder
	 * @param name
	 * @param amount
	 * @param description
	 * @param items
	 */
	public BuildItem(int idBuildItem,String name, int amount,String description, Collection<Item> items) {
		this.idBuildItem = idBuildItem;
		this.name=name;
		this.amount = amount;
		this.description=description;
		addItemsInBuild(items);
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

	@Override
	public String toString() {
		return "BuildItem [idBuildItem=" + idBuildItem + ", idOrder=" + idOrder + ", name=" + name + ", amount="
				+ amount + ", description=" + description + ", itemsInBuild=" + itemsInBuild + "]";
	}

	// Instance methods
	public int addOne() {
		return ++amount;
	}

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
		if(item instanceof OrderItem)
			return false;
		ItemInBuild itemToAdd=item.new ItemInBuild(item);//************************************SUS
		if(itemsInBuild.containsKey(itemToAdd.getIdItem()))
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
		if(item instanceof OrderItem)
			return false;
		ItemInBuild itemToAdd=item.new ItemInBuild(item,amountInBuild);//************************************SUS
		if(itemsInBuild.containsKey(itemToAdd.getIdItem()))
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

}
