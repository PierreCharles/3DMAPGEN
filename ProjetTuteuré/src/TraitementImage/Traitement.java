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
        double h= -5.0;
        if (doitEtreRemonte(img, s))
            h = -2.0;
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
        double deb = image.getWidth() * 0.1;
        double fin = image.getWidth() * 0.9;
        return (s.getX() >= deb && s.getX() <= fin 
            && s.getZ() >= deb && s.getZ() <= image.getHeight() - deb  //zone du rectangle du socle
                
            || s.getX() >= (image.getWidth()-deb)/2 && s.getX() <= (image.getWidth()+deb)/2
            && s.getZ() <= deb   //slot haut
                
            || s.getX() <= deb
            && s.getZ() >= (image.getHeight()-deb)/2 && s.getZ() <= (image.getHeight()+deb)/2 //slot gauche
                
            || s.getX() >= (image.getWidth()-deb)/2 && s.getX() <= (image.getWidth()+deb)/2
            && s.getZ() >= image.getHeight()-deb     //slot bas
                
            || s.getZ() >= (image.getHeight()-deb)/2 && s.getZ() <= (image.getHeight()+deb)/2
            && s.getX() >= fin);       //slot droit
    }
    
    public static Integer getNbAttache(int nbDecoupeL, int nbDecoupeH) {
        return (nbDecoupeL - 1) + (2*nbDecoupeL - 1) * (nbDecoupeH - 1);
    }
    
    /*
    Structure de l'attache:
    3___4           11___12
    |   |5__________7|   |
    |                    |
    |    6__________8    |
    |___|            |___|
    1   2           9    10
    Sommet s0X: sommet au dessus de sX
    */
    public static Maillage genererAttache(BufferedImage parcelle) {
        Maillage attache = new Maillage();
        double deb = parcelle.getWidth() * 0.1;
        Sommet s1 = new Sommet(0, 0, 0);
        attache.getEnsembleSommets().put(s1.getId(), s1);
        Sommet s01 = new Sommet(0, 3, 0);
        attache.getEnsembleSommets().put(s01.getId(), s01);
        Sommet s2 = new Sommet(deb/2, 0, 0);
        attache.getEnsembleSommets().put(s2.getId(), s2);
        Sommet s02 = new Sommet(deb/2, 3, 0);
        attache.getEnsembleSommets().put(s02.getId(), s02);
        Sommet s3 = new Sommet(0, 0, 2*deb);
        attache.getEnsembleSommets().put(s3.getId(), s3);
        Sommet s03 = new Sommet(0, 3, 2*deb);
        attache.getEnsembleSommets().put(s03.getId(), s03);
        Sommet s4 = new Sommet(deb/2, 0, 2*deb);
        attache.getEnsembleSommets().put(s4.getId(), s4);
        Sommet s04 = new Sommet(deb/2, 3, 2*deb);
        attache.getEnsembleSommets().put(s04.getId(), s04);
        Sommet s5 = new Sommet(deb/2, 0, 1.5*deb);
        attache.getEnsembleSommets().put(s5.getId(), s5);
        Sommet s05 = new Sommet(deb/2, 3, 1.5*deb);
        attache.getEnsembleSommets().put(s05.getId(), s05);
        Sommet s6 = new Sommet(deb/2, 0, deb/2);
        attache.getEnsembleSommets().put(s6.getId(), s6);
        Sommet s06 = new Sommet(deb/2, 3, deb/2);
        attache.getEnsembleSommets().put(s06.getId(), s06);
        Sommet s7 = new Sommet(2.5*deb, 0, 1.5*deb);
        attache.getEnsembleSommets().put(s7.getId(), s7);
        Sommet s07 = new Sommet(2.5*deb, 3, 1.5*deb);
        attache.getEnsembleSommets().put(s07.getId(), s07);
        Sommet s8 = new Sommet(2.5*deb, 0, deb/2);
        attache.getEnsembleSommets().put(s8.getId(), s8);
        Sommet s08 = new Sommet(2.5*deb, 3, deb/2);
        attache.getEnsembleSommets().put(s08.getId(), s08);
        Sommet s9 = new Sommet(2.5*deb, 0, 0);
        attache.getEnsembleSommets().put(s9.getId(), s9);
        Sommet s09 = new Sommet(2.5*deb, 3, 0);
        attache.getEnsembleSommets().put(s09.getId(), s09);
        Sommet s10 = new Sommet(3*deb, 0, 0);
        attache.getEnsembleSommets().put(s10.getId(), s10);
        Sommet s010 = new Sommet(3*deb, 3, 0);
        attache.getEnsembleSommets().put(s010.getId(), s010);
        Sommet s11 = new Sommet(2.5*deb, 0, 2*deb);
        attache.getEnsembleSommets().put(s11.getId(), s11);
        Sommet s011 = new Sommet(2.5*deb, 3, 2*deb);
        attache.getEnsembleSommets().put(s011.getId(), s011);
        Sommet s12 = new Sommet(3*deb, 0, 2*deb);
        attache.getEnsembleSommets().put(s12.getId(), s12);
        Sommet s012 = new Sommet(3*deb, 3, 2*deb);
        attache.getEnsembleSommets().put(s012.getId(), s012);
        //faces horizontales
        attache.getEnsembleFaces().add(new Face (s1.getId(), s2.getId(), s3.getId()));
        attache.getEnsembleFaces().add(new Face (s01.getId(), s02.getId(), s03.getId()));
        attache.getEnsembleFaces().add(new Face (s2.getId(), s3.getId(), s4.getId()));
        attache.getEnsembleFaces().add(new Face (s02.getId(), s03.getId(), s04.getId()));
        attache.getEnsembleFaces().add(new Face (s5.getId(), s6.getId(), s7.getId()));
        attache.getEnsembleFaces().add(new Face (s05.getId(), s06.getId(), s07.getId()));
        attache.getEnsembleFaces().add(new Face (s6.getId(), s7.getId(), s8.getId()));
        attache.getEnsembleFaces().add(new Face (s06.getId(), s07.getId(), s08.getId()));
        attache.getEnsembleFaces().add(new Face (s9.getId(), s10.getId(), s11.getId()));
        attache.getEnsembleFaces().add(new Face (s09.getId(), s010.getId(), s011.getId()));
        attache.getEnsembleFaces().add(new Face (s10.getId(), s11.getId(), s12.getId()));
        attache.getEnsembleFaces().add(new Face (s010.getId(), s011.getId(), s012.getId()));
        
        //faces verticales
        
        attache.getEnsembleFaces().add(new Face (s1.getId(), s01.getId(), s3.getId()));
        attache.getEnsembleFaces().add(new Face(s3.getId(), s03.getId(), s01.getId()));
        
        attache.getEnsembleFaces().add(new Face(s1.getId(), s01.getId(), s2.getId()));
        attache.getEnsembleFaces().add(new Face(s01.getId(), s02.getId(), s2.getId()));
        
        attache.getEnsembleFaces().add(new Face(s3.getId(), s03.getId(), s4.getId()));
        attache.getEnsembleFaces().add(new Face(s03.getId(), s04.getId(), s4.getId()));
        
        attache.getEnsembleFaces().add(new Face(s2.getId(), s02.getId(), s6.getId()));
        attache.getEnsembleFaces().add(new Face(s02.getId(), s06.getId(), s6.getId()));
        
        attache.getEnsembleFaces().add(new Face(s4.getId(), s04.getId(), s5.getId()));
        attache.getEnsembleFaces().add(new Face(s04.getId(), s05.getId(), s5.getId()));
        
        attache.getEnsembleFaces().add(new Face(s6.getId(), s06.getId(), s8.getId()));
        attache.getEnsembleFaces().add(new Face(s06.getId(), s08.getId(), s8.getId()));
        
        attache.getEnsembleFaces().add(new Face(s5.getId(), s05.getId(), s7.getId()));
        attache.getEnsembleFaces().add(new Face(s05.getId(), s07.getId(), s7.getId()));
        
        attache.getEnsembleFaces().add(new Face(s8.getId(), s9.getId(), s08.getId()));
        attache.getEnsembleFaces().add(new Face(s08.getId(), s9.getId(), s09.getId()));
        
        attache.getEnsembleFaces().add(new Face(s7.getId(), s07.getId(), s11.getId()));
        attache.getEnsembleFaces().add(new Face(s07.getId(), s11.getId(), s011.getId()));
        
        attache.getEnsembleFaces().add(new Face(s9.getId(), s10.getId(), s09.getId()));
        attache.getEnsembleFaces().add(new Face(s09.getId(), s10.getId(), s010.getId()));
        
        attache.getEnsembleFaces().add(new Face(s11.getId(), s12.getId(), s011.getId()));
        attache.getEnsembleFaces().add(new Face(s011.getId(), s012.getId(), s12.getId()));
        
        attache.getEnsembleFaces().add(new Face(s12.getId(), s10.getId(), s012.getId()));
        attache.getEnsembleFaces().add(new Face(s10.getId(), s012.getId(), s010.getId()));
        

        return attache;
    }
	
}