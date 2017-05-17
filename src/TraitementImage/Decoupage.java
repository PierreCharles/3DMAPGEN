/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TraitementImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alexi
 */
public class Decoupage {

    /**
     * @return the hauteurParcelle
     */
    public static int getHauteurParcelle() {
        return hauteurParcelle;
    }

    /**
     * @return the largeurParcelle
     */
    public static int getLargeurParcelle() {
        return largeurParcelle;
    }
    
    private static int nbDecoupeHauteur;
    private static int nbDecoupeLargeur;
    private static int hauteurParcelle;
    private static int largeurParcelle;
    
    public static List<BufferedImage> decouperImage(Charger ch, double largeurVoulue, double hauteurVoulue, double largeurMaxImpression, double hauteurMaxImpression) {
        List<BufferedImage> listeImages = new ArrayList<>();
        BufferedImage imageBase = ch.getImage();
        Decoupage.nbDecoupeHauteur = (int) Math.ceil(hauteurVoulue / (hauteurMaxImpression/10));
        Decoupage.nbDecoupeLargeur = (int) Math.ceil(largeurVoulue / (largeurMaxImpression/10));
        Decoupage.hauteurParcelle = (int) Math.floor(imageBase.getHeight() / getNbDecoupeHauteur());
        Decoupage.largeurParcelle = (int) Math.floor(imageBase.getWidth() / getNbDecoupeLargeur());
        System.out.println("hauteurParcelle : " + getHauteurParcelle());
        System.out.println("largeurParcelle : " + getLargeurParcelle());

        for(int x = 0; x < getNbDecoupeLargeur(); x++) {
            for(int y = 0; y < getNbDecoupeHauteur(); y++) {
                listeImages.add(imageBase.getSubimage(x * getLargeurParcelle(), y * getHauteurParcelle(), getLargeurParcelle(), getHauteurParcelle()));
            }
        }
        return listeImages;
    }

    /**
     * @return the nbDecoupeHauteur
     */
    public static int getNbDecoupeHauteur() {
        return nbDecoupeHauteur;
    }

    /**
     * @return the nbDecoupeLargeur
     */
    public static int getNbDecoupeLargeur() {
        return nbDecoupeLargeur;
    }
}
