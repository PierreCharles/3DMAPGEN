package controller;

import static treatment.Export.createDirectory;
import static treatment.Export.exportToObj;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mesh.Mesh;
import parameter.Parameter;
import treatment.Treatment;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import config.Config;
import javafx.scene.layout.GridPane;

/**
 * MainWindowController Stage Is the controller of the main window stage
 * 
 * @author picharles
 */
public class MainApplicationWindowController extends Stage {

	@FXML
	GridPane gridPane;
	@FXML
	private ImageView viewImage;
	@FXML
	private Button saveButton, onTreatmentButton, openFileChooserButton;
	@FXML
	private MenuItem themePreference1, themePreference2, englishLanguagePreference, frenchLanguagePreference, close;
	@FXML
	private Menu file, edit, language;
	@FXML
    private ResourceBundle ressources;

	Image image;
	private String imagePath;
	private Parameter parameters = new Parameter();
	public List<Mesh> parcelsList = new ArrayList<>();
	public Mesh clipMesh = new Mesh();
	private File selectedFile;

	/**
	 * Initialize method
	 */
	public void initialize(URL location, ResourceBundle bundle) {	
		saveButton.setDisable(true);
		onTreatmentButton.setDisable(true);
	}
	
	/**
	 * Method to change propertie file language
	 * 
	 * @param lang : string language shortcut
	 */
	private void loadLang(String lang){
		Config.Current_Language = new Locale(lang);
		ressources = ResourceBundle.getBundle("properties.lang_" + Config.Current_Language);
		refreshTextApplication();
	}
	
	/**
	 * Method luanch when user change language to french
	 * 
	 * @param event
	 */
	@FXML
	private void changeLanguageFrench(ActionEvent event){
		loadLang("fr");
	}
	
	/**
	 * Method luanch when user change language to english
	 * 
	 * @param event
	 */
	@FXML
	private void changeLanguageEnglish(ActionEvent event){
		loadLang("en");
	}
	
	/**
	 * Method to set all text in current selected language
	 */
	private void refreshTextApplication(){
		openFileChooserButton.setText(ressources.getString("openFileChooserButton"));
		saveButton.setText(ressources.getString("saveButton"));
		onTreatmentButton.setText(ressources.getString("onTreatmentButton"));
		englishLanguagePreference.setText(ressources.getString("englishLanguagePreference"));
		frenchLanguagePreference.setText(ressources.getString("frenchLanguagePreference"));
		themePreference1.setText(ressources.getString("themePreference1"));
		themePreference2.setText(ressources.getString("themePreference2"));		
		close.setText(ressources.getString("close"));
		file.setText(ressources.getString("file"));
		edit.setText(ressources.getString("edit"));
		language.setText(ressources.getString("language"));
	}

	/**
	 * Set the button to true
	 */
	public void setButtonTrue() {
		saveButton.setDisable(false);
		onTreatmentButton.setDisable(false);
		openFileChooserButton.setDisable(false);
	}

	/**
	 * Method execute when user click on oppen button
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void openFileChooser(ActionEvent event) throws IOException 
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ouvrir");
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
		
		// TO DO -> Corrected progress bar window openProgressWindow();
		
		Treatment treatment = new Treatment();
		parcelsList = treatment.executeTreatment(selectedFile.toURI(), this.parameters);

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
	public void save(ActionEvent envent) throws IOException 
	{	
		int i = 1;
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Enregistrer");
		directoryChooser.setInitialDirectory(new File("C://"));

		File selectedSaveFile = directoryChooser.showDialog(this);
		if(Config.DEBUG){
			System.out.println(selectedSaveFile.toString());
		}
		if (selectedSaveFile != null) {
			createDirectory(selectedSaveFile.toString(), "Mesh");
			for (Mesh mesh : parcelsList) {
				exportToObj(mesh, selectedSaveFile.toString(), "Mesh", i);
				i++;
			}
			if(Config.DEBUG){
				System.out.println("Exportation terminée");
			}
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
		ParametersWindowController parametersWindowController = new ParametersWindowController(image, this.parameters);
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

	/*
	public void openProgressWindow() throws IOException {
		Stage progressStage = new Stage();

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/ProgressWindow.fxml"));

		progressStage.setTitle("Traitement ...");
		progressStage.setResizable(false);
	}
	*/

	/**
	 * Method using for select the theme 1
	 */
	@FXML
	public void changeThemePreference1() {
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
	public void changeThemePreference2() {
		gridPane.setStyle("-fx-background-color:#2c3e50");
		saveButton.getStyleClass().remove("basic");
		saveButton.getStyleClass().add("record-sales");
		onTreatmentButton.getStyleClass().remove("basic");
		onTreatmentButton.getStyleClass().add("record-sales");
		openFileChooserButton.getStyleClass().remove("basic");
		openFileChooserButton.getStyleClass().add("record-sales");
	}

}
