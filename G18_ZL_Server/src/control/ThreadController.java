package control;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import entity.Order;
import entity.Order.OrderStatus;

public class ThreadController {
	
	static Timer timer = new Timer();
	
	public static void Trackingfunction() {
		
		timer.schedule(new TimerTask() {
			ArrayList<Order> Lateorders;
			  @Override
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
			}, 0, 1000);//wait 0 ms before doing the action and do it evry 1000ms (1second)
	}

	public static void CloseTrackingfunction() {
	 
		
		timer.cancel();
		
	}

}
