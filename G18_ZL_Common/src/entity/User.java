package entity;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -3363120264501521428L;

	public enum UserType {
		CUSTOMER, BRANCH_MANAGER, CEO, STORE_WORKER, DELIVERY_WORKER, CUSTOMER_SUPPORT, SUPPORT_SPECIALIST,
		CATALOG_MANAGER;

		public static UserType getById(int id) {

			for (UserType type : values()) {
				if (type.ordinal() == id)
					return type;
			}
			return null;
		}
	}

	private int idUser;
	private int idUserType;
	private String username;
	private String password;
	public User(int idUser, int idUserType, String username, String password) {
		this.idUser = idUser;
		this.idUserType = idUserType;
		this.username = username;
		this.password = password;
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
	
	public UserType getUserType() {
		return UserType.getById(idUserType);
	}
	
	public void setUserType(UserType type) {
		idUserType=type.ordinal();
	}
	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", UserType=(" + idUserType +") "+getUserType()+ ", username=" + username + "]";
	}
	
	
}
