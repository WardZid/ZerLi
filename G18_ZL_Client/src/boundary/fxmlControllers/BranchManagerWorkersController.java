
package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.Store;
import entity.User;
import entity.User.UserType;
import entity.Worker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * @author wardz BranchManager manages his worker's roles in his branch only
 */
public class BranchManagerWorkersController implements Initializable {

	/**
	 * currently selected user from the listview
	 */
	private User selectedUser;

	/**
	 * worker map that holds an entries with a user as key and his worker account as
	 * a value;
	 */
	private HashMap<User, Worker> workersMap = new HashMap<>();
	@FXML
	private Text emailText;

	@FXML
	private Text idUserText;

	@FXML
	private Text idWorkerText;

	@FXML
	private Text nameText;

	@FXML
	private Text passwordText;

	@FXML
	private Text phoneText;

	@FXML
	private Text storeText;

	@FXML
	private Text usernameText;

	@FXML
	private ImageView workerIV;

	@FXML
	private ComboBox<String> roleCB;

	@FXML
	private ListView<String> workerLV;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		workerIV.setImage(new Image("boundary/media/workers.jpg"));
		fetchWorkers();
		initList();

		for (UserType type : UserType.values())
			if (type.isWorker())
				roleCB.getItems().add(type.toString().replace("_", " "));

		workerLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				int idUser = Integer.parseInt(newValue.split(" - ")[1]);
				for (User user : workersMap.keySet())
					if (user.getIdUser() == idUser) {
						setInfo(user);
						break;
					}

			}
		});

		roleCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null)
					return;
				UserType newType = UserType.valueOf(newValue.replace(" ", "_"));
				selectedUser.setUserType(newType);
				User after = ((ArrayList<User>) MainController.getMyClient().send(MessageType.UPDATE, "user/type",
						selectedUser)).get(0);
				if (after.getUserType() != newType)
					roleCB.getSelectionModel().select(oldValue);
			}
		});
	}

	// Helper Methods

	/**
	 * fetch users and worker accounts from server to put into a hashmap
	 */
	@SuppressWarnings("unchecked")
	private void fetchWorkers() {
		ArrayList<Worker> workers = (ArrayList<Worker>) MainController.getMyClient().send(MessageType.GET,
				"worker/by/id_store/" + ClientConsoleController.getWorker().getIdStore(), null);
		for (Worker worker : workers) {
			if (worker.getIdWorker() != ClientConsoleController.getWorker().getIdWorker()) {
				ArrayList<User> users = (ArrayList<User>) MainController.getMyClient().send(MessageType.GET,
						"user/by/id_worker/" + worker.getIdWorker(), null);
				if (users.size() > 0)
					workersMap.put(users.get(0), worker);
			}
		}
	}

	/**
	 * Populates listView with worker ID numbers
	 */
	private void initList() {
		workerLV.getItems().clear();
		for (User user : workersMap.keySet()) {
			workerLV.getItems().add("ID Num - " + user.getIdUser());
		}
	}

	private void setInfo(User user) {
		clearFields();

		selectedUser = user;
		
		idUserText.setText(user.getIdUser() + "");
		nameText.setText(user.getName());
		usernameText.setText(user.getUsername());
		passwordText.setText(user.getPassword());
		emailText.setText(user.getEmail());
		phoneText.setText(user.getPhone());
		idWorkerText.setText(workersMap.get(user).getIdWorker() + "");
		storeText.setText(Store.getById(workersMap.get(user).getIdStore()).toString());
		
		roleCB.getSelectionModel().select(user.getUserType().toString());
		roleCB.setDisable(false);

	}

	/**
	 * clears all the info fields to prepare for a new info entry
	 */
	private void clearFields() {
		selectedUser = null;

		idUserText.setText("---");
		nameText.setText("---");
		usernameText.setText("---");
		passwordText.setText("---");
		emailText.setText("---");
		phoneText.setText("---");
		idWorkerText.setText("---");
		storeText.setText("---");

		roleCB.getSelectionModel().clearSelection();
	}

}
