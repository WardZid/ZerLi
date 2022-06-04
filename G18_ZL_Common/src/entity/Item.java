package entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javafx.scene.image.Image;

public class Item implements Serializable {

	private static final long serialVersionUID = 8505837124591041487L;

	public class OrderItem extends Item {

		private static final long serialVersionUID = 1L;
		private int amount;

		public OrderItem(int idItem, String name, double price, int sale, String category, String color,
				String description,String status, byte[] imageBytes, int amount) {
			super(idItem, name, price, sale, category, color, description,status, imageBytes);
			this.amount = amount;
		}

		public OrderItem(Item item, int amount) {
			super(item);
			this.amount = amount;
		}

		/**
		 * Adds one unit to the order
		 * 
		 * @return amount after invocation of this method
		 */
		public int addOne() {
			return ++amount;
		}

		/**
		 * Deducts one unit from the order, but if there are 0 units it keeps it as is
		 * 
		 * @return amount after invocation of this method
		 */
		public int deductOne() {
			return (amount > 0) ? --amount : amount;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		public double getCalculatedPrice() {
			return amount*getPriceAfterSale();
		}

		@Override
		public String toString() {
			return "OrderItem [" + super.toString() + ", amount=" + amount + "]";
		}
	}

	public class ItemInBuild extends Item {

		private static final long serialVersionUID = 1L;
		private int amount;

		public ItemInBuild(int idItem, String name, double price, int sale, String category, String color,
				String description,String status, byte[] imageBytes, int amount) {
			super(idItem, name, price, sale, category, color, description,status, imageBytes);
			this.amount = amount;
		}

		public ItemInBuild(Item item) {
			super(item);

			if (item instanceof ItemInBuild)
				amount = ((ItemInBuild) item).amount;
			else
				amount = 1;
		}

		public ItemInBuild(Item item, int amount) {
			super(item);
			this.amount = amount;
		}

		/**
		 * Adds one unit to the build
		 * 
		 * @return amount after invocation of this method
		 */
		public int addOne() {
			return ++amount;
		}

		public int addAmount(int amountToAdd) {
			amount += amountToAdd;
			return amountToAdd;
		}

		/**
		 * Deducts one unit from the build, but if there are 0 units it keeps it as is
		 * 
		 * @return amount after invocation of this method
		 */
		public int deductOne() {
			if (amount > 0)
				return --amount;
			return amount;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		public double getCalculatedPrice() {
			return amount*getPriceAfterSale();
		}

		@Override
		public String toString() {
			return "ItemInBuild [" + super.toString() + ", amount=" + amount + "]";
		}

	}

	private int idItem;
	private String name;
	private double price;
	private int sale;
	private String category;
	private String color;
	private String description;
	private String status;
	private byte[] imageBytes;

	/**
	 * Standard constructor for some variables (not image)
	 * 
	 * @param idItem
	 * @param name
	 * @param price
	 * @param sale
	 * @param category
	 * @param color
	 * @param description
	 * @param image
	 */
	public Item(int idItem, String name, double price, int sale, String category, String color, String description,String status) {
		this.idItem = idItem;
		this.name = name;
		this.price = price;
		this.sale = sale;
		this.category = category;
		this.color = color;
		this.description = description;
		this.status=status;
	}

	/**
	 * Standard constructor for all the variables
	 * 
	 * @param idItem
	 * @param name
	 * @param price
	 * @param sale
	 * @param category
	 * @param color
	 * @param description
	 * @param image
	 */
	public Item(int idItem, String name, double price, int sale, String category, String color, String description,String status,
			byte[] imageBytes) {
		this.idItem = idItem;
		this.name = name;
		this.price = price;
		this.sale = sale;
		this.category = category;
		this.color = color;
		this.description = description;
		this.status=status;
		this.imageBytes=imageBytes;
	}

	/*
	 * Constructor that recieves an item and copies its variables
	 */
	public Item(Item item) {
		this.idItem = item.idItem;
		this.name = item.name;
		this.price = item.price;
		this.sale = item.sale;
		this.category = item.category;
		this.color = item.color;
		this.description = item.description;
		this.status=item.status;
		this.imageBytes=item.imageBytes;
	}

	/**
	 * Constructor for new items to be added that doesn't receive an id,status, or image
	 * new items always start as unavailable
	 * 
	 * @param name
	 * @param price
	 * @param sale
	 * @param category
	 * @param color
	 * @param description
	 */
	public Item(String name, double price, int sale, String category, String color, String description) {
		this.name = name;
		this.price = price;
		this.sale = sale;
		this.category = category;
		this.color = color;
		this.description = description;
	}

	// Getters and setters
	public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes=imageBytes;
	}
	public byte[] getImageBytes() {
		return imageBytes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Item [idItem=");
		builder.append(idItem);
		builder.append(", name=");
		builder.append(name);
		builder.append(", price=");
		builder.append(price);
		builder.append(", sale=");
		builder.append(sale);
		builder.append(", category=");
		builder.append(category);
		builder.append(", color=");
		builder.append(color);
		builder.append(", description=");
		builder.append(description);
		builder.append(", status=");
		builder.append(status);
		builder.append(", imageBytes= ");
		builder.append(imageBytes);
		builder.append("]");
		return builder.toString();
	}

	public String infoString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" idItem=");
		builder.append(getCategory());
		builder.append("\n name=");
		builder.append(name);
		builder.append("\n price=");
		builder.append(price);
		builder.append("\n sale=");
		builder.append(sale);
		builder.append("\n category=");
		builder.append(category);
		builder.append("\n color=");
		builder.append(color);
		builder.append("\n description=");
		builder.append(description);
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		return idItem == other.idItem;
	}

	public Image getImage() {
		if(imageBytes==null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
		Image image = new Image(bais);
		try {
			bais.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	
	public double getPriceAfterSale() {
		return (this.getPrice() - (((double) sale / 100) * this.getPrice()));
	}

	public double getPriceBeforeSale() {
		return (this.getPrice() * 100 / (100 - sale));
	}
}
