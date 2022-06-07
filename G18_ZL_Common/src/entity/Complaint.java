package entity;

import java.io.Serializable;
/**
 * complaint class so the employee can insert the complaint details to the ZLI system 
 * @author saher
 *
 */
public class Complaint implements Serializable{
	private static final long serialVersionUID = -4849768615516941203L;
	/**
	 * id for the complaint 
	 */
	private int idComplaint;
	/**
	 * id of the customer
	 */
	private int idCustomer;
	/**
	 * complaint status 
	 */
	private String status;
	/**
	 * complaint date 
	 */
	private String date;
	/** 
	 * refund that the employee give for the customer 
	 */
	private double refund;
	/**
	 * string that saved the complaint from the customer 
	 */
	private String complaint;
	/**
	 * response to the complaint 
	 */
	private String response;
	
	/**
	 * General constructor for all Parameters
	 * @param idComplaint
	 * @param idCustomer
	 * @param status
	 * @param date
	 * @param refund
	 * @param complaint
	 * @param response
	 */
	public Complaint(int idComplaint, int idCustomer, String status, String date, double refund, String complaint,
			String response) {
		this.idComplaint = idComplaint;
		this.idCustomer = idCustomer;
		this.status = status;
		this.date = date;
		this.refund = refund;
		this.complaint = complaint;
		this.response = response;
	}
	
	/**
	 * Constructor for new complaints from the customer
	 * @param idCustomer
	 * @param date
	 * @param complaint
	 */
	public Complaint(int idCustomer, String date, String complaint) {
		this.idCustomer = idCustomer;
		this.date = date;
		this.complaint = complaint;
	}

	public int getIdComplaint() {
		return idComplaint;
	}
	public void setIdComplaint(int idComplaint) {
		this.idComplaint = idComplaint;
	}
	public int getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getRefund() {
		return refund;
	}
	public void setRefund(double refund) {
		this.refund = refund;
	}
	public String getComplaint() {
		return complaint;
	}
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	@Override
	public String toString() {
		return "Complaint [idComplaint=" + idComplaint + ", idCustomer=" + idCustomer + ", status=" + status + ", date="
				+ date + ", refund=" + refund + ", complaint=" + complaint + ", response=" + response + "]";
	}
	
}
