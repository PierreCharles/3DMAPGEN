package TraitementImage;

import Maillage.Maillage;
import Maillage.Sommet;
import Maillage.Face;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;

public class Traitement {

    /**
     *
     */
    public StringProperty etat;
    
    /**
     * permet de créer un sommet du socle à partir un sommet du maillage de la carte
     * @param s : Sommet du maillage de la carte
     * @return : Sommet du socle en dessous du paramètre s
     */
    public static Sommet pointDuSocle(Sommet s) {
        return new Sommet(s.getX(), 0.0, s.getZ());
    }
    
    /*
    vérifie si le sommet passé en paramètre existe déjà
    si oui renvoie l'existant
    sinon renvoie le nouveau créé
    */
    public static Sommet creationSommet(Double i, Double j, Double resolution, BufferedImage image) {
        int pixel;
        int rouge;
        int vert;
        int bleu;
        int moyenne;
        int k = (int) Math.round(i);
        int l = (int) Math.round(j);
        Double hauteur;
        pixel = image.getRGB(k,l);
        rouge = (pixel >> 16) & 0xff;
        vert = (pixel >> 8) & 0xff;
        bleu = (pixel) & 0xff;
        moyenne = 255-(rouge+vert+bleu)/3;
        hauteur = resolution*moyenne;
        Sommet sommet = new Sommet(i, hauteur, j);
        return sommet;
    }
    
    /**
     * Retourne une liste de sommets compris dans la zone limitée par depX, finX, depZ, finY
     * fonctionne uniquement pour les sommets du socle y compris ceux creusés
     * @param m : maillage contenant tous les points
     * @param depX : abscisse de la borne gauche de la zone
     * @param finX : abscisse de la borne droite de la zone
     * @param depZ : "profondeur" de la borne haute de la zone
     * @param finZ : "profondeur" de la borne basse de la zone
     * @return : une liste de sommets qui contient tous ceux présents dans la zone
     */
    public static ArrayList<Sommet> recupererZone(Maillage m, Double depX, Double finX, Double depZ, Double finZ) {
        ArrayList<Sommet> zone = new ArrayList<>();
        for (int i = 0; i < m.getListeSocle().size(); i++) {
             if (m.getListeSocle().get(i).getX().compareTo(depX) >= 0 
                     && m.getListeSocle().get(i).getX().compareTo(finX) <= 0) {
                if (m.getListeSocle().get(i).getZ().compareTo(depZ) >= 0 
                        && m.getListeSocle().get(i).getZ().compareTo(finZ) <= 0) {
                    zone.add(m.getListeSocle().get(i));
                }
            }
        }
        return zone;
    }
    
    
    /**
     * Permet de creuser un rectangle sur le socle, centré avec une certaine longueur et une certaine largeur
     * @param m : maillage contenant tout les sommets
     * @param image : image chargée
     * @param longueur : longueur (Z) du rectangle
     * @param largeur : largeur (X) du rectangle
     */
    public static void creuserRectangle(Maillage m, BufferedImage image, Double longueur, Double largeur) { 
        //rajouter test si longueur et largeur inférieur à celles de l'image
        List<Sommet> listeADeplacer = recupererZone(m, (image.getWidth()-largeur)/2 , (image.getWidth()+largeur)/2, (image.getHeight()-longueur)/2, (image.getHeight()+longueur)/2);
        for (int i = 0; i< listeADeplacer.size(); i++) {
            listeADeplacer.get(i).deplacerY(5.0);
        }
    }
    
