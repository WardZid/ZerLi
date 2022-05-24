package boundary.fxmlControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*
									1.
SELECT O.id_order , C.name_customer as name , C.phone_customer as phone , O.date_order , O.delivery_date_order as delivery_date , O.address_order as address , O.price_order as overall 
FROM assignment3.customer C , assignment3.order O
WHERE O.id_order_status = 0 AND C.id_customer = O.id_customer AND O.id_store = branchID

class WaitingOrder(int orderID, String name, String phone, String o_date, String request_date, String address, double overall)
 									
GET -> waiting/approve/branchID

public static ArrayList<WaitingOrder> getAmountOfEveryItem(String branch_id, String month, String year){
		ArrayList<WaitingOrder> waitings = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery(" #XYZ# ");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				waitings.add(new WaitingOrder(
						rs.getInt("id_order"),
						rs.getString("name")
						rs.getString("phone")
						rs.getString("date_order")
						rs.getString("delivery_date")
						rs.getString("address")
						rs.getDouble("overall")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return waitings;
}				
									
									2.
UPDATE assignment3.order O
SET O.id_order_status = 1
WHERE O.id_order = orderID;

UPDATE -> waiting/approve/orderID

public static int updateOrderToApproved(String orderID){
		int er; // number of effected rows
		try {
			er = statement.executeQuery(" #XYZ# ");		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return er;
}
									
									
 * */

public class BranchManagerOrdersController implements Initializable {

	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
	
	
	/* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
	
	
	
	/* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	/* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
	
	
	
	/* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	
	
}
