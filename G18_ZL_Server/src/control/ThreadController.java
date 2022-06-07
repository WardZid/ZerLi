package control;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import entity.Complaint;
import entity.Email;
import entity.Order;
import entity.User;
import entity.MyMessage.MessageType;
import entity.Order.OrderStatus;

public class ThreadController {

	static Timer timer = new Timer();
	public static final Object lock = new Object();

	public static void ShippingTrackingfunction() {

		timer.schedule(new TimerTask() {
			ArrayList<Order> Lateorders;

			@Override
			public void run() {
				synchronized (lock) {

					Lateorders = DBController.getLateOrderDelivery();
					for (Order lateOrder : Lateorders) {
						System.out.println(lateOrder.toString());
						lateOrder.setIdOrderStatus(OrderStatus.DELIVERED.ordinal());

						DBController.updateOrderStatus(lateOrder);
						System.out.println("update " + lateOrder.getPrice() + " " + lateOrder.getIdCustomer());

						User currentUser = DBController.getUserBy("id_customer", lateOrder.getIdCustomer() + "").get(0);
						Email email = new Email(currentUser.getEmail(), "Late Delivery  ",
								" we are sorry the delivery was late you will get full refund-  " + lateOrder.getPrice()
										+ ".  \n");
						EmailController.sendEmail(email);
						
						
						DBController.updatePoint(lateOrder.getIdCustomer(), lateOrder.getPrice());

					}
				}
			}
		}, 0, 10000);// wait 0 ms before doing the action and do it evry 1000ms (1second)
	}

	public static void ComplainTrackingfunction(String ComplainId,String idUser) {

		Timer timer= new Timer();
		timer.schedule(new TimerTask() {
			int flagOnce = 0;

			@Override
			public void run() {
				ArrayList<Complaint> complaint;
				synchronized (lock) {
					if (flagOnce == 1)
						timer.cancel();
					if (flagOnce != 0) {
						complaint = DBController.checkComplaint(ComplainId);
						if (complaint.size()!=0) {
							User currentUser = DBController.getUserBy("id_user", idUser + "").get(0);
							System.out.println("i will send mail to currentUser " + currentUser);
							Email email = new Email(currentUser.getEmail(), "Reminder Complain ",
									" 24 hours have passed since the complaint ( " + ComplainId
											+ " ) this email for a Reminder\n");
							EmailController.sendEmail(email);	
						}
					} else
						flagOnce = 1;
				}
			}
		}, 0, 20000);// wait 0 ms before doing the action and do it evry 1000ms (1second)
					// 60* 1440*1000 =1 day
	}

	public static void CloseTrackingfunction() {

		timer.cancel();

	}

	
	
	
	
/*
  complaint = DBController.checkComplaint(ComplainId);
						System.out.println("i will complaint in " + complaint.size());
						if (complaint.size() != 0) {

							User currentUser = DBController.getUserBy("id_user", idUser + "").get(0);
							System.out.println("i will send mail to currentUser " + currentUser);
							Email email = new Email(currentUser.getEmail(), "Reminder Complain ",
									" 24 hours have passed since the complaint ( " + ComplainId
											+ " ) this email for a Reminder\n");
							EmailController.sendEmail(email);
							timer.cancel();
   *
   */
}
