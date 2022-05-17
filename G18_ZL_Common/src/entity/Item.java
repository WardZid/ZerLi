package entity;

public class Item   {
 
	public enum Category{
		Pot,
		PotFlower,
		bouquetFlower,
	}
	private int idItem;
	private int idCategory;
	private String name;
	private double price;
	private int sale;
	private String color;
	private String description;
	private int quantity=0;


	public Item(int idItem, int idCategory, String name, double price, int sale, String color, String description) {
		this.idItem = idItem;
		this.idCategory = idCategory;
		this.name = name;
		this.price = price;
		this.sale = sale;
		this.color = color;
		this.description = description;
		
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



	public int getIdCategory() {
		return idCategory;
	}



	public void setIdCategory(int idCategory) {
		this.idCategory = idCategory;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public int getSale() {
		return sale;
	}



	public void setSale(int sale) {
		this.sale = sale;
	}



	public String getColor() {
		return color;
	}



	public void setColor(String color) {
		this.color = color;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
