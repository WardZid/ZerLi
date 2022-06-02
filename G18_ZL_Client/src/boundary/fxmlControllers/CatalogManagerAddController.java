package boundary.fxmlControllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import boundary.ClientView;
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

public class CatalogManagerAddController implements Initializable {

	private CatalogManagerController cmc;

	public void setCatManCtrl(CatalogManagerController controller) {
		cmc = controller;
	}

	private Image image = null;
	private byte[] imageBytes;

	@FXML
	private ComboBox<String> catogryCB;

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
		catogryCB.getItems().addAll(CatalogManagerController.getCategoryType().keySet());
	}

	@FXML
	void onImagePressed() {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PNG Images", "*.png"));
		File f = fc.showOpenDialog(null);

		try {
			imageBytes=Files.readAllBytes(Paths.get(f.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image = new Image(f.toURI().toString());
		imageIV.setImage(image);

	}

	@FXML
	void onCancelPressed() {
		cmc.closeOverlayAdd();
	}

	@FXML
	void onConfirmPressed() {
		boolean allGood = true;
		nameTF.setStyle("-fx-text-fill: black");
		priceTF.setStyle("-fx-text-fill: black");
		saleTF.setStyle("-fx-text-fill: black");
		colorTF.setStyle("-fx-text-fill: black");
		catogryCB.setStyle("-fx-text-fill: black");

		String name = nameTF.getText();
		String price = priceTF.getText();
		String sale = saleTF.getText();
		String color = colorTF.getText();
		String category = catogryCB.getValue();
		String description = descriptionTA.getText();

		if (!isValid(name)) {
			allGood = false;
			nameTF.setStyle("-fx-text-fill: red");
		}

		if (!isValid(price)) {
			allGood = false;
			nameTF.setStyle("-fx-text-fill: red");
		}
		double priceD = 0.0;
		try {
			priceD = Double.parseDouble(price);
		} catch (NumberFormatException e) {
			allGood = false;
			priceTF.setStyle("-fx-text-fill: red");
		}

		int saleInt = 0;
		try {
			saleInt = Integer.parseInt(sale);
			if (saleInt < 0 || saleInt > 100) {
				saleInt = 0;
			}
		} catch (NumberFormatException e) {
			allGood = false;
			saleTF.setStyle("-fx-text-fill: red");
		}

		if (!isValid(color)) {
			allGood = false;
			colorTF.setStyle("-fx-text-fill: red");
		}

		if (category == null) {
			allGood = false;
			catogryCB.setStyle("-fx-text-fill: red");
		}

		if (allGood) {
			Item item = new Item(name, priceD, saleInt, category, color, description);
			item.setImageBytes(imageBytes);
			if ((boolean) MainController.getMyClient().send(MessageType.POST, "item", item)) {
				cmc.closeOverlayAdd();
			}
		} else {
			requiredText.setVisible(true);
		}

	}

	private boolean isValid(String s) {
		if (s == null || s.equals(""))
			return false;
		return true;
	}

}
