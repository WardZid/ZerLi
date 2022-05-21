package entity;

import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = -4182417182449829997L;

	public enum CustomerStatus {
		UNAPPROVED, APPROVED, FROZEN;

		public static CustomerStatus getById(int id) {
			for (CustomerStatus status : values())
				if (status.ordinal() == id)
					return status;
			return null;
		}
	}

	private int idCustomer;
	private int idCustomerStatus;
	private int idUser;
	private String name;
	private String email;
	private String phone;
	private String card;

	public Customer(int idCustomer, int idCustomerStatus, int idUser, String name, String email, String phone,
			String card) {
		this.idCustomer = idCustomer;
		this.idCustomerStatus = idCustomerStatus;
		this.idUser = idUser;
		this.name = name;
		this.email = email;
		this.phone = phone;
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

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	@Override
	public String toString() {
		return "Customer [idCustomer=" + idCustomer + ", CustomerStatus=(" + idCustomerStatus + ") "
				+ CustomerStatus.getById(idCustomerStatus) + ", idUser=" + idUser + ", name=" + name + ", email="
				+ email + ", phone=" + phone + ", card=" + card + "]";
	}

}
