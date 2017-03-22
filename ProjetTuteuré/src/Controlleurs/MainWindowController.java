package Controlleurs;

import Maillage.Maillage;
import Parametres.Parametres;
import TraitementImage.Charger;
import static TraitementImage.Decoupage.decouperImage;

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
import static TraitementImage.Exporter.exportToObj;
import static TraitementImage.Exporter.createDirectory;
import static TraitementImage.Exporter.exportAttacheToObj;
import static TraitementImage.Traitement.ParcelleToMaillage;
import static TraitementImage.Traitement.genererAttache;
import static TraitementImage.Traitement.miseAEchelle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.GridPane;

public class MainWindowController extends Stage {
    
    @FXML GridPane gridPane;
    private String imagePath;
    @FXML private ImageView viewImage;  
    //@FXML private Button traitementButton;
    //@FXML private MenuItem close;
    @FXML private Button enregistrer, traitementBtn, ouvrirBtn;
    @FXML private MenuItem preferences, preferences1;

    Image image;
    private Parametres para;


    
    
    public List<Maillage> listeParcelles = new ArrayList<>();
    public Maillage attache = new Maillage();
    
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
            ouvrirDialogue();
        }
        

    }
    
    @FXML 
    public void close(ActionEvent event) {
        Platform.exit();
    }
    
    @FXML
    public void onTraitement(ActionEvent envent) throws IOException {
        ouvrirProgressStage();
        Charger ch = new Charger(new File(selectedFile.toURI()));
        ch.ajouterImage();        
        
        List<BufferedImage> listeImages  = decouperImage(ch, para.getLargeurImage(), para.getHauteurImage(), para.getLargeurMaxImpression(), para.getHauteurMaxImpression());
         
        listeImages.forEach((image) -> {
            listeParcelles.add(ParcelleToMaillage(image, para.getHauteurMaillage(), para));
        });
        
        listeParcelles.forEach((parcelle) -> {
            System.out.println("mise à l'échelle parcelle");
            miseAEchelle(parcelle, listeImages.get(0), para);
        });
    
        System.out.println("mise à l'échelle attache");
        attache = genererAttache(listeImages.get(0));
        miseAEchelle(attache, listeImages.get(0), para);
        
        enregistrer.setDisable(false);
        traitementBtn.setDisable(true);
    }
    
    @FXML
    public void enregistrer(ActionEvent envent) throws IOException{
        int i = 1;
        DirectoryChooser dir = new DirectoryChooser();
        dir.setTitle("Enregistrer");
        dir.setInitialDirectory(new File("C://"));
        
        File selectedSaveFile = dir.showDialog(this);
        System.out.println(selectedSaveFile.toString());
        if(selectedSaveFile != null){
            createDirectory(selectedSaveFile.toString(), "Maillage");
            for(Maillage m : listeParcelles) {
                exportToObj(m, selectedSaveFile.toString(), "Maillage", i);
                i++;
            }
            exportAttacheToObj(selectedSaveFile.toString(), "Maillage", attache);
            System.out.println("Exportation terminée");
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
    
    public void ouvrirProgressStage() throws IOException {
        Stage progressStage = new Stage();
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Vues/Progression.fxml"));
       

        progressStage.setTitle("Traitement...");
        progressStage.setResizable(false);

    }
    @FXML 
    public void changerTheme1() {
        gridPane.setStyle("-fx-background-color:white");
        enregistrer.getStyleClass().remove("record-sales");
        enregistrer.getStyleClass().add("basic");
        traitementBtn.getStyleClass().remove("record-sales");
        traitementBtn.getStyleClass().add("basic");
        ouvrirBtn.getStyleClass().remove("record-sales");
        ouvrirBtn.getStyleClass().add("basic");
        
    }
    public void changerTheme2() {
        gridPane.setStyle("-fx-background-color:#2c3e50");
              enregistrer.getStyleClass().remove("basic");
        enregistrer.getStyleClass().add("record-sales");
        traitementBtn.getStyleClass().remove("basic");
        traitementBtn.getStyleClass().add("record-sales");
        ouvrirBtn.getStyleClass().remove("basic");
        ouvrirBtn.getStyleClass().add("record-sales");
        
    }
    
}
