package control;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import entity.Complaint;
import entity.Email;
import entity.Order;
import entity.User;
import entity.Order.OrderStatus;
/**
 * thread class to tracking the server 
 * 
 * @author ameer
 *
 */
public class ThreadController {
/**
 * ShippingTracking is a timer 
 */
	static Timer ShippingTracking = new Timer();
	/**
	 * locker to synchronize
	 */
	public static final Object lock = new Object();
/**
 * the system checking that the order is delivered in the correct time  
 */
	public static void ShippingTrackingfunction() {
		   ShippingTracking = new Timer();
		ShippingTracking.schedule(new TimerTask() {
			ArrayList<Order> Lateorders;
            
			@Override
			public void run() {
				 
				synchronized (lock) {

					Lateorders = DBController.getLateOrderDelivery();
					for (Order lateOrder : Lateorders) {
						System.out.println(lateOrder.toString());
						lateOrder.setIdOrderStatus(OrderStatus.DELIVERED.ordinal());

						DBController.updateOrderStatus(lateOrder);
						 
						User currentUser = DBController.getUserBy("id_customer", lateOrder.getIdCustomer() + "").get(0);
						Email email = new Email(currentUser.getEmail(), "Late Delivery  ",
								" we are sorry the delivery was late you will get full refund-  " + lateOrder.getPrice()
										+ ".  \n");
						EmailController.sendEmail(email);
		 
						DBController.updatePoint(lateOrder.getIdCustomer(), lateOrder.getPrice());
		 
					}
				}
			}
		}, 0, 20000);// works every 20 seconds for checking a late orders
	}
/**
 * function that tracking the store worker that he has response to the customer in 24 hour 
 * @param ComplainId complane id 
 * @param idUser user id 
 */
	public static void ComplainTrackingfunction(String ComplainId,String idUser) {

		Timer timer= new Timer();
		timer.schedule(new TimerTask() {
			int flagOnce = 0;

			@Override
			public void run() {
				ArrayList<Complaint> complaint;
				synchronized (lock) {
					 
					if (flagOnce == 1) {
						timer.cancel();
						 
					}
					if (flagOnce != 0) {
						complaint = DBController.checkComplaint(ComplainId);
						if (complaint.size()!=0) {
							User currentUser = DBController.getUserBy("id_user", idUser + "").get(0);
							 Email email = new Email(currentUser.getEmail(), "Reminder Complain ",
									" 24 hours have passed since the complaint ( " + ComplainId
											+ " ) this email for a Reminder\n");
							EmailController.sendEmail(email);	
						}
					} else
						flagOnce = 1;
				}
			}
		}, 0, 60* 1440*1000);// wait 0 ms before doing the action and do it evry 1000ms (1second)
					// 60* 1440*1000 =1 day
	}

	 
/**
 * function to stop the timer
 */
	public static void stopTimers() {
		ShippingTracking.cancel();
		 System.exit(0);  
		
	}

 
}
