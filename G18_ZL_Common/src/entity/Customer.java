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
	private String card;
	private double point;

	public Customer(int idCustomer, int idCustomerStatus, int idUser, String card, double point) {
		this.idCustomer = idCustomer;
		this.idCustomerStatus = idCustomerStatus;
		this.idUser = idUser;
		this.card = card;
		this.point = point;
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

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return "Customer [idCustomer=" + idCustomer + ", CustomerStatus=(" + idCustomerStatus + ") "
				+ CustomerStatus.getById(idCustomerStatus) + ", idUser=" + idUser + ", card=" + card + ", point="
				+ point + "]";
	}

}
