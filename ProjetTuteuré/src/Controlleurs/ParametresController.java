/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlleurs;

import Parametres.Parametres;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author petit
 */
public class ParametresController implements Initializable {
    @FXML private Button annuler, valider;
    @FXML private TextField hauteurField, largeurField,hauteurMaillageField, largeurMaxImpressionField, hauteurMaxImpressionField;
    private double hauteurMaillage, largeurMaxImpression, hauteurMaxImpression, hauteur, largeur;
    private Image image;
    private Stage thisStage;
    static Parametres para = new Parametres();
//    private DoubleProperty hauteur = new SimpleDoubleProperty();
//    
//    public DoubleProperty hauteurProperty () {
//        return hauteur;
//    }
//    public double getHauteur () {
//        return hauteurProperty().get();
//    }
    
    ParametresController(Image image, Parametres para) {
        this.image = image; 
        this.para = para;
    }
    public void setStage(Stage thisStage) {
        this.thisStage = thisStage;
    }   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //      hauteur.textProperty().bindBidirectional((Property<String>) largeur);
        setTextField();
    }    
    @FXML
    public void setTextField() {
//        hauteur.set(image.getHeight());
//       // largeur = image.getWidth();
//        System.out.println(hauteur);
//       // System.out.println(largeur);
//        
//        hauteurField.textProperty().bind(hauteurProperty().asString());
////       largeurField.setText(Double.toString(largeur));
////       hauteurField.textProperty().bindBidirectional(largeurField.textProperty());
        
    } 
    @FXML 
    public void Annuler() {
        thisStage.close();
    }
    @FXML 
    public void Valider() {
        hauteur = Double.parseDouble(hauteurField.getText());
        largeur = Double.parseDouble(largeurField.getText());
        hauteurMaillage = Double.parseDouble(hauteurMaillageField.getText());
        largeurMaxImpression = Double.parseDouble(largeurMaxImpressionField.getText());
        hauteurMaxImpression = Double.parseDouble(hauteurMaxImpressionField.getText()); 

        para.setHauteurImage(hauteur);
        para.setHauteurMaillage(hauteurMaillage);
        para.setLargeurImage(largeur);
        para.setHauteurMaxImpression(hauteurMaxImpression);
        para.setLargeurMaxImpression(largeurMaxImpression);
        thisStage.close();
    }
}
