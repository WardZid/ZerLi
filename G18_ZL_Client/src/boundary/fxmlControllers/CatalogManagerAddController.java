package boundary.fxmlControllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import control.MainController;
import entity.Item;
import entity.MyMessage.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * the controller that adds new item
 * 
 * @author hamza
 *
 */
public class CatalogManagerAddController implements Initializable {

	private CatalogManagerController cmc;

	public void setCatManCtrl(CatalogManagerController controller) {
		cmc = controller;
	}

	private Image image = null;
	private byte[] imageBytes;

	@FXML
	private ComboBox<String> categoryCB;

	@FXML
	private TextField colorTF;

	@FXML
	private Button confirmBtn;

	@FXML
	private TextArea descriptionTA;

	@FXML
	private ImageView imageIV;

	@FXML
	private TextField nameTF;

	@FXML
	private TextField priceTF;

	@FXML
	private Text requiredText;

	@FXML
	private TextField saleTF;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		categoryCB.getItems().addAll(CatalogManagerController.getCategoryType().keySet());
	}

	/**
	 * action to do when an image is pressed
	 */
	@FXML
	void onImagePressed() {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PNG Images", "*.png"));
		fc.getExtensionFilters().add(new ExtensionFilter("JPG Images", "*.jpg"));
		File f = fc.showOpenDialog(null);

		if (f == null)
			return;
		try {
			imageBytes = Files.readAllBytes(Paths.get(f.getPath()));
			image = new Image(f.toURI().toString());
			imageIV.setImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * action to do when cancel is pressed
	 */
	@FXML
	void onCancelPressed() {
		cmc.cancelOverlay();
	}

	/**
	 * action to do whem confirm is pressed
	 */
	@FXML
	void onConfirmPressed() {
		boolean allGood = true;
		nameTF.setStyle("-fx-background-color: 1de9b6");
		priceTF.setStyle("-fx-background-color: 1de9b6");
		saleTF.setStyle("-fx-background-color: 1de9b6");
		colorTF.setStyle("-fx-background-color: 1de9b6");
		categoryCB.setStyle("-fx-background-color: 1de9b6");

		String name = nameTF.getText();
		String price = priceTF.getText();
		String sale = saleTF.getText();
		String color = colorTF.getText();
		String category = categoryCB.getValue();
		String description = descriptionTA.getText();

		if (!isValid(name)) {
			allGood = false;
			nameTF.setStyle("-fx-background-color: #ff5000");
		}

		if (!isValid(price)) {
			allGood = false;
			nameTF.setStyle("-fx-background-color: #ff5000");
		}
		double priceD = 0.0;
		try {
			priceD = Double.parseDouble(price);
		} catch (NumberFormatException e) {
			allGood = false;
			priceTF.setStyle("-fx-background-color: #ff5000");
		}

		int saleInt = 0;
		try {
			saleInt = Integer.parseInt(sale);
			if (saleInt < 0 || saleInt > 100) {
				saleInt = 0;
			}
		} catch (NumberFormatException e) {
			allGood = false;
			saleTF.setStyle("-fx-background-color: #ff5000");
		}

		if (!isValid(color)) {
			allGood = false;
			colorTF.setStyle("-fx-background-color: #ff5000");
		}

		if (category == null) {
			allGood = false;
			categoryCB.setStyle("-fx-background-color: #ff5000");
		}

		if (allGood) {
			Item item = new Item(name, priceD, saleInt, category, color, description);
			item.setImageBytes(imageBytes);
			if ((boolean) MainController.getMyClient().send(MessageType.POST, "item", item)) {
				cmc.closeOverlay();
			}
		} else {
			requiredText.setVisible(true);
		}

	}

	// HELPER METHODS
	/**
	 * Checks if an input is good enough to continue
	 * 
	 * @param s String to check
	 * @return false id the string is null or empty
	 */

	private boolean isValid(String s) {
		if (s == null || s.equals(""))
			return false;
		return true;
	}

}
