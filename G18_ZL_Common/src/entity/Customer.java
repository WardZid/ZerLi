package entity;

import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = -4182417182449829997L;

	public enum CustomerStatus {
		ACTIVE, FROZEN;

		public static CustomerStatus getById(int id) {
			for (CustomerStatus status : values())
				if (status.ordinal() == id)
					return status;
			return null;
		}
	}

	/**
	 * Id of the customer
	 */
	private int idCustomer;
	/**
	 * id of the customer status in the database
	 */
	private int idCustomerStatus;
	/**
	 * login user id for the customer
	 */
	private int idUser;
	/**
	 * credit card
	 */
	private String card;
	/**
	 * fungible credit points
	 */
	private double point;

	/**
	 * General constructor that takes everything
	 * @param idCustomer id of the customer
	 * @param idCustomerStatus status id
	 * @param card credit card of the customer
	 * @param point refund points
	 */
	public Customer(int idCustomer, int idCustomerStatus, String card, double point) {
		this.idCustomer = idCustomer;
		this.idCustomerStatus = idCustomerStatus;
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
