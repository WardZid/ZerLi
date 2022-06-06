package entity;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -3363120264501521428L;

	public enum UserType {
		CUSTOMER, BRANCH_MANAGER, CEO, STORE_WORKER, DELIVERY_WORKER, CUSTOMER_SUPPORT, SUPPORT_SPECIALIST,
		CATALOG_MANAGER,SURVEY_WORKER;

		public static UserType getById(int id) {

			for (UserType type : values()) {
				if (type.ordinal() == id)
					return type;
			}
			return null;
		}
		public boolean isWorker() {
			if(this==BRANCH_MANAGER || this==STORE_WORKER || this==DELIVERY_WORKER || this==CATALOG_MANAGER || this==SURVEY_WORKER)
				return true;
			return false;
		}
	}

	private int idUser;
	private int idUserType;
	private String username;
	private String password;
	private String name;
	private String email;
	private String phone;
	private int idCustomer;
	private int idWorker;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public User(int idUser, int idUserType, String username, String password, String name,String email,String phone) {
		this.idUser = idUser;
		this.idUserType = idUserType;
		this.username = username;
		this.password = password;
		this.name=name;
		this.email=email;
		this.phone=phone;
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
	public UserType getUserType() {
		return UserType.getById(idUserType);
	}
	
	public void setUserType(UserType type) {
		idUserType=type.ordinal();
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
	
	
	
	
}
