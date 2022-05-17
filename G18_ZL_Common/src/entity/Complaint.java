package entity;

public class Complaint {
	public int Complaint_id;
	public int Customer_id;
	public String Status;
	public String date;
	public int refund;
	public Complaint(int complaint_id, int customer_id, String status, String date, int refund) {
		Complaint_id = complaint_id;
		Customer_id = customer_id;
		Status = status;
		this.date = date;
		this.refund = refund;
	}
	public int getComplaint_id() {
		return Complaint_id;
	}
	public void setComplaint_id(int complaint_id) {
		Complaint_id = complaint_id;
	}
	public int getCustomer_id() {
		return Customer_id;
	}
	public void setCustomer_id(int customer_id) {
		Customer_id = customer_id;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getRefund() {
		return refund;
	}
	public void setRefund(int refund) {
		this.refund = refund;
	}
	
}
