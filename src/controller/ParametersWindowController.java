package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import parameter.Parameter;

/**
 * FXML Controller class of paramaters window
 * 
 * @author petit
 */
public class ParametersWindowController implements Initializable {

	@FXML
	private Button adjustWidthButton, adjustHeightButton, resetButton;
	@FXML
	private TextField heightField, widthField, heightMeshField, maxWidthPrintField, maxHeightPrintField;
	@FXML
	private Label errorLabel, adjustLabel;

	private double heightMesh, maxWidthPrint, maxHeightPrint, height, width;
	private Image image;
	private Stage stage;
	static Parameter parameters = new Parameter();
	private StringProperty heightProperty = new SimpleStringProperty();
	private StringProperty widthProperty = new SimpleStringProperty();
	private Double ratioHeight, ratioWidth;

	/**
	 * Constructor of the window
	 * 
	 * @param image
	 * @param parameters
	 */
	ParametersWindowController(Image image, Parameter parameters) {
		this.image = image;
		this.parameters = parameters;
	}

	/**
	 * Setter of the Height Property the heightProperty to set
	 * 
	 * @param heightProperty
	 */
	public void setHeightProperty(StringProperty heightProperty) {
		this.heightProperty = heightProperty;
	}

	/**
	 * Getter of the height property
	 * 
	 * @return the height property
	 */
	public StringProperty getHeightProperty() {
		return heightProperty;
	}

	/**
	 * Getter of the height
	 * 
	 * @returnthe height with the hauteur property
	 */
	public String getHeight() {
		return getHeightProperty().get();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle ressourceBundle) {
		ratioWidth = image.getWidth() / image.getHeight();
		ratioHeight = image.getHeight() / image.getWidth();
	}

	/**
	 * Method launch when close button is pressed
	 */
	@FXML
	public void CancelAction() {
		stage.close();
	}

	/**
	 * Method to adjust the height entered by users
	 */
	@FXML
	public void AdjustHeightAction() {
		if (heightField.getText().isEmpty()) {
			heightField.setStyle("-fx-control-inner-background: red");
		} else {
			width = Double.parseDouble(widthField.getText());
			height = width * ratioHeight;
			heightField.setText(String.valueOf(height));
			adjustHeightButton.setDisable(true);
		}
	}

	/**
	 * Method to adjust the with entered by users
	 */
	@FXML
	public void AdjustWidthAction() {
		if (widthField.getText().isEmpty()) {
			widthField.setStyle("-fx-control-inner-background: red");
		} else {
			height = Double.parseDouble(widthField.getText());
			width = height * ratioWidth;
			widthField.setText(String.valueOf(width));
			adjustWidthButton.setDisable(true);
		}
	}

	/**
	 * Method to reset wiht and height fields
	 */
	@FXML
	public void ResetAction() {
		widthField.setText("");
		heightField.setText("");
		adjustWidthButton.setDisable(false);
		adjustHeightButton.setDisable(false);
	}

	/**
	 * Method to validate parameters fields
	 */
	@FXML
	public void ValidateAction() {
		if (heightField.getText().isEmpty() || 
				widthField.getText().isEmpty() || 
				maxWidthPrintField.getText().isEmpty() || 
				maxHeightPrintField.getText().isEmpty()) {
			errorLabel.setVisible(true);
		} else {
			height = Double.parseDouble(heightField.getText());
			width = Double.parseDouble(widthField.getText());
			heightMesh = Double.parseDouble(heightMeshField.getText());
			maxWidthPrint = Double.parseDouble(maxWidthPrintField.getText());
			maxHeightPrint = Double.parseDouble(maxHeightPrintField.getText());
			if (height / width != ratioHeight) {
				adjustLabel.setVisible(true);
			} else {
				parameters.setImageHeight(height);
				parameters.setMeshHeight(heightMesh);
				parameters.setImageWidth(width);
				parameters.setMaxHeightOfPrint(maxHeightPrint);
				parameters.setMaxWidthOfPrint(maxWidthPrint);
				stage.close();
			}
		}
	}
}
