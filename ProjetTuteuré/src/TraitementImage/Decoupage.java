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
    
    private static int nbDecoupeHauteur;
    private static int nbDecoupeLargeur;
    private static int hauteurParcelle;
    private static int largeurParcelle;
    
    public static List<BufferedImage> decouperImage(Charger ch, double largeurVoulue, double hauteurVoulue, double tailleImpression) {
        List<BufferedImage> listeImages = new ArrayList<>();
        BufferedImage imageBase = ch.getImage();
        Decoupage.nbDecoupeHauteur = (int) Math.ceil(hauteurVoulue / tailleImpression);
        Decoupage.nbDecoupeLargeur = (int) Math.ceil(largeurVoulue / tailleImpression);
        Decoupage.hauteurParcelle = (int) Math.floor(imageBase.getHeight() / getNbDecoupeHauteur());
        Decoupage.largeurParcelle = (int) Math.floor(imageBase.getWidth() / getNbDecoupeLargeur());
        System.out.println("hauteurParcelle : " + hauteurParcelle);
        System.out.println("largeurParcelle : " + largeurParcelle);

        for(int x = 0; x < getNbDecoupeLargeur(); x++) {
            for(int y = 0; y < getNbDecoupeHauteur(); y++) {
                listeImages.add(imageBase.getSubimage(x * largeurParcelle, y * hauteurParcelle, largeurParcelle, hauteurParcelle));
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
