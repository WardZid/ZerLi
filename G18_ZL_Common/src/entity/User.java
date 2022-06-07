package entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * every person that signs has a user, and this is it
 * 
 * @author wardz
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = -3363120264501521428L;

	/**
	 * types of users
	 * 
	 * @author wardz
	 *
	 */
	public enum UserType {
		CUSTOMER, BRANCH_MANAGER, CEO, STORE_WORKER, DELIVERY_WORKER, CUSTOMER_SUPPORT, SUPPORT_SPECIALIST,
		CATALOG_MANAGER, SURVEY_WORKER;

		public static UserType getById(int id) {

			for (UserType type : values()) {
				if (type.ordinal() == id)
					return type;
			}
			return null;
		}

		/**
		 * check if enum is worker by our chooing
		 * 
		 * @return true if worker
		 */
		public boolean isWorker() {
			if (this == BRANCH_MANAGER || this == STORE_WORKER || this == DELIVERY_WORKER || this == CATALOG_MANAGER
					|| this == SURVEY_WORKER)
				return true;
			return false;
		}
	}

	/**
	 * personal id of user
	 */
	private int idUser;
	/**
	 * type of user (customer, ceo, etc.)
	 */
	private int idUserType;
	/**
	 * username login credential
	 */
	private String username;
	/**
	 * password for login
	 */
	private String password;
	/**
	 * personal name
	 */
	private String name;
	/**
	 * personal email
	 */
	private String email;
	/**
	 * personal phone number
	 */
	private String phone;
	/**
	 * customer account number (if user is customer)
	 */
	private int idCustomer;
	/**
	 * worker account number (if user is worker)
	 */
	private int idWorker;

	/**
	 * user constructor with only user and pass to send to server for authentication
	 * 
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 
	 * @param idUser
	 * @param idUserType
	 * @param username
	 * @param password
	 * @param name
	 * @param email
	 * @param phone
	 */
	public User(int idUser, int idUserType, String username, String password, String name, String email, String phone) {
		this.idUser = idUser;
		this.idUserType = idUserType;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public int getIdUserType() {
		return idUserType;
	}

	public void setIdUserType(int idUserType) {
		this.idUserType = idUserType;
	}

	public int getIdUser() {
		return idUser;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone number to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	 * @return the idWorker
	 */
	public int getIdWorker() {
		return idWorker;
	}

	/**
	 * @param idWorker the idWorker to set
	 */
	public void setIdWorker(int idWorker) {
		this.idWorker = idWorker;
	}

	/**
	 * gets usertype enum using id
	 * @return
	 */
	public UserType getUserType() {
		return UserType.getById(idUserType);
	}

	/**
	 * sets type id using usertype
	 * @param type
	 */
	public void setUserType(UserType type) {
		idUserType = type.ordinal();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [idUser=");
		builder.append(idUser);
		builder.append(", idUserType=");
		builder.append(idUserType);
		builder.append(", username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", name=");
		builder.append(name);
		builder.append(", email=");
		builder.append(email);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", idCustomer=");
		builder.append(idCustomer);
		builder.append(", idWorker=");
		builder.append(idWorker);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * hashing function
	 */
	@Override
	public int hashCode() {
		return Objects.hash(email, idUser, idUserType, name, password, phone, username);
	}

	/**
	 * equals to compare with others
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && idUser == other.idUser && idUserType == other.idUserType
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password)
				&& Objects.equals(phone, other.phone) && Objects.equals(username, other.username);
	}

}
