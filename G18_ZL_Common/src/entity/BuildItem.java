package entity;

import java.util.ArrayList;

public class BuildItem {

	private int idItem;
	private int idBuildItem;
	private int quantity=0;
	
	ArrayList<Item> items ;

	public BuildItem(int idItem, int idBuildItem) {
		super();
		this.idItem = idItem;
		this.idBuildItem = idBuildItem;
		this.items= new ArrayList<Item>();
	}

	
	public void AddItemToBuild(Item item) {
		if(this.items.contains(item))
			item.AddQuantity();
		else 
			this.items.add(item);
	}
	
	public void AddQuantity() {
		this.quantity++;
	}
	
	public void DeclineQuantity() {
		this.quantity--;
	}
	
	
	public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public int getIdBuildItem() {
		return idBuildItem;
	}

	public void setIdBuildItem(int idBuildItem) {
		this.idBuildItem = idBuildItem;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
 

	
	
	

	
}
