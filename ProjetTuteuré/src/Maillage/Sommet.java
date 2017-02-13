package Maillage;

public class Sommet {
    private static int cpt = 1;
    //attributs
    private Double x;
    private Double y;
    private Double z;
    private int id;
    
    //constructeur
    public Sommet(Double ligne, Double hauteur, Double colonne) {
        x = colonne;
        y = hauteur;
        z = ligne;
        id = cpt;
        cpt++;
    }
    
    //methodes
    public Double getX() {
        return x;
    }
    
    public Double getY() {
        return y;
    }
    
    public Double getZ() {
        return z;
    }
    
    public int getId() {
        return id;
    }
    
    public void setX (Double val) {
        x = val;
    }
    
    public void setY (Double val) {
        y = val;
    }
    
    public void setZ (Double val) {
        z = val;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean equals(Sommet s) {
        return (this==s);
    }
    
    @Override
    public String toString() {
        return "v " + x + " " + y + " " + z + "\r\n";
    }
    
    public void deplacerX(Double d) {
        setX(getX() + d);
    }
    
    public void deplacerY(Double d) {
        setY(getY() + d);
    }
    
    public void deplacerZ(Double d) {
        setZ(getZ() + d);
    }
}
