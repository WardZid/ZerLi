package control;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import boundary.ServerView;
import entity.Email;

public class EmailController {
	private static final String myEmail = "ZerLeeG18@outlook.com"; // your email
	private static final String password = "SemesterProject18"; // your email password
	private static final String sender = "ZerLeeG18@outlook.com"; // Insert Your email again

	public static boolean sendEmail(Email email) {
		if(email.getSubject()==null || email.getSubject().equals(""))
			return false;
		
		final Session newSession = Session.getInstance(EmailController.mailProperties(), new Authenticator() {
			@Override
			// password authentication
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myEmail, password);
			}
		});
		// MimeMessage is used to create the email message
		try {
			final Message msg = new MimeMessage(newSession);
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipient()));
			msg.setFrom(new InternetAddress(sender));
			msg.setSubject(email.getSubject()); // email subject
			msg.setText(email.getBody()); // The content of email
			msg.setSentDate(new Date());
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						ServerView.printErr(EmailController.class,"Sending email to: "+email.getRecipient());
						Transport.send(msg);
						ServerView.printErr(EmailController.class,"Your Email has been successfully sent to: "+email.getRecipient());
					} catch (MessagingException e) {
						ServerView.printErr(EmailController.class,"Email Sending Failed"); // failed
						e.printStackTrace();
					}// Transport the email
					
				}
				
			}).start();
			return true;
		} catch (final MessagingException e) { // exception to catch the errors
			ServerView.printErr(EmailController.class,"Email Sending Failed"); // failed
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * The permanent set of properties containing string keys, the following
	 * setting the properties for SMPT function
	 * @return
	 */
	public static Properties mailProperties() {
		final Properties mailProp = new Properties();
		mailProp.put("mail.smtp.host", "smtp.office365.com");
		mailProp.put("mail.smtp.post", "587");// 587;
		mailProp.put("mail.smtp.auth", true);
		mailProp.put("mail.smtp.starttls.enable", true);
		mailProp.put("mail.smtp.ssl.protocols", "TLSv1.2");
		return mailProp;
	}

}