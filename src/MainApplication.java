import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/MainWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Image icon = new Image(getClass().getResourceAsStream("icone.png"));
        stage.getIcons().add(icon);
        stage.setTitle("3DMapGen");
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