    public static void traitementNiveauDeGris(BufferedImage image, Maillage m, Double max, int min) {
        
        int rouge;
        int vert;
        int bleu;
        int moyenne;
        int pixel = 0;
        int couleur = 0;
        Double hauteurSommet = 0.0;
        Double resolution = max/256;
        Sommet sommetHG;
        Sommet socleHG;
        Sommet sommetHD;
        Sommet socleHD;
        Sommet sommetBG;
        Sommet socleBG;
        Sommet sommetBD;
        Sommet socleBD;
        
        for (Double i = 0.0; i < image.getWidth()- 1; i++) {
            for(Double j = 0.0; j < image.getHeight() - 1; j++) {
                
                sommetHG = creationSommet(i,j,resolution, image);
                m.getEnsembleSommets().put(sommetHG.getId(), sommetHG);
                
                sommetHD = creationSommet(i+1, j, resolution, image);
                m.getEnsembleSommets().put(sommetHD.getId(), sommetHD);
                
                sommetBG = creationSommet(i, j+1, resolution, image);
                m.getEnsembleSommets().put(sommetBG.getId(), sommetBG);
                
                sommetBD = creationSommet(i+1, j+1, resolution, image);
                m.getEnsembleSommets().put(sommetBD.getId(), sommetBD);
                
                Face triangleG = new Face(sommetHG.getId(),sommetHD.getId(),sommetBG.getId());
                Face triangleD = new Face(sommetHD.getId(),sommetBD.getId(),sommetBG.getId());
                
                /*SOCLE*/
                
                //ajoute le sommet i,j dans l'ensemble de tout les sommets
                //crée le sommet du socle à une hauteur de 0
                socleHG = pointDuSocle(sommetHG);
                m.getEnsembleSommets().put(socleHG.getId(), socleHG);
                m.getListeSocle().add(socleHG);
                
                //ajoute le sommet i+1,j dans l'ensemble de tout les sommets
                //crée le sommet du socle à une hauteur de 0
                socleHD = pointDuSocle(sommetHD);
                m.getEnsembleSommets().put(socleHD.getId(), socleHD);
                m.getListeSocle().add(socleHD);
                
                 //ajoute le sommet i,j+1 dans l'ensemble de tout les sommets
                //crée le sommet du socle à une hauteur de 0
                socleBG = pointDuSocle(sommetBG);
                m.getEnsembleSommets().put(socleBG.getId(), socleBG);
                m.getListeSocle().add(socleBG);
                
                //ajoute le sommet i+1,j+1 dans l'ensemble de tout les sommets
                //crée le sommet du socle à une hauteur de 0
                socleBD = pointDuSocle(sommetBD);
                m.getEnsembleSommets().put(socleBD.getId(), socleBD);
                m.getListeSocle().add(socleBD);
                
                //crée les faces à partir des sommets du socle
                Face socleTriangleG = new Face(socleHG.getId(), socleHD.getId(), socleBG.getId());
                Face socleTriangleD = new Face(socleHD.getId(), socleBD.getId(), socleBG.getId());
                
               //ajout des faces
                m.getEnsembleFaces().add(triangleG);
                m.getEnsembleFaces().add(triangleD);
                //ajout des faces du socle
                m.getEnsembleFaces().add(socleTriangleG);
                m.getEnsembleFaces().add(socleTriangleD);
                
                // COTES
                    // Cote gauche
                if(i == 0) {
                    Face cote1 = new Face(sommetHG.getId(), sommetBG.getId(), socleHG.getId());
                    Face cote2 = new Face(sommetBG.getId(), socleHG.getId(), socleBG.getId());
                    
                    m.getEnsembleFaces().add(cote1);
                    m.getEnsembleFaces().add(cote2);
                }
                    // Cote haut
                if(j == 0) {
                    Face cote1 = new Face(sommetHG.getId(), sommetHD.getId(), socleHG.getId());
                    Face cote2 = new Face(sommetHD.getId(), socleHD.getId(), socleHG.getId());
                    
                    m.getEnsembleFaces().add(cote1);
                    m.getEnsembleFaces().add(cote2);
                }
                    // Cote droit
                if(i == (image.getWidth()- 2)) {
                    Face cote1 = new Face(sommetHD.getId(), sommetBD.getId(), socleHD.getId());
                    Face cote2 = new Face(sommetBD.getId(), socleBD.getId(), socleHD.getId());
                    
                    m.getEnsembleFaces().add(cote1);
                    m.getEnsembleFaces().add(cote2);
                }
                    // Cote bas
                if(j == (image.getHeight()- 2)) {
                    Face cote1 = new Face(sommetBG.getId(), sommetBD.getId(), socleBG.getId());
                    Face cote2 = new Face(sommetBD.getId(), socleBD.getId(), socleBG.getId());
                    
                    m.getEnsembleFaces().add(cote1);
                    m.getEnsembleFaces().add(cote2);
                }
            }
        }
        creuserRectangle(m, image, image.getHeight() * 0.8, image.getWidth() * 0.8);
    }
}