package entity;

public class Customer {

	private int idCustomer;
	private int idCustomerStatus;
	private int idUser;
	private String name;
	private String email;
	private String phone;
	private String status;
	private String card;
	
	public Customer(int idCustomer, int idCustomerStatus, int idUser, String name, String email, String phone,
			String status, String card) {
		this.idCustomer = idCustomer;
		this.idCustomerStatus = idCustomerStatus;
		this.idUser = idUser;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.card = card;
	}

	public int getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	public int getIdCustomerStatus() {
		return idCustomerStatus;
	}

	public void setIdCustomerStatus(int idCustomerStatus) {
		this.idCustomerStatus = idCustomerStatus;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
	
	
}
