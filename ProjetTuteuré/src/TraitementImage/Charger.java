package TraitementImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Charger {
    
    BufferedImage image = null;
    private File lien;
    private int hauteur;
    private int largeur;
    int[][] imageTraitee = new int [largeur][hauteur];
    
    public Charger(File lien){
        this.lien=lien;
    }
    
    public int[][] getImageTraitee() {
        return imageTraitee;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }
    

    
    public void ajouterImage() {
        try {
            image = ImageIO.read(lien);
            this.hauteur = image.getHeight();
            this.largeur = image.getWidth();
            System.out.println("hauteur : "+hauteur+" largeur : "+largeur);
            System.out.println("image chargée");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public BufferedImage getImage(){
        return image;
    }
}
