package Controlleurs;

import Maillage.Maillage;
import Parametres.Parametres;
import TraitementImage.Charger;
import TraitementImage.Traitement;
import static TraitementImage.Traitement.traitementNiveauDeGris;
import static TraitementImage.Exporter.ExportToObj;

import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;

public class MainWindowController extends Stage {
    
   // @FXML MenuItem ouvrir;
    private String imagePath;
    @FXML private ImageView viewImage;
    @FXML private Label etat;
    //@FXML private Label traitementLabel;
    @FXML private Button traitementButton;
    @FXML private MenuItem close;
    @FXML private Button enregistrer;
    @FXML private Button traitementBtn;
    @FXML private Button ouvrirBtn;
    Image image;
    private Parametres para;
    public  Maillage m;
    
    public void initialize() {
        enregistrer.setDisable(true);
        traitementBtn.setDisable(true);
        para = new Parametres();
        
    }
    public void setButtonTrue() {
        enregistrer.setDisable(false);
        traitementBtn.setDisable(false);
        ouvrirBtn.setDisable(false);
    }
    
    private File selectedFile;
    @FXML
    public void ouvrir(ActionEvent event) throws IOException {
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("ouvrir");
   
        imageChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        selectedFile = imageChooser.showOpenDialog(this);
        if(selectedFile != null) {
            imagePath = selectedFile.toURI().toString();
            image = new Image(imagePath);
            viewImage.setImage(image);
            traitementBtn.setDisable(false);   
            ouvrirBtn.setDisable(true);           
        }
        ouvrirDialogue();

    }
    
    @FXML 
    public void close(ActionEvent event) {
        Platform.exit();
    }
    
    @FXML
    public void onTraitement(ActionEvent envent) {
 
        Charger ch = new Charger(new File(selectedFile.toURI()));
        m = new Maillage();
        ch.ajouterImage();
        traitementNiveauDeGris(ch, m, 50.0, 0);
        enregistrer.setDisable(false);
        traitementBtn.setDisable(true);
    }
    @FXML
    public void enregistrer(ActionEvent envent) throws IOException{
        DirectoryChooser dir = new DirectoryChooser();
        dir.setTitle("Enregistrer");
        dir.setInitialDirectory(new File("C://"));
        
        File selectedSaveFile = dir.showDialog(this);
        System.out.println(selectedSaveFile.toString());
        if(selectedSaveFile != null){
            ExportToObj(m,selectedSaveFile.toString());
        }
        this.setButtonTrue();
    }

    public void ouvrirDialogue() throws IOException {
            Stage paraStage = new Stage();
            ParametresController controller = new ParametresController(image, para);
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Vues/Parametres.fxml"));
            loader.setController(controller);
            controller.setStage(paraStage);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            paraStage.setTitle("Paramètres");
            paraStage.setResizable(false);
            paraStage.setScene(scene);
            paraStage.showAndWait();
    }
}
