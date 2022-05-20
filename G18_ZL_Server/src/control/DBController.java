package control;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.BuildItem;
import entity.Complaint;
import entity.Item;
import entity.Order;
import entity.User;
import javafx.scene.image.Image;

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
	
	//helper methods
	
	private static int resultSetSize(ResultSet rs) throws SQLException {
		int size =0;
		if (rs != null) 
		{
		  rs.last();    // moves cursor to the last row
		  size = rs.getRow(); // get row id 
		}
		return size;
	}
	private static Image blobToImage(Blob blob) {
		try {
			return new Image(blob.getBinaryStream());
		} catch (SQLException e) {
			MainController.printErr(DBController.class, "Could not load blob to image from mysql");
		}
		return null;
		
	}

	// SQL Query Methods ******************************
	
	public static User getUser(String username,String password) throws Exception{
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM user WHERE usernme="+username+" AND password="+password);
			if(resultSetSize(rs)==0)
				throw new Exception("No such user!");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				return new User(
						rs.getInt("id_user"),
						rs.getInt("id_user_type"),
						rs.getString("username"),
						rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Order> getOrdersAll(){
		ArrayList<Order> orders = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM order"); // ---get all orders
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				orders.add(new Order(
						rs.getInt("id_order"),
						rs.getInt("id_customer"),
						rs.getInt("id_store"),
						rs.getInt("id_order_status"),
						rs.getDouble("price_order"),
						rs.getString("date_order"),
						rs.getString("delivery_date_order"),
						rs.getString("address_order"),
						rs.getString("greeting_order"),
						rs.getString("description_order")));
			}
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}
	
	public static ArrayList<Order> getOrdersBy(String column,String value){
		ArrayList<Order> orders = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM order WHERE "+column+"="+value);
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				orders.add(new Order(
						rs.getInt("id_order"),
						rs.getInt("id_customer"),
						rs.getInt("id_store"),
						rs.getInt("id_order_status"),
						rs.getDouble("price_order"),
						rs.getString("date_order"),
						rs.getString("delivery_date_order"),
						rs.getString("address_order"),
						rs.getString("greeting_order"),
						rs.getString("description_order")));
			}
			return orders;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}
	
	public static ArrayList<Item> getItemsAll(){
		ArrayList<Item> items = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM item");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				items.add(new Item(
						rs.getInt("id_item"),
						rs.getInt("id_category"),
						rs.getString("name"),
						rs.getDouble("price"),
						rs.getInt("sale"),
						rs.getString("color"),
						rs.getString("description"),
						blobToImage(rs.getBlob("image"))));
			return items;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public static ArrayList<Item> getItemsBy(String column,String value){
		ArrayList<Item> items = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM item WHERE "+column+"="+value);
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				items.add(new Item(
						rs.getInt("id_item"),
						rs.getInt("id_category"),
						rs.getString("name"),
						rs.getDouble("price"),
						rs.getInt("sale"),
						rs.getString("color"),
						rs.getString("description"),
						blobToImage(rs.getBlob("image"))));
			return items;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public static ArrayList<BuildItem> getBuildItemsBy(String column,String value){
		ArrayList<BuildItem> buildItems = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM build_item WHERE "+column+"="+value);
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				buildItems.add(new BuildItem(
						rs.getInt("id_build_item"),
						rs.getInt("id_order"),
						rs.getInt("amount")));
			return buildItems;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buildItems;
	}
	
//	public static ArrayList<ItemInBuild> getBuildItemInBuildAll(int idBuildItem,int idOrder){
//		ArrayList<ItemInBuild> itemsInBuild = new ArrayList<>();
//		ResultSet rs;
//		try {
//			rs = statement.executeQuery("SELECT * FROM build_item WHERE "+column+"="+value);
//			rs.beforeFirst(); // ---move back to first row
//			while (rs.next()) {
//				itemsInBuild.add();
//			return itemsInBuild;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return itemsInBuild;
//	} 
	
	
	
	public static ArrayList<Complaint> getComplaintsAll() {
		ArrayList<Complaint> complaints = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM complaint");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				complaints.add(new Complaint(
						rs.getInt("id_complaint"),
						rs.getInt("id_customer"),
						rs.getString("status_complaint"),
						rs.getString("date_complaint"),
						rs.getDouble("refund_amount"),
						rs.getString("complaint"),
						rs.getString("response")));
			}
			return complaints;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return complaints;
		
	}
	
	public static ArrayList<Complaint> getComplaintsBy(String column, String value) {
		ArrayList<Complaint> complaints = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM complaint WHERE "+column+"="+value);
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				complaints.add(new Complaint(
						rs.getInt("id_complaint"),
						rs.getInt("id_customer"),
						rs.getString("status_complaint"),
						rs.getString("date_complaint"),
						rs.getDouble("refund_amount"),
						rs.getString("complaint"),
						rs.getString("response")));
			}
			return complaints;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return complaints;
		
	}
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
