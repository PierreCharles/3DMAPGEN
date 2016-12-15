package TraitementImage;

import Maillage.Maillage;
import Maillage.Sommet;
import Maillage.Face;
import java.util.ArrayList;

public class Traitement {
    
    public static Sommet pointDuSocle(Sommet s) {
        return new Sommet(s.getX(), -15.0, s.getZ());
    }
    
    /*
    vérifie si le sommet passé en paramètre existe déjà
    si oui renvoie l'existant
    sinon renvoie le nouveau créé
    */
    public static Sommet ajoutSommet(Double i, Double j, Double resolution, Sommet existant, Charger ch) {
        int pixel;
        int rouge;
        int vert;
        int bleu;
        int moyenne;
        int k = (int) Math.round(i);
        int l = (int) Math.round(j);
        Double hauteur;
        
        pixel = ch.getImage().getRGB(k,l);
        
        rouge = (pixel >> 16) & 0xff;
        vert = (pixel >> 8) & 0xff;
        bleu = (pixel) & 0xff;
        moyenne = (rouge+vert+bleu)/3;
               
        hauteur = resolution*moyenne;
        if((int)Math.round(existant.getX()) == (int)Math.round(i)
                && (int)Math.round(existant.getY()) == (int)Math.round(hauteur)
                && (int)Math.round(existant.getZ()) == (int)Math.round(j)) {
            return existant;
        }
        else {
            Sommet sommet = new Sommet(i, hauteur, j);
        return sommet;
        }
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
    
    public static void traitementNiveauDeGris (Charger ch, Maillage m, Double max, int min) {
        
        int rouge;
        int vert;
        int bleu;
        int moyenne;
        int pixel = 0;
        int couleur=0;
        Double hauteurSommet = 0.0;
        Double resolution = max/256;
        Sommet tempHG = new Sommet(0.0,0.0,0.0);
        Sommet tempHD = new Sommet(0.0,0.0,0.0);
        Sommet tempBG = new Sommet(0.0,0.0,0.0);
        Sommet tempBD = new Sommet(0.0,0.0,0.0);
        Sommet sommetHG, socleHG, sommetHD, socleHD, sommetBG, socleBG, sommetBD, socleBD;
        
        for (Double i=1.0; i<ch.getLargeur()-1; i++) {
            for(Double j=1.0; j<ch.getHauteur()-1; j++) {
                
                sommetHG = ajoutSommet(i,j,resolution,tempHG,ch); 
                m.getEnsembleSommets().put(sommetHG.getId(), sommetHG);
                
                sommetHD = ajoutSommet(i+1, j, resolution, tempHD,ch);
                m.getEnsembleSommets().put(sommetHD.getId(), sommetHD); 
                
                sommetBG = ajoutSommet(i, j+1, resolution, tempBG,ch);
                m.getEnsembleSommets().put(sommetBG.getId(), sommetBG);
                
                sommetBD = ajoutSommet(i+1, j+1, resolution, tempBD,ch);
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
            }
        }
        creuserRectangle(m, ch, ch.getHauteur()*0.8, ch.getLargeur()*0.8);
    }
}