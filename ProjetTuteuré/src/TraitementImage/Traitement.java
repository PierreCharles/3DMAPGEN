package TraitementImage;

import Maillage.Maillage;
import Maillage.Sommet;
import Maillage.Face;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Traitement {
    
    public static Sommet pointDuSocle(Sommet s) {
        return new Sommet(s.getZ(), -15.0, s.getX());
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
        moyenne = (rouge+vert+bleu)/3;
        hauteur = resolution*moyenne;
        return hauteur;
    }
    
    public static void creuserRectangle(Maillage m, Charger ch, Double longueur, Double largeur) {
        ArrayList<Sommet> listeADeplacer = new ArrayList<>();
        //récupérer les sommets à déplacer pour le rectangle
        for (int i = 0; i < m.getListeSocle().size(); i++) {
             if (m.getListeSocle().get(i).getX().compareTo((ch.getLargeur() + largeur) / 2) <= 0 
                     && m.getListeSocle().get(i).getX().compareTo((ch.getLargeur() - largeur) / 2) >= 0) {
                if (m.getListeSocle().get(i).getZ().compareTo((ch.getHauteur() + longueur) / 2) <= 0 
                        && m.getListeSocle().get(i).getZ().compareTo((ch.getHauteur() - longueur) / 2) >= 0) {
                    listeADeplacer.add(m.getListeSocle().get(i));
                }
            }
        }
        for (int i = 0; i< listeADeplacer.size(); i++) {
            listeADeplacer.get(i).deplacerY(5.0);
        }
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
    
    public static Sommet testSommet (int ligne, int colonne, Double resolution, Charger ch, Maillage m)
    {
        List<Sommet> vecteurPrecedent;
        
        if(ligne == 0)
        {
            Sommet sommet = (Sommet)m.getEnsembleSommets().get(colonne);
            if(sommet != null && sommet.getX() == colonne && sommet.getY() == getHauteurPixel(ligne, colonne, resolution, ch.getImage()) && sommet.getZ() == ligne)
            {
                return sommet;
            }
            return new Sommet((double)ligne, getHauteurPixel(ligne, colonne, resolution, ch.getImage()), (double)colonne);
        }
        else
        {
            vecteurPrecedent = vecteurSommet(ligne-1, ch.getLargeur(), ch.getHauteur(), m.getEnsembleSommets());
            //System.out.println("colonne : "+colonne+" ligne : "+ligne+" taille vecteur : "+vecteurPrecedent.size());
            Sommet sommet = vecteurPrecedent.get(colonne);
            if (sommet != null && sommet.getX() == colonne && sommet.getY() == getHauteurPixel(ligne, colonne, resolution, ch.getImage()) && sommet.getZ() == ligne) 
            {
                return sommet;
            }
            else 
            {
                return new Sommet((double)ligne, getHauteurPixel(ligne, colonne, resolution, ch.getImage()), (double)colonne);
            }
        }
    }
    
    public static void traitementNiveauDeGris(Charger ch, Maillage m, Double max, int min) {
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
        
        for (int ligne = 0; ligne < ch.getHauteur()-1; ligne++) {
            for(int colonne = 0; colonne < ch.getLargeur()-1; colonne++) {
                if (ligne+1 < ch.getHauteur()-1 || colonne+1 < ch.getLargeur()-1 || ligne < ch.getHauteur()-1 || colonne < ch.getLargeur()-1){
                    sommetHG = testSommet(ligne, colonne, resolution, ch, m);
                    //sommetHG = creationSommet(i,j,resolution);
                    m.getEnsembleSommets().put(sommetHG.getId(), sommetHG);

                    sommetHD = testSommet(ligne, colonne+1, resolution, ch, m);
                    //sommetHD = creationSommet(i+1, j, resolution);
                    m.getEnsembleSommets().put(sommetHD.getId(), sommetHD);

                    sommetBG = testSommet(ligne+1, colonne, resolution, ch, m);
                    //sommetBG = creationSommet(i, j+1, resolution);
                    m.getEnsembleSommets().put(sommetBG.getId(), sommetBG);
                    //System.out.println("Sommet BD : i = "+ i + " j = " + j);
                    sommetBD = testSommet(ligne+1, colonne+1, resolution, ch, m);
                    //sommetBD = creationSommet(i+1, j+1, resolution);
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
                   if(ligne == (ch.getHauteur() - 2)) {
                        Face cote1 = new Face(sommetBG.getId(), sommetBD.getId(), socleBG.getId());
                        Face cote2 = new Face(sommetBD.getId(), socleBD.getId(), socleBG.getId());

                        m.getEnsembleFaces().add(cote1);
                        m.getEnsembleFaces().add(cote2);
                    }
                        // Cote bas
                    if(colonne == (ch.getLargeur() - 2)) {
                        Face cote1 = new Face(sommetHD.getId(), sommetBD.getId(), socleHD.getId());
                        Face cote2 = new Face(sommetBD.getId(), socleBD.getId(), socleHD.getId());

                        m.getEnsembleFaces().add(cote1);
                        m.getEnsembleFaces().add(cote2);
                    }
                }
            }
        }
        creuserRectangle(m, ch, ch.getHauteur()*0.8, ch.getLargeur()*0.8);
    }
}