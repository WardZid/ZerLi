package boundary.fxmlControllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import control.DBController;
import control.MainController;
import control.ServerController;
import control.ThreadController;
import entity.ClientConnection;
import entity.MyMessage;
import entity.MyMessage.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import ocsf.server.ConnectionToClient;

public class ServerViewController implements Initializable {

	/**
	 * Server instance
	 */
	private static ServerController serverCon;

	private static ObservableList<ClientConnection> clientsObservableList = FXCollections
			.<ClientConnection>observableArrayList();

	// FXML Components************************

	@FXML
	private AnchorPane mainAnchor;

	@FXML
	private HBox mainHBox;

	@FXML
	private TableView<ClientConnection> tblClients;

	@FXML
	private TableColumn<ClientConnection, String> tblColHost;

	@FXML
	private TableColumn<ClientConnection, String> tblColIP;

	@FXML
	private GridPane gridTextInputs;

	@FXML
	private TextField txtIP;
	@FXML
	private TextField txtPort;
	@FXML
	private TextField txtDBURL;
	@FXML
	private TextField txtUser;
	@FXML
	private TextField txtPass;

	@FXML
	private Button btnReset;
	@FXML
	private Button btnConnect;
	@FXML
	private Button btnDisconnect;

	@FXML
	private Button btnImport;

	@FXML
	private Button btnLogOut;

	@FXML
	private ScrollPane logSP;
	@FXML
	private TextFlow logTextFlow;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serverCon = MainController.getServer();

		logTextFlow.heightProperty().addListener(observable -> logSP.setVvalue(1D));

		initTextFields();
		initTable();

	}

	private void initTextFields() {
		txtIP.setText(serverCon.getIP());
		txtPort.setText(serverCon.getPort() + "");
		txtDBURL.setText(DBController.DBURL);
		txtUser.setText(DBController.userDB);
		txtPass.setText(DBController.passDB);

		txtIP.setDisable(true);
		txtPort.setDisable(true);

	}

	/**
	 * sets up the tableview for the customers to show up
	 */
	private void initTable() {
		tblColIP.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("ip"));
		tblColHost.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("host"));
		tblClients.setItems(clientsObservableList);
	}

	@FXML
	public void onConnect() {
		try {
			DBController.setDBInfo(txtDBURL.getText(), txtUser.getText(), txtPass.getText());
			MainController.getServer().startServer();

			enableButtons(false, false, true, true, true);

			gridTextInputs.setDisable(true); 

		 	 ThreadController.Trackingfunction();
		 	 
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
 

	@FXML
	public void onDisconnect() {

		ThreadController.CloseTrackingfunction();
		MainController.getServer().stopServer();
		clientsObservableList.clear();

		enableButtons(true, true, false, false, false);

		gridTextInputs.setDisable(false);
	}

	@FXML
	void onReset() {
		DBController.resetDBInfo();
		initTextFields();
	}

	@FXML
	void onImport() {
		DBController.importUsers();
	}

	@FXML
	void onLogOut() {
		MainController.getServer().sendToAllClients(new MyMessage(null, 0, MessageType.INFO, "global/logout", null));
		DBController.logOutAll();
	}

	private void enableButtons(boolean reset, boolean connect, boolean disconnect, boolean imports, boolean logOut) {
		btnReset.setDisable(!reset);
		btnConnect.setDisable(!connect);
		btnDisconnect.setDisable(!disconnect);
		btnImport.setDisable(!imports);
		btnLogOut.setDisable(!logOut);
	}

	/**
	 * Prints normal log message
	 * 
	 * @param out Log message
	 */
	public void printLog(String out) {
		Text t = new Text("\n" + out);
		print(t);
	}

	/**
	 * Prints Red error message
	 * 
	 * @param out Log message
	 */
	public void printErrLog(String out) {
		Text t = new Text("\n" + out);
		t.setFill(Color.RED);
		print(t);
	}

	/**
	 * Adds text to add log on the JavaFX thread
	 * 
	 * @param t Text to add to log on the FX's thread
	 */
	private void print(Text t) {
		javafx.application.Platform.runLater(() -> logTextFlow.getChildren().add(t));
	}

	// Static Components

	public static ObservableList<ClientConnection> getCientsObservableList() {
		return clientsObservableList;
	}

	public static void addClient(ConnectionToClient client) {
		clientsObservableList.add(new ClientConnection(client));

	}

	public static void removeClient(ConnectionToClient client) {
		clientsObservableList.remove(new ClientConnection(client));

	}

}
