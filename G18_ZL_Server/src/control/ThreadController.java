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

						DBController.updatePoint(lateOrder.getIdCustomer(), lateOrder.getPrice());

					}
				}
			}
		}, 0, 5000);// wait 0 ms before doing the action and do it evry 1000ms (1second)
	}

	public static void ComplainTrackingfunction(int ComplainId,int idUser) {

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
							User currentUser =  DBController.getUserBy("id_user", idUser+"").get(0);
							Email email = new Email(currentUser.getEmail(),"Reminder Complain " ," 24 hours have passed since the complaint ( "+ComplainId+" ) this email for a Reminder\n"  );
							//EmailController.sendEmail(email);		
						}
					} else
						flagOnce = 1;
				}
			}
		}, 0, 2000);// wait 0 ms before doing the action and do it evry 1000ms (1second)
					// 60* 1440*1000 =1 day
	}

	public static void CloseTrackingfunction() {

		timer.cancel();

	}

}
