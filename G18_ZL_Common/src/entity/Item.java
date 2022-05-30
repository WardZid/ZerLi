package entity;

import java.io.Serializable;

import javafx.scene.image.Image;

public class Item implements Serializable {

	private static final long serialVersionUID = 8505837124591041487L;

	public class OrderItem extends Item {

		private static final long serialVersionUID = 1L;
		private int amount;

		public OrderItem(int idItem, int idCategory, String name, double price, int sale, String color,
				String description, Image image, int amount) {
			super(idItem, idCategory, name, price, sale, color, description, image);
			this.amount = amount;
		}
		public OrderItem(Item item, int amount) {
			super(item.getIdItem(), item.getIdCategory(), item.getName(), item.getPrice(), item.getSale(),
					item.getColor(), item.getDescription(), item.getImage());
			this.amount=amount;
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

		@Override
		public String toString() {
			return "OrderItem [" + super.toString() + ", amount=" + amount + "]";
		}
	}

	public class ItemInBuild extends Item {

		private static final long serialVersionUID = 1L;
		private int amount;

		public ItemInBuild(int idItem, int idCategory, String name, double price, int sale, String color,
				String description, Image image, int amount) {
			super(idItem, idCategory, name, price, sale, color, description, image);
			this.amount = amount;
		}

		public ItemInBuild(Item item) {
			super(item.getIdItem(), item.getIdCategory(), item.getName(), item.getPrice(), item.getSale(),
					item.getColor(), item.getDescription(), item.getImage());

			if (item instanceof ItemInBuild)
				amount = ((ItemInBuild) item).amount;
			else
				amount = 1;
		}

		public ItemInBuild(Item item, int amount) {
			super(item.getIdItem(), item.getIdCategory(), item.getName(), item.getPrice(), item.getSale(),
					item.getColor(), item.getDescription(), item.getImage());
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

		@Override
		public String toString() {
			return "ItemInBuild [" + super.toString() + ", amount=" + amount + "]";
		}

	}

	public enum Category {
		POT, POT_FLOWER, FLOWER;

		public static Category getById(int id) {

			for (Category category : values()) {
				if (category.ordinal() == id)
					return category;
			}
			return null;
		}
	}

	private int idItem;
	private int idCategory;
	private String name;
	private double price;
	private int sale;
	private String color;
	private String description;
	private Image image;

	public Item(int idItem, int idCategory, String name, double price, int sale, String color, String description,
			Image image) {
		this.idItem = idItem;
		this.idCategory = idCategory;
		this.name = name;
		this.price = price;
		this.sale = sale;
		this.color = color;
		this.description = description;
		this.image = image;
	}

	// Getters and setters
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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	// handle category by id

	public Category getCategory() {
		return Category.getById(idCategory);
	}

	public void setCategory(Category category) {
		idCategory = category.ordinal();
	}

	@Override
	public String toString() {
		return "Item [idItem=" + idItem + ", Category=(" + idCategory + ") " + getCategory() + ", name=" + name
				+ ", price=" + price + ", sale=" + sale + ", color=" + color + ", description=" + description + "]";
	}

	public String infoString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\t idItem=");
		builder.append(idItem);
		builder.append("\n\t Category=");
		builder.append(getCategory());
		builder.append("\n\t name=");
		builder.append(name);
		builder.append("\n\t price=");
		builder.append(price);
		builder.append("\n\t sale=");
		builder.append(sale);
		builder.append("\n\t color=");
		builder.append(color);
		builder.append("\n\t description=");
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
//		return Objects.equals(color, other.color) && Objects.equals(description, other.description)
//				&& idCategory == other.idCategory && idItem == other.idItem && Objects.equals(image, other.image)
//				&& Objects.equals(name, other.name)
//				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price) && sale == other.sale;
	}

}
