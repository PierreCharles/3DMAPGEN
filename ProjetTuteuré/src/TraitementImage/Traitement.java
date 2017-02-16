package TraitementImage;

import Maillage.Maillage;
import Maillage.Sommet;
import Maillage.Face;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javafx.beans.property.StringProperty;

public class Traitement {

    /**
     *
     */
    public StringProperty etat;
     
    /**
     * permet de créer un sommet du socle à partir un sommet du maillage de la carte
     * @param s : Sommet du maillage de la carte
     * @param img: parcelle à mailler
     * @return : Sommet du socle en dessous du paramètre s
     */
    public static Sommet pointDuSocle(Sommet s, BufferedImage img) {
        double h=0.0;
        if (doitEtreRemonte(img, s))
            h = 2.0;
        return new Sommet(s.getZ(), h, s.getX());
    }
    
    /**
     * Permet d'obtenir la hauteur d'un pixel donné de l'image chargée en fonction de ses coordonées
     * @param ligne Coordonnée y
     * @param colonne Coordonnée x
     * @param resolution Résolution de la hauteur en fonction des niveau de gris
     * @param image Image chargée dans le logiciel
     * @return La hauteur qu'il faut attribuer au sommet du maillage correspondant au pixel traitée
     */
    public static double getHauteurPixel(double ligne, double colonne, double resolution, BufferedImage image){
        int pixel;
        int rouge;
        int vert;
        int bleu;
        int moyenne;
        double hauteur;
        int x = (int) Math.ceil(colonne);
        int y = (int) Math.ceil(ligne);
        pixel = image.getRGB(x,y);

        rouge = (pixel >> 16) & 0xff;
        vert = (pixel >> 8) & 0xff;
        bleu = (pixel) & 0xff;
        moyenne = 255-(rouge+vert+bleu)/3;
        hauteur = resolution*moyenne;
        
        return hauteur;
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
    public static ArrayList<Sommet> recupererZone(Maillage m, double depX,double finX, double depZ, double finZ) {
        ArrayList<Sommet> zone = new ArrayList<>();
        for (int i = 0; i < m.getListeSocle().size(); i++) {
             if (m.getListeSocle().get(i).getX() >= depX && m.getListeSocle().get(i).getX() <= finX) {
                if (m.getListeSocle().get(i).getZ() >= depZ && m.getListeSocle().get(i).getZ() <= finZ) {
                    zone.add(m.getListeSocle().get(i));
                }
            }
        }
        return zone;
    }
    
    /**
     * Permet de retourner une liste de Sommet correspondant à une ligne de l'image
     * Pour avoir uniquement les sommets correspondant a l'image de base, nous récupérons 1 sommet sur 2 car l'autre correspond au sommet du socle
     * @param indiceLigne Indice de la ligne que l'on souhaite récupérer
     * @param L Largeur de l'image
     * @param H Hauteur de l'image
     * @param ensembleSommet Liste de tous les Sommet du Maillage
     * @return La liste des Sommet de la ligne
     */
    public static List<Sommet> vecteurSommet (int indiceLigne, int L, int H, TreeMap ensembleSommet){
        int debut = indiceLigne * (L-1);
        int fin = (indiceLigne * (L-1)) + (L-1);
        List<Sommet> vecteurSommet = new ArrayList<>();
        
        for(int i = debut; i<=fin; i++)
        {
            vecteurSommet.add((Sommet)ensembleSommet.get(i));
        }
        return vecteurSommet;
    }
    
    public static Sommet testSommet (int ligne, int colonne, Double resolution, BufferedImage image, Maillage m)
    {
        List<Sommet> vecteurPrecedent;
        
        if(ligne == 0)
        {
            Sommet sommet = (Sommet)m.getEnsembleSommets().get(colonne);
            if(sommet != null && sommet.getX() == colonne && sommet.getY() == getHauteurPixel(ligne, colonne, resolution, image) && sommet.getZ() == ligne)
            {
                return sommet;
            }
            return new Sommet((double)ligne, getHauteurPixel(ligne, colonne, resolution, image), (double)colonne);
        }
        else
        {
            vecteurPrecedent = vecteurSommet(ligne-1, image.getWidth(), image.getHeight(), m.getEnsembleSommets());
            //System.out.println("colonne : "+colonne+" ligne : "+ligne+" taille vecteur : "+vecteurPrecedent.size());
            Sommet sommet = vecteurPrecedent.get(colonne);
            if (sommet != null && sommet.getX() == colonne && sommet.getY() == getHauteurPixel(ligne, colonne, resolution, image) && sommet.getZ() == ligne) 
            {
                return sommet;
            }
            else 
            {
                return new Sommet((double)ligne, getHauteurPixel(ligne, colonne, resolution, image), (double)colonne);
            }
        }
    }
    
   
    public static Maillage ParcelleToMaillage(BufferedImage image, double max, int min) {
        
        Maillage m = new Maillage();
        int rouge;
        int vert;
        int bleu;
        int moyenne;
        int pixel = 0;
        int couleur = 0;
        double hauteurSommet = 0.0;
        double resolution = max/256;
        Sommet sommetHG;
        Sommet socleHG;
        Sommet sommetHD;
        Sommet socleHD;
        Sommet sommetBG;
        Sommet socleBG;
        Sommet sommetBD;
        Sommet socleBD;
        
        for (int ligne = 0; ligne < image.getHeight()-1; ligne++) {
            for(int colonne = 0; colonne < image.getWidth()-1; colonne++) {
                if (ligne+1 < image.getHeight()-1 || colonne+1 < image.getWidth()-1 || ligne < image.getHeight()-1 || colonne < image.getWidth()-1){
                    sommetHG = testSommet(ligne, colonne, resolution, image, m);
                    //sommetHG = creationSommet(i,j,resolution);
                    m.getEnsembleSommets().put(sommetHG.getId(), sommetHG);

                    sommetHD = testSommet(ligne, colonne+1, resolution, image, m);
                    //sommetHD = creationSommet(i+1, j, resolution);
                    m.getEnsembleSommets().put(sommetHD.getId(), sommetHD);

                    sommetBG = testSommet(ligne+1, colonne, resolution, image, m);
                    //sommetBG = creationSommet(i, j+1, resolution);
                    m.getEnsembleSommets().put(sommetBG.getId(), sommetBG);
                    //System.out.println("Sommet BD : i = "+ i + " j = " + j);
                    sommetBD = testSommet(ligne+1, colonne+1, resolution, image, m);
                    //sommetBD = creationSommet(i+1, j+1, resolution);
                    m.getEnsembleSommets().put(sommetBD.getId(), sommetBD);

                    Face triangleG = new Face(sommetHG.getId(),sommetHD.getId(),sommetBG.getId());
                    Face triangleD = new Face(sommetHD.getId(),sommetBD.getId(),sommetBG.getId());
                
                    /*SOCLE*/

                    //ajoute le sommet i,j dans l'ensemble de tout les sommets
                    //crée le sommet du socle à une hauteur de 0
                    socleHG = pointDuSocle(sommetHG, image);
                    m.getEnsembleSommets().put(socleHG.getId(), socleHG);
                    m.getListeSocle().add(socleHG);

                    //ajoute le sommet i+1,j dans l'ensemble de tout les sommets
                    //crée le sommet du socle à une hauteur de 0
                    socleHD = pointDuSocle(sommetHD, image);
                    m.getEnsembleSommets().put(socleHD.getId(), socleHD);
                    m.getListeSocle().add(socleHD);

                     //ajoute le sommet i,j+1 dans l'ensemble de tout les sommets
                    //crée le sommet du socle à une hauteur de 0
                    socleBG = pointDuSocle(sommetBG, image);
                    m.getEnsembleSommets().put(socleBG.getId(), socleBG);
                    m.getListeSocle().add(socleBG);

                    //ajoute le sommet i+1,j+1 dans l'ensemble de tout les sommets
                    //crée le sommet du socle à une hauteur de 0
                    socleBD = pointDuSocle(sommetBD, image);
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
                    if(colonne == 0) {
                        Face cote1 = new Face(sommetHG.getId(), sommetBG.getId(), socleHG.getId());
                        Face cote2 = new Face(sommetBG.getId(), socleBG.getId(), socleHG.getId());

                        m.getEnsembleFaces().add(cote1);
                        m.getEnsembleFaces().add(cote2);
                    }
                        // Cote haut
                    if(ligne == 0) {
                        Face cote1 = new Face(sommetHG.getId(), sommetHD.getId(), socleHG.getId());
                        Face cote2 = new Face(sommetHD.getId(), socleHD.getId(), socleHG.getId());

                        m.getEnsembleFaces().add(cote1);
                        m.getEnsembleFaces().add(cote2);
                    }

                    
                        // Cote droit
                   if(ligne == (image.getHeight()- 2)) {
                        Face cote1 = new Face(sommetBG.getId(), sommetBD.getId(), socleBG.getId());
                        Face cote2 = new Face(sommetBD.getId(), socleBD.getId(), socleBG.getId());

                        m.getEnsembleFaces().add(cote1);
                        m.getEnsembleFaces().add(cote2);
                    }
                        // Cote bas
                    if(colonne == (image.getWidth()- 2)) {
                        Face cote1 = new Face(sommetHD.getId(), sommetBD.getId(), socleHD.getId());
                        Face cote2 = new Face(sommetBD.getId(), socleBD.getId(), socleHD.getId());

                        m.getEnsembleFaces().add(cote1);
                        m.getEnsembleFaces().add(cote2);
                    }
                }
            }
        }
        
        return m;
    }
    
    public static boolean doitEtreRemonte(BufferedImage image, Sommet s) {
        return (s.getX() >= image.getWidth()*0.1 && s.getX() <= image.getWidth()*0.9 
                && s.getZ() >= image.getHeight()*0.1 &&s.getZ() <= image.getHeight()*0.9   //zone du rectangle du socle
                
                || s.getX() >= image.getWidth()*0.45 && s.getX() <= image.getWidth()*0.55
                && s.getZ() <= image.getHeight()*0.1    //slot haut
                
                || s.getX() <= image.getWidth()*0.1
                && s.getZ() >= image.getHeight()*0.45 && s.getZ() <= image.getHeight()*0.55 //slot gauche
                
                || s.getX() >= image.getWidth()*0.45 && s.getX() <= image.getWidth()*0.55
                && s.getZ() >= image.getHeight()*0.9     //slot bas
                
                || s.getZ() >= image.getHeight()*0.45 && s.getZ() <= image.getHeight()*0.55
                && s.getX() >= image.getWidth()*0.9);       //slot droit
    }
}