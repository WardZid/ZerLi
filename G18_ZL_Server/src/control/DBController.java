package control;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import boundary.ServerView;
import entity.AmountItem;
import entity.BuildItem;
import entity.Complaint;
import entity.Customer;
import entity.Customer.CustomerStatus;
import entity.DailyIncome;
import entity.Item;
import entity.Item.OrderItem;
import entity.Order;
import entity.Receipt;
import entity.Store;
import entity.Survey;
import entity.SurveyQuestion;
import entity.User;

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
	final public static String DATABASE_URL = "localhost/assignment3";

	/**
	 * DB username and pass with permissions to allow for data manipulation and
	 * retrieval
	 */
	public static String userDB = DBConfig.DBUSER;
	public static String passDB = DBConfig.DBPASS;

	final public static String DATABASE_USER = DBConfig.DBUSER;
	final public static String DATABASE_PASSWORD = DBConfig.DBUSER;

	public static void setDBInfo(String url, String user, String pass) {
		DBURL = url;
		userDB = user;
		passDB = pass;
	}

	public static void resetDBInfo() {
		setDBInfo(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
	}

	/**
	 * Calls driver and establishes connection to MySQL server
	 * 
	 * @throws Exception
	 */
	public static void connectDB() throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			ServerView.print(DBController.class, "Driver definition succeeded");
		} catch (Exception ex) {
			/* handle the error */
			ServerView.printErr(DBController.class, "Driver definition failed");
			throw ex;
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + DBURL + "?serverTimezone=IST&useSSL=false", userDB,
					passDB);
			ServerView.print(DBController.class, "SQL connection successfully established!");

			statement = conn.createStatement();
			ServerView.print(DBController.class, "Statement created");

		} catch (SQLException ex) {
			/* handle any errors */

			ServerView.printErr(DBController.class, "SQLException: " + ex.getMessage());
			ServerView.printErr(DBController.class, "SQLState: " + ex.getSQLState());
			ServerView.printErr(DBController.class, "VendorError: " + ex.getErrorCode());

			throw ex;
		}
	}

	/**
	 * Terminates connection to the MySQL server
	 */
	public static void disconnectDB() {
		try {
			conn.close();
			ServerView.print(DBController.class, "DB Connection Terminated.");
		} catch (SQLException e) {
			ServerView.printErr(DBController.class, "Error in DB Disconnection");
		}
	}

	// helper methods

	private static int resultSetSize(ResultSet rs) throws SQLException {
		int size = 0;
		if (rs != null) {
			rs.last(); // moves cursor to the last row
			size = rs.getRow(); // get row id
		}
		return size;
	}

	private static byte[] blobToMyFile(Blob blob) throws SQLException {
		if (blob == null)
			return null;
		int blobLength = (int) blob.length();
		if (blobLength == 0)
			return null;
		byte[] blobAsBytes = blob.getBytes(1, blobLength);

		blob.free();
		return blobAsBytes;

	}

	// SQL Query Methods ******************************
	public static ArrayList<SurveyQuestion> getAllSurves() {
		ResultSet rs;
		SurveyQuestion surveyBuild;
		ArrayList<SurveyQuestion> surviesList = new ArrayList<>();
		try {
			rs = statement.executeQuery("SELECT * FROM assignment3.questions;");
			while (rs.next()) {
				surveyBuild = new SurveyQuestion();
				for (int i = 3; i <= 8; i++)
					surveyBuild.getQuestion().add(rs.getString(i));
				surviesList.add(surveyBuild);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return surviesList;

	}


	public static User getUser(String username, String password) {
		ResultSet rs;
		try {
			rs = statement.executeQuery(
					"SELECT * FROM user WHERE username='" + username + "' AND password='" + password + "'");
			if (resultSetSize(rs) == 0)
				return null;
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				return new User(rs.getInt("id_user"), rs.getInt("id_user_type"), rs.getString("username"),
						rs.getString("password"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Order> getOrdersAll() {
		ArrayList<Order> orders = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM order"); // ---get all orders
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				orders.add(new Order(rs.getInt("id_order"), rs.getInt("id_customer"), rs.getInt("id_store"),
						rs.getInt("id_order_status"), rs.getDouble("price_order"), rs.getString("date_order"),
						rs.getString("delivery_date_order"), rs.getString("cancel_date_order"),
						rs.getString("address_order"), rs.getString("greeting_order"),
						rs.getString("description_order")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public static ArrayList<Order> getLateOrderDelivery() {
		ArrayList<Order> orders = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery(
					"SELECT * FROM assignment3.order WHERE id_order_status=2&& DATEDIFF(delivery_date_order,NOW()   ) <0;"); // ---get
			// all
			// orders
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				orders.add(new Order(rs.getInt("id_order"), rs.getInt("id_customer"), rs.getInt("id_store"),
						rs.getInt("id_order_status"), rs.getDouble("price_order"), rs.getString("date_order"),
						rs.getString("delivery_date_order"), rs.getString("cancel_date_order"),
						rs.getString("address_order"), rs.getString("greeting_order"),
						rs.getString("description_order")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public static ArrayList<Double> getIncomesInQuarter(String branch, String month, String year) {
		ArrayList<Double> allIncomesInQuarter = new ArrayList<>();
		ResultSet rs;
		try {
			
			for(int i=0;i<3;i++) {
				int currenIntegertMonth = Integer.parseInt(month)+i;
				rs = statement.executeQuery("SELECT sum(O.price_order) as sum FROM assignment3.order O WHERE Month(O.date_order) = "+currenIntegertMonth+" AND Year(O.date_order) ="+year+" AND O.id_store = "+branch);
				rs.beforeFirst(); // ---move back to first row
				while (rs.next()) {
					allIncomesInQuarter.add(new Double(rs.getDouble("sum")));
				}
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allIncomesInQuarter;
	}
	
	public static ArrayList<Order> getOrdersBy(String column, String value) {
		ArrayList<Order> orders = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM assignment3.order WHERE " + column + " = " + value);
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				orders.add(new Order(rs.getInt("id_order"), rs.getInt("id_customer"), rs.getInt("id_store"),
						rs.getInt("id_order_status"), rs.getDouble("price_order"), rs.getString("date_order"),
						rs.getString("delivery_date_order"), rs.getString("cancel_date_order"),
						rs.getString("address_order"), rs.getString("greeting_order"),
						rs.getString("description_order")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public static Order getOrderItemsFull(Order o) {
		try {
			ArrayList<OrderItem> orderItems = new ArrayList<>();
			ResultSet rs = statement.executeQuery("SELECT I.* ,OI.amount FROM item I, order_item OI WHERE OI.id_order="
					+ o.getIdOrder() + " AND OI.id_item=I.id_item");
			System.out.println("order: " + o.toString());
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {

				Item itemToAdd = new Item(rs.getInt("id_item"), rs.getString("name"), rs.getDouble("price"),
						rs.getInt("sale"), rs.getString("category"), rs.getString("color"), rs.getString("description"),
						rs.getString("status"), blobToMyFile(rs.getBlob("image")));
				orderItems.add(itemToAdd.new OrderItem(itemToAdd, rs.getInt("amount")));
			}
			rs.close();
			o.addOrderItems(orderItems);
		} catch (Exception e) {
			ServerView.printErr(DBController.class, "ERROR -> Unable to fetch all order items");
		}
		System.out.println(o.getItems().toString());
		o.addBuildItems(getFullBuildItemsBy("id_order", o.getIdOrder() + ""));
		return o;
	}

	public static ArrayList<Item> getItemsAll() {
		ArrayList<Item> items = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM item WHERE status='SHOWN'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				items.add(new Item(rs.getInt("id_item"), rs.getString("name"), rs.getDouble("price"), rs.getInt("sale"),
						rs.getString("category"), rs.getString("color"), rs.getString("description"),
						rs.getString("status"), blobToMyFile(rs.getBlob("image"))));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public static ArrayList<Item> getItemsBy(String column, String value) {
		ArrayList<Item> items = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM item WHERE " + column + "='" + value + "'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				items.add(new Item(rs.getInt("id_item"), rs.getString("name"), rs.getDouble("price"), rs.getInt("sale"),
						rs.getString("category"), rs.getString("color"), rs.getString("description"),
						rs.getString("status"), blobToMyFile(rs.getBlob("image"))));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public static ArrayList<Item> getItemsComplete() {
		ArrayList<Item> items = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM item");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				items.add(new Item(rs.getInt("id_item"), rs.getString("name"), rs.getDouble("price"), rs.getInt("sale"),
						rs.getString("category"), rs.getString("color"), rs.getString("description"),
						rs.getString("status"), blobToMyFile(rs.getBlob("image"))));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public static ArrayList<String> getCategoryAll() {
		ArrayList<String> category = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM category");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				category.add(rs.getString("category"));

			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return category;
	}

	public static ArrayList<String> getCategoryByType(String type) {
		ArrayList<String> types = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM category WHERE type='" + type + "'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				types.add(rs.getString("category"));

			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}

	public static ArrayList<String> getTypeAll() {
		ArrayList<String> types = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM item_type");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				types.add(rs.getString("type"));

			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}

	public static ArrayList<BuildItem> getBuildItemsAll() {
		ArrayList<BuildItem> buildItems = new ArrayList<>();
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM build_item");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				buildItems.add(new BuildItem(rs.getInt("id_build_item"), rs.getInt("id_order"), rs.getInt("amount")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buildItems;
	}

	public static ArrayList<BuildItem> getBuildItemsBy(String column, String value) {
		ArrayList<BuildItem> buildItems = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM build_item WHERE " + column + "='" + value + "'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				buildItems.add(new BuildItem(rs.getInt("id_build_item"), rs.getInt("id_order"), rs.getInt("amount")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buildItems;
	}

	public static ArrayList<BuildItem> getFullBuildItemsAll() {
		ArrayList<BuildItem> buildItems = new ArrayList<>();
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM build_item");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
//				buildItems.add(getItemInBuildAll(
//				new BuildItem(rs.getInt("id_build_item"), rs.getInt("id_order"), rs.getInt("amount"))));
				buildItems.add(new BuildItem(rs.getInt("id_build_item"), rs.getInt("id_order"), rs.getInt("amount")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// populate the build item itself
		for (BuildItem buildItem : buildItems) {
			buildItem = getItemInBuildAll(buildItem);
		}
		return buildItems;
	}

	/**
	 * 
	 * @param column to use in the "WHERE" clause
	 * @param value  to use with the column
	 * @return ArrayList<BuildItem> arraylist with build ite with all its items
	 */
	public static ArrayList<BuildItem> getFullBuildItemsBy(String column, String value) {
		ArrayList<BuildItem> buildItems = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM build_item WHERE " + column + "='" + value + "'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				buildItems.add(new BuildItem(rs.getInt("id_build_item"), rs.getInt("id_order"), rs.getInt("amount")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// populate the build item itself
		for (BuildItem buildItem : buildItems) {
			buildItem = getItemInBuildAll(buildItem);
		}
		return buildItems;
	}

	/**
	 * 
	 * @param buildItem the BuildItem to fill with the the item components
	 * @return BuildItem same BuildItem receive in parameter after filling with the
	 *         items in build
	 */
	public static BuildItem getItemInBuildAll(BuildItem buildItem) {
		ResultSet rs;
		try {
			rs = statement
					.executeQuery("SELECT I.*,IB.amount_in_build FROM item I,item_in_build IB WHERE IB.id_build_item="
							+ buildItem.getIdBuildItem() + " AND IB.id_item=I.id_item");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				buildItem.addItem(
						new Item(rs.getInt("id_item"), rs.getString("name"), rs.getDouble("price"), rs.getInt("sale"),
								rs.getString("category"), rs.getString("color"), rs.getString("description"),
								rs.getString("status"), blobToMyFile(rs.getBlob("image"))),
						rs.getInt("amount_in_build"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buildItem;
	}

	public static ArrayList<Customer> getCustomerAll() {
		ArrayList<Customer> customers = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM customer");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				customers.add(new Customer(rs.getInt("id_customer"), rs.getInt("id_customer_status"),
						rs.getInt("id_user"), rs.getString("name_customer"), rs.getString("email_customer"),
						rs.getString("phone_customer"), rs.getString("card_number"), rs.getDouble("point")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;
	}

	public static ArrayList<Customer> getCustomerBy(String column, String value) {
		ArrayList<Customer> customers = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM customer WHERE " + column + "='" + value + "'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				customers.add(new Customer(rs.getInt("id_customer"), rs.getInt("id_customer_status"),
						rs.getInt("id_user"), rs.getString("name_customer"), rs.getString("email_customer"),
						rs.getString("phone_customer"), rs.getString("card_number"), rs.getDouble("point")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;
	}

	public static ArrayList<Complaint> getComplaintsAll() {
		ArrayList<Complaint> complaints = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM complaint");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				complaints.add(new Complaint(rs.getInt("id_complaint"), rs.getInt("id_customer"),
						rs.getString("status_complaint"), rs.getString("date_complaint"), rs.getDouble("refund_amount"),
						rs.getString("complaint"), rs.getString("response")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return complaints;

	}

	public static ArrayList<Complaint> getComplaintsBy(String column, String value) {
		ArrayList<Complaint> complaints = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM complaint WHERE " + column + "='" + value + "'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				complaints.add(new Complaint(rs.getInt("id_complaint"), rs.getInt("id_customer"),
						rs.getString("status_complaint"), rs.getString("date_complaint"), rs.getDouble("refund_amount"),
						rs.getString("complaint"), rs.getString("response")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return complaints;

	}

	public static ArrayList<String> getStoreAll() {
		ArrayList<String> stores = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM store");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				stores.add(rs.getString("name_store"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stores;
	}

	public static ArrayList<Store> getStoreBy(String column, String value) {
		ArrayList<Store> stores = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM store WHERE " + column + "='" + value + "'");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				stores.add(Store.valueOf(rs.getString("name_store")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stores;
	}

	public static ArrayList<String> getQuestionsAll() {
		ArrayList<String> questions = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery("Select * FROM assignment3.question");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				questions.add(rs.getString("question"));
			}
		} catch (Exception e) {
			ServerView.printErr(DBController.class, e.getMessage());
		}

		return questions;
	}

	public static HashMap<String,HashMap<Integer,SurveyQuestion>> getAllSurvesYears(){
		HashMap<String,HashMap<Integer,SurveyQuestion>> yearsIdQuestions = new HashMap<String,HashMap<Integer,SurveyQuestion>>();
		SurveyQuestion sq;
		try {
			ResultSet rs  = statement.executeQuery(
					"SELECT year(s.date_survey) as year, q.id_question, q.question1, q.question2, q.question3, q.question4, q.question5, q.question6 FROM assignment3.survey s, assignment3.questions q"
							);
			while(rs.next()) {
				sq = new SurveyQuestion();
				System.out.println(rs.getString(0) + "aziz");
				System.out.println(rs.getString(1) + "hamed");
				if(yearsIdQuestions.get(rs.getString(1))==null)
				if(yearsIdQuestions.get(rs.getString(1)).get(rs.getInt(2))==null) {
				for(int i=0 ; i<6 ; i++)
					sq.getQuestion().add(rs.getString(i+3));
				HashMap<Integer,SurveyQuestion> idQuestion = new HashMap<Integer, SurveyQuestion>();
				idQuestion.put(rs.getInt(2), sq);
				yearsIdQuestions.put(rs.getString(1),idQuestion);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return yearsIdQuestions;
	}
	
	// Report get queries
	public static ArrayList<String> getMonthsInBranch(String idStore) {
		ArrayList<String> monthsYears = new ArrayList<>();
		try {
			ResultSet rs = statement.executeQuery(
					"SELECT distinct(Month(O.date_order)) as month,year(O.date_order) as year FROM assignment3.order O WHERE O.id_store="
							+ idStore);
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				monthsYears.add(rs.getString("month") + "/" + rs.getString("year"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return monthsYears;
	}

	/**
	 * @param branch_id
	 * @param month
	 * @param year
	 * @return ArrayList of receipts in the chosen month
	 */
	public static ArrayList<Receipt> getReceiptsOfMonth(String branch_id, String month, String year) {
		ArrayList<Receipt> receipts = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery(
					" SELECT name_customer as name, O.date_order  as date ,  (O.price_order) as income FROM assignment3.order O , assignment3.customer C  WHERE O.id_store ="
							+ branch_id + " AND Month(O.date_order) = " + month + " AND Year(O.date_order) =" + year
							+ "  and  O.id_customer=  C.id_customer ");

			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				receipts.add(new Receipt(rs.getString("name"), rs.getString("date"), rs.getDouble("income")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return receipts;
	}

	/**
	 * /**
	 * 
	 * @param branch
	 * @param month
	 * @param year
	 * @return ArrayList<Order> that contains the orders of a branch in a month of a
	 *         year.
	 */
	public static ArrayList<Order> getOrdersByBranchMonthYear(String branch, String month, String year) {

		ArrayList<Order> orders = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT * FROM assignment3.order O WHERE id_store = " + branch
					+ " AND (Month(O.date_order)) = " + month + " AND (Year(O.date_order)) = " + year);
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				orders.add(new Order(rs.getInt("id_order"), rs.getInt("id_customer"), rs.getInt("id_store"),
						rs.getInt("id_order_status"), rs.getDouble("price_order"), rs.getString("date_order"),
						rs.getString("delivery_date_order"), rs.getString("cancel_date_order"),
						rs.getString("address_order"), rs.getString("greeting_order"),
						rs.getString("description_order")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * @param branch_id
	 * @param month
	 * @param year
	 * @return ArrayList of the daily income of a specific store in a month of the
	 *         year
	 */
	public static ArrayList<DailyIncome> getSumOfDailyIncome(String branch_id, String month, String year) {

		ArrayList<DailyIncome> incomes = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery(
					"SELECT day(date_order) as day , sum(price_order) as income FROM assignment3.order  WHERE id_store ="
							+ branch_id + " AND Month(date_order) = " + month + "  AND Year(date_order) =" + year
							+ "  GROUP BY Day(date_order) ORDER BY day ");

			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				incomes.add(new DailyIncome(rs.getInt("day"), rs.getDouble("income")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return incomes;
	}

	public static ArrayList<AmountItem> getAmountOfEveryItem(String branchID, String month, String year) {
		ArrayList<AmountItem> amounts = new ArrayList<>();
		ResultSet rs;
		try {
			rs = statement.executeQuery("SELECT I.name , sum(OI.amount) as amount" + " FROM order_item OI , item I"
					+ " WHERE I.id_item = OI.id_item AND OI.id_order IN (" + " SELECT id_order"
					+ "	FROM assignment3.order O" + " WHERE id_store = " + branchID + " AND (Month(O.date_order)) = "
					+ month + " AND (Year(O.date_order)) = " + year + ")" + " GROUP BY name");
			rs.beforeFirst(); // ---move back to first row
			while (rs.next()) {
				amounts.add(new AmountItem(rs.getString("name"), rs.getInt("amount")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amounts;
	}

	// INSERT QUERIES (POST)*******************************************************

	public static boolean insertItem(Item item) {
		int linesChanged = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO item (`name`, `price`, `sale`, `category`,`color`,`description`,`image`) VALUES(?,?,?,?,?,?,?)");
			ps.setString(1, item.getName());
			ps.setDouble(2, item.getPrice());
			ps.setInt(3, item.getSale());
			ps.setString(4, item.getCategory());
			ps.setString(5, item.getColor());
			ps.setString(6, item.getDescription());
			ps.setBytes(7, item.getImageBytes());
			linesChanged = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ServerView.printErr(DBController.class, "Unable to add new item: " + item.toString());
		}
		if (linesChanged == 0)
			return false;
		return true;
	}

	public static boolean insertComplaint(Complaint c) {
		int linesChanged = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO complaint (`id_customer`, `status_complaint`, `date_complaint`, `complaint`) VALUES(?,?,?,?)");
			ps.setInt(1, c.getIdCustomer());
			ps.setString(2, "OPEN");
			ps.setString(3, c.getDate());
			ps.setString(4, c.getComplaint());
			linesChanged = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ServerView.printErr(DBController.class, "Unable to add new complaint: " + c.toString());
		}
		if (linesChanged == 0)
			return false;
		return true;
	}

	public static boolean insertSurvey(Survey s) {
		int linesChanged = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO survey (`id_question`,`id_store`,`date_survey`,`answer1`,`answer2`,`answer3`,`answer4`,`answer5`,`answer6`) VALUES(?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1,s.getIdQuestion());
			ps.setInt(2, s.getIdStore());
			ps.setString(3, s.getDateSurvey());
			for(int i=0 ; i<6 ; i++)
			ps.setInt(i+4, s.getAnswers().get(i));
			
			linesChanged = ps.executeUpdate();

//			ResultSet generatedKeys = ps.getGeneratedKeys();
//			int idSurvey = generatedKeys.getInt(0);
			ps.close();
			//System.out.println(idSurvey);
			// for (SurveyQuestion sq : s.getQuestions()) {
//			insertSurveyAnswer(idSurvey, s.getSurveyQuestion());
			// }
		} catch (SQLException e) {
			e.printStackTrace();
			ServerView.printErr(DBController.class, "Unable to add new survey: " + s.toString());
			return false;
		}
		if (linesChanged == 0)
			return false;
		return true;

	}
	/*
	 * public static boolean insertSurvey(Survey s) { int linesChanged = 0; try {
	 * PreparedStatement ps = conn.
	 * prepareStatement("INSERT INTO survey (`date_survey`, `id_store`) VALUES(?,?)"
	 * , Statement.RETURN_GENERATED_KEYS); ps.setString(1, s.getDateSurvey());
	 * ps.setInt(2, s.getIdStore()); linesChanged = ps.executeUpdate();
	 * 
	 * ResultSet generatedKeys = ps.getGeneratedKeys(); int idSurvey =
	 * generatedKeys.getInt(0); ps.close(); System.out.println(idSurvey); for
	 * (SurveyQuestion sq : s.getQuestions()) { insertSurveyAnswer(idSurvey, sq); }
	 * } catch (SQLException e) { e.printStackTrace();
	 * ServerView.printErr(DBController.class, "Unable to add new survey: " +
	 * s.toString()); return false; } if (linesChanged == 0) return false; return
	 * true; }
	 */

//	public static boolean insertSurveyAnswer(int idSurvey, SurveyQuestion sq) {
//		int linesChanged = 0;
//		try {
//			PreparedStatement ps = conn.prepareStatement(
//					"INSERT INTO survey_question (`id_survey`, `id_question`,`answer`) VALUES(?,?,?)");
//			ps.setInt(1, idSurvey);
//			ps.setInt(2, sq.getIdQuestion());
//			for (int i = 3; i < 9; i++)
//				ps.setInt(i, sq.getAnswer().get(i - 3));
//			linesChanged = ps.executeUpdate();
//			ps.close();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			ServerView.printErr(DBController.class, "Unable to add new survey_question: " + sq.toString());
//			return false;
//		}
//		if (linesChanged == 0)
//			return false;
//		return true;
//	}

	// UPDATE QUERIES****************************************************

	public static void logOutAll() {
		try {
			statement.executeUpdate("UPDATE user SET logged_in=FALSE");
		} catch (SQLException e) {
			ServerView.printErr(DBController.class, e.getMessage());
		}
	}

	public static boolean updateLogIn(User u) {
		int linesChanged = 0;
		try {
			linesChanged = statement.executeUpdate("UPDATE user SET logged_in=TRUE WHERE id_user=" + u.getIdUser());
		} catch (SQLException e) {
			ServerView.printErr(DBController.class, e.getMessage());
		}
		if (linesChanged == 0)
			return false;
		return true;
	}

	public static boolean updateLogOut(User u) {
		int linesChanged = 0;
		try {
			linesChanged = statement.executeUpdate("UPDATE user SET logged_in=FALSE WHERE id_user=" + u.getIdUser());
		} catch (SQLException e) {
			ServerView.printErr(DBController.class, e.getMessage());
		}
		if (linesChanged == 0)
			return false;
		return true;
	}

	public static ArrayList<Item> updateEditItem(Item item) {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE assignment3.item " + "SET name=?, " + "price=?, "
					+ "sale=?, " + "category=?, " + "color=?, " + "description=?, " + "image=? " + "WHERE id_item=?");

			ps.setString(1, item.getName());
			ps.setDouble(2, item.getPrice());
			ps.setInt(3, item.getSale());
			ps.setString(4, item.getCategory());
			ps.setString(5, item.getColor());
			ps.setString(6, item.getDescription());
			ps.setBytes(7, item.getImageBytes());
			ps.setInt(8, item.getIdItem());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			ServerView.printErr(DBController.class, e.getMessage());
			e.printStackTrace();
		}
		return getItemsBy("id_item", item.getIdItem() + "");
	}
	
	public static ArrayList<Item> updateItemStatus(Item item) {
		try {
			PreparedStatement ps=conn.prepareStatement("UPDATE assignment3.item SET status=? WHERE id_item=?");
			ps.setString(1, item.getStatus());
			ps.setInt(2, item.getIdItem());
			
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return getItemsBy("id_item", item.getIdItem()+"");
	}

	public static ArrayList<Customer> updateCustomerStatusOne(Customer c, CustomerStatus status) {

		try {
			statement.executeUpdate("UPDATE customer SET id_customer_status=" + status.ordinal() + " WHERE id_customer="
					+ c.getIdCustomer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getCustomerBy("id_customer", c.getIdCustomer() + "");
	}

	public static ArrayList<Complaint> updateComplaint(Complaint c) {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE complaint SET status_complaint='CLOSED', refund_amount=?, response=? WHERE id_complaint=?");
			ps.setDouble(1, c.getRefund());
			ps.setString(2, c.getResponse());
			ps.setInt(3, c.getIdComplaint());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getComplaintsBy("id_complaint", c.getIdComplaint() + "");
	}

	public static ArrayList<Order> updateOrderStatus(Order o) {
		try {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE assignment3.order SET id_order_status=? WHERE id_order=?");
			ps.setInt(1, o.getIdOrderStatus());
			ps.setInt(2, o.getIdOrder());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getOrdersBy("id_order", o.getIdOrder() + "");
	}

	public static boolean updatePoint(int idCustomer, double newPoint) {
		try {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE assignment3.customer SET point=?+point WHERE id_customer=?");
			ps.setDouble(1, newPoint);
			ps.setInt(2, idCustomer);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	

}
