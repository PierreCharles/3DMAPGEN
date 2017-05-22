package application;
import java.io.IOException;
import java.util.ResourceBundle;

import config.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application : extends Application This is entry point of this application
 * 
 * @author
 */
public class MainApplicationWindow extends Application {

	/**
	 * Entry point of the application : Start method for create and open a main window application.
	 * 
	 * @param primaryStage : main windows system
	 * @throws IOException : when FXML not found
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("properties.lang_" + Config.DEFAULT_LANG);
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainApplicationWindow.fxml"), bundle);
		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		Image icon = new Image(getClass().getResourceAsStream("/image/icone.png"));
		primaryStage.getIcons().add(icon);
		primaryStage.setTitle("3DMapGen");
		primaryStage.show();

	}


}
