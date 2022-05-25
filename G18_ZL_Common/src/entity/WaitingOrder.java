package entity;

public class WaitingOrder {
	
	private int orderID;
	private String name; 
	private String phone; 
	private String o_date; 
	private String request_date; 
	private String address;
	private double overall;
	private String c_date;
	
	public WaitingOrder(int orderID, String name, String phone, String o_date, String request_date, String address, double overall){
		this.orderID = orderID;
		this.name = name;
		this.phone = phone;
		this.o_date = o_date;
		this.request_date = request_date;
		this.address = address;
		this.overall = overall;
		this.c_date = "0";
	}
	
	public WaitingOrder(int orderID, String name, String phone, String o_date, String request_date, String address,String c_date, double overall){
		this.orderID = orderID;
		this.name = name;
		this.phone = phone;
		this.o_date = o_date;
		this.request_date = request_date;
		this.address = address;
		this.overall = overall;
		this.c_date = c_date;
	}

	
	public String getC_date() {
		return c_date;
	}

	public void setC_date(String c_date) {
		this.c_date = c_date;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getO_date() {
		return o_date;
	}

	public void setO_date(String o_date) {
		this.o_date = o_date;
	}

	public String getRequest_date() {
		return request_date;
	}

	public void setRequest_date(String request_date) {
		this.request_date = request_date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getOverall() {
		return overall;
	}

	public void setOverall(double overall) {
		this.overall = overall;
	}
	
	
	
}
