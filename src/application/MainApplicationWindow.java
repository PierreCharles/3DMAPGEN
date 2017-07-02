package application;

import java.io.IOException;
import java.util.ResourceBundle;

import config.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main application : extends Application This is the second entry point of this
 * application
 * 
 * @author
 */
public class MainApplicationWindow extends Application {

	/**
	 * Entry point of the application : Start method for create and open a main
	 * window application.
	 * 
	 * @param primaryStage
	 *            : main windows system
	 * @throws IOException
	 *             : when FXML not found
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {

		ResourceBundle bundle = ResourceBundle.getBundle("properties.lang_" + Config.DEFAULT_LANG);
		Image icon = new Image(getClass().getResourceAsStream("/image/icone.png"));

		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainApplicationWindow.fxml"), bundle);

		Scene scene = new Scene(loader.load());

		primaryStage.setScene(scene);
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(1100);
		primaryStage.getIcons().add(icon);
		primaryStage.setTitle("3DMapGen");

		// Override closing method to close all Thread on close application event
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.show();
	}

}