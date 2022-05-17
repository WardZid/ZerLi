package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Order;

public class DBController {

	// Database variables
	/**
	 * Holds the connection to the MySQL server so it can be referenced at any point
	 */
	private static Connection conn = null;

	private static Statement statement;

	/**
	 * URL with the DB's IP address and schema name
	 */
	public static String DBURL = "localhost/assignment3";
	final public static String DATABASE_URL="localhost/assignment3";

	/**
	 * DB username and pass with permissions to allow for data manipulation and
	 * retrieval
	 */
	public static String userDB = "root";
	public static String passDB = "0000";
	
	final public static String DATABASE_USER="root";
	final public static String DATABASE_PASSWORD="0000";

	public static void setDBInfo(String url,String user,String pass) {
		DBURL=url;
		userDB=user;
		passDB=pass;
	}
	
	public static void resetDBInfo() {
		setDBInfo(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	}
	
	/**
	 * Calls driver and establishes connection to MySQL server
	 * @throws Exception 
	 */
	public static void connectDB() throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			MainController.print(DBController.class, "Driver definition succeeded");
		} catch (Exception ex) {
			/* handle the error */
			MainController.printErr(DBController.class, "Driver definition failed");
			throw ex;
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://"+DBURL+"?serverTimezone=IST&useSSL=false", userDB, passDB);
			MainController.print(DBController.class, "SQL connection successfully established!");

			statement = conn.createStatement();
			MainController.print(DBController.class, "Statement created");

		} catch (SQLException ex) {
			/* handle any errors */

			MainController.printErr(DBController.class, "SQLException: " + ex.getMessage());
			MainController.printErr(DBController.class, "SQLState: " + ex.getSQLState());
			MainController.printErr(DBController.class, "VendorError: " + ex.getErrorCode());

			throw ex;
		}
	}

	/**
	 * Terminates connection to the MySQL server
	 */
	public static void disconnectDB() {
		try {
			conn.close();
			MainController.print(DBController.class, "DB Connection Terminated.");
		} catch (SQLException e) {
			MainController.printErr(DBController.class, "Error in DB Disconnection");
		}
	}

	// SQL Query Methods ******************************
/*
	public static ArrayList<Order> getAllOrders() {
		ArrayList<Order> orders = new ArrayList<>();

		ResultSet rs;

		try {
			rs = statement.executeQuery("SELECT * FROM orders"); // ---get all orders
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				orders.add(new Order(
						rs.getInt("order_number"), 
						rs.getDouble("price"), 
						rs.getString("branch"),
						rs.getString("order_date"), 
						rs.getString("date"), 
						rs.getString("color"),
						rs.getString("order_desc"), 
						rs.getString("greeting_card")));
			}
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orders;
	}

	public static Order getOrder(int orderNum) {
		Order order=null;
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM orders WHERE order_number=" + orderNum); // ---get all orders
			rs.next();
			order=new Order(rs.getInt("order_number"), rs.getDouble("price"), rs.getString("branch"),
					rs.getString("order_date"), rs.getString("date"), rs.getString("color"), rs.getString("order_desc"),
					rs.getString("greeting_card"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	
	public static Order updateOrder(Order order, String col, String newVal) {

		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE orders SET " + col + " = ? WHERE order_number = ?");
			ps.setString(1, newVal);
			ps.setInt(2, order.getOrderNum());
			ps.executeUpdate();
		} catch (SQLException e) {
			MainController.printErr(DBController.class, "Error updating Order");
			e.printStackTrace();
		}
		return getOrder(order.getOrderNum());
	}
*/
}
