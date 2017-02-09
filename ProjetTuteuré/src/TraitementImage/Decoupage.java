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
    
    public static List<BufferedImage> decouperImage(Charger ch, double longueurVoulue, double largeurVoulue, double tailleImpression) {
        List<BufferedImage> listeImages = new ArrayList<>();
        BufferedImage imageBase = ch.getImage();
        int longueur = ch.getHauteur();
        int largueur = ch.getLargeur();
        int nbDecoupeLongueur = (int) Math.ceil(longueurVoulue / tailleImpression);
        int nbDecoupeLargeur = (int) Math.ceil(largeurVoulue / tailleImpression);
        System.out.println("nbDecoupeLongueur : " + nbDecoupeLongueur);
        System.out.println("nbDecoupeLargeur : " + nbDecoupeLargeur);
        for(int x = 0; x < nbDecoupeLongueur; x++) {
            for(int y = 0; y < nbDecoupeLargeur; y++) {
                System.out.println("coucou");
                listeImages.add(imageBase.getSubimage((int) Math.floor(x * (longueurVoulue / nbDecoupeLongueur)),
                        (int) Math.floor(y * (largeurVoulue / nbDecoupeLargeur)),
                        (int) Math.floor(longueurVoulue / nbDecoupeLongueur), (int) Math.floor(largeurVoulue / nbDecoupeLargeur)));
            }
        }
        return listeImages;
    }
}
