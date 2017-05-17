import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application : extends Application This is entry point of this application
 * @author picharles
 */
public class MainApplication extends Application {

	/**
	 * Entry point of the application : Start method for create and open a main window application.
	 * @param primaryStage : main windows system
	 * @throws IOException : when FXML not found
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		Image icon = new Image(getClass().getResourceAsStream("icone.png"));
		primaryStage.getIcons().add(icon);
		primaryStage.setTitle("3DMapGen");
		primaryStage.show();
	}

	/**
	 * main method
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
