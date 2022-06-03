package control;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import entity.Order;
import entity.Order.OrderStatus;

public class ThreadController {
	
	static Runnable helloRunnable = new Runnable() {
		ArrayList<Order> Lateorders;
		public void run() {
			Lateorders=DBController.getLateOrderDelivery();
			for (Order lateOrder : Lateorders) {
				System.out.println(lateOrder.toString());
				lateOrder.setIdOrderStatus(OrderStatus.REFUNDED.ordinal());
				DBController.updateOrderStatus(lateOrder);
				System.out.println("update "+lateOrder.getPrice()+" "+lateOrder.getIdCustomer());
				
				 DBController.updatePoint(lateOrder.getIdCustomer(),lateOrder.getPrice());
			}

		}
	};
	
	public static void Trackingfunction() {
		

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, 5, TimeUnit.SECONDS);
	}

}
