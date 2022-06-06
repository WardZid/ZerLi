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

	/**
	 * @return the idCustomer
	 */
	public int getIdCustomer() {
		return idCustomer;
	}

	/**
	 * @param idCustomer the idCustomer to set
	 */
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	/**
	 * @return the idCustomerStatus
	 */
	public int getIdCustomerStatus() {
		return idCustomerStatus;
	}

	/**
	 * @param idCustomerStatus the idCustomerStatus to set
	 */
	public void setIdCustomerStatus(int idCustomerStatus) {
		this.idCustomerStatus = idCustomerStatus;
	}

	/**
	 * @return the idUser
	 */
	public int getIdUser() {
		return idUser;
	}

	/**
	 * @param idUser the idUser to set
	 */
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	/**
	 * @return the card
	 */
	public String getCard() {
		return card;
	}

	/**
	 * @param card the card to set
	 */
	public void setCard(String card) {
		this.card = card;
	}

	/**
	 * @return the point
	 */
	public double getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(double point) {
		this.point = point;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Customer [idCustomer=");
		builder.append(idCustomer);
		builder.append(", idCustomerStatus=");
		builder.append(idCustomerStatus);
		builder.append(", idUser=");
		builder.append(idUser);
		builder.append(", card=");
		builder.append(card);
		builder.append(", point=");
		builder.append(point);
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * sets CustomerStatus enum as id
	 * @param customerStatus to set as idCustomerStatus
	 */
	public void setCustomerStatus(CustomerStatus customerStatus) {
		this.idCustomerStatus=customerStatus.ordinal();
	}
	
	/**
	 * return UserType from stored id
	 * @return CustomerStatus enum
	 */
	public CustomerStatus getCustomerStatus() {
		return CustomerStatus.getById(idCustomerStatus);
	}

}
