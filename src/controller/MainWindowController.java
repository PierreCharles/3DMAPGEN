package controller;

import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mesh.Mesh;
import parameter.Parameter;
import treatments.Load;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;

import static treatments.Cut.cutImage;
import static treatments.Export.createDirectory;
import static treatments.Export.exportToObj;
import static treatments.Treatment.parcelToMesh;
import static treatments.Treatment.scalling;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.GridPane;

/**
 * MainWindowController Stage Is the controller of the main window stage
 * 
 * @author picharles
 */
public class MainWindowController extends Stage {

	@FXML
	GridPane gridPane;
	private String imagePath;
	@FXML
	private ImageView viewImage;
	@FXML
	private Button saveButton, onTreatmentButton, openFileChooserButton;
	@FXML
	private MenuItem preferences1, preferences2;

	Image image;
	private Parameter parameter;
	public List<Mesh> parcelsList = new ArrayList<>();
	public Mesh clipMesh = new Mesh();

	/**
	 * Initialize method
	 */
	public void initialize() {
		saveButton.setDisable(true);
		onTreatmentButton.setDisable(true);
		parameter = new Parameter();
	}

	/**
	 * Set the button to true
	 */
	public void setButtonTrue() {
		saveButton.setDisable(false);
		onTreatmentButton.setDisable(false);
		openFileChooserButton.setDisable(false);
	}

	private File selectedFile;

	/**
	 * Method execute when user click on oppen button
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void openFileChooser(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("ouvrir");

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
				new FileChooser.ExtensionFilter("All Files", "*.*"));
		selectedFile = fileChooser.showOpenDialog(this);
		if (selectedFile != null) {
			imagePath = selectedFile.toURI().toString();
			image = new Image(imagePath);
			viewImage.setImage(image);
			onTreatmentButton.setDisable(false);
			openFileChooserButton.setDisable(true);
			openParametersWindow();
		}
	}

	/**
	 * Method launched when user press close button
	 * 
	 * @param event
	 */
	@FXML
	public void close(ActionEvent event) {
		Platform.exit();
	}

	/**
	 * Method launch when user press Treatment button
	 * 
	 * @param envent
	 * @throws IOException
	 */
	@FXML
	public void onTreatement(ActionEvent envent) throws IOException {
		openProgressWindow();
		Load ch = new Load(new File(selectedFile.toURI()));
		ch.addImage();

		List<BufferedImage> listeImages = cutImage(ch, parameter.getImageWidth(), parameter.getImageHeight(),
				parameter.getMaxWidthOfPrint(), parameter.getMaxHeightOfPrint());

		listeImages.forEach((image) -> {
			parcelsList.add(parcelToMesh(image, parameter.getMeshHeight(), parameter));
		});

		parcelsList.forEach((parcelle) -> {
			System.out.println("Mise à l'échelle parcelle");
			scalling(parcelle, listeImages.get(0), parameter);
		});

		saveButton.setDisable(false);
		onTreatmentButton.setDisable(true);
	}

	/**
	 * Method launch when user press save button
	 * 
	 * @param envent
	 * @throws IOException
	 */
	@FXML
	public void save(ActionEvent envent) throws IOException {
		int i = 1;
		DirectoryChooser dir = new DirectoryChooser();
		dir.setTitle("Enregistrer");
		dir.setInitialDirectory(new File("C://"));

		File selectedSaveFile = dir.showDialog(this);
		System.out.println(selectedSaveFile.toString());
		if (selectedSaveFile != null) {
			createDirectory(selectedSaveFile.toString(), "Mesh");
			for (Mesh mesh : parcelsList) {
				exportToObj(mesh, selectedSaveFile.toString(), "Mesh", i);
				i++;
			}
			System.out.println("Exportation terminée");
		}
		this.setButtonTrue();
	}

	/**
	 * Method using for open Parameters window
	 * 
	 * @throws IOException
	 */
	public void openParametersWindow() throws IOException {
		Stage parametersStage = new Stage();
		ParametersWindowController parametersWindowController = new ParametersWindowController(image, parameter);
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/ParametersWindow.fxml"));
		loader.setController(parametersWindowController);
		parametersWindowController.setStage(parametersStage);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		parametersStage.setTitle("Paramètres");
		parametersStage.setResizable(false);
		parametersStage.setScene(scene);
		parametersStage.showAndWait();
	}

	public void openProgressWindow() throws IOException {
		Stage progressStage = new Stage();

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/ProgressWindow.fxml"));

		progressStage.setTitle("Traitement ...");
		progressStage.setResizable(false);
	}

	/**
	 * Method using for select the theme 1
	 */
	@FXML
	public void changeTheme1() {
		gridPane.setStyle("-fx-background-color:white");
		saveButton.getStyleClass().remove("record-sales");
		saveButton.getStyleClass().add("basic");
		onTreatmentButton.getStyleClass().remove("record-sales");
		onTreatmentButton.getStyleClass().add("basic");
		openFileChooserButton.getStyleClass().remove("record-sales");
		openFileChooserButton.getStyleClass().add("basic");
	}

	/**
	 * Method using for select the theme 2
	 */
	@FXML
	public void changeTheme2() {
		gridPane.setStyle("-fx-background-color:#2c3e50");
		saveButton.getStyleClass().remove("basic");
		saveButton.getStyleClass().add("record-sales");
		onTreatmentButton.getStyleClass().remove("basic");
		onTreatmentButton.getStyleClass().add("record-sales");
		openFileChooserButton.getStyleClass().remove("basic");
		openFileChooserButton.getStyleClass().add("record-sales");
	}

}
