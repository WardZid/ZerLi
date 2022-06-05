package entity;

import java.io.Serializable;

/**
 * @author wardz
 * email class to make sending email easy and effiecient
 *
 */
public class Email implements Serializable {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = 1742346171191388352L;
	
	/**
	 * The recipient of the email
	 */
	private String recipient;
	/**
	 * Subject in the header of the email
	 */
	private String subject;
	/**
	 * body portion of the email
	 */
	private String body;
	
	
	/**
	 * 
	 * @param recipient
	 * @param subject
	 * @param body
	 */
	public Email(String recipient, String subject, String body) {
		this.recipient = recipient;
		this.subject = subject;
		this.body = body;
	}
	/**
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Email [recipient=");
		builder.append(recipient);
		builder.append(", subject=");
		builder.append(subject);
		builder.append(", body=");
		builder.append(body);
		builder.append("]");
		return builder.toString();
	}
	
	

}
