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
    
    public static List<BufferedImage> decouperImage(Charger ch, double largeurVoulue, double hauteurVoulue, double tailleImpression) {
        List<BufferedImage> listeImages = new ArrayList<>();
        BufferedImage imageBase = ch.getImage();
        int hauteur = ch.getHauteur();
        int largueur = ch.getLargeur();
        int nbDecoupeHauteur = (int) Math.ceil(hauteurVoulue / tailleImpression);
        int nbDecoupeLargeur = (int) Math.ceil(largeurVoulue / tailleImpression);
        int hauteurParcelle = (int) Math.floor(imageBase.getHeight() / nbDecoupeHauteur);
        int largeurParcelle = (int) Math.floor(imageBase.getWidth() / nbDecoupeLargeur);
        System.out.println("hauteurParcelle : " + hauteurParcelle);
        System.out.println("largeurParcelle : " + largeurParcelle);
        for(int x = 0; x < nbDecoupeLargeur; x++) {
            for(int y = 0; y < nbDecoupeHauteur; y++) {
                listeImages.add(imageBase.getSubimage(x * largeurParcelle, y * hauteurParcelle, largeurParcelle, hauteurParcelle));
            }
        }
        return listeImages;
    }
}
