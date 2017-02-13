package Maillage;

public class Sommet {
    private static int cpt = 1;
    //attributs
    private double x;
    private double y;
    private double z;
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
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public int getId() {
        return id;
    }
    
    public void setX (double val) {
        x = val;
    }
    
    public void setY (double val) {
        y = val;
    }
    
    public void setZ (double val) {
        z = val;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public static void resetCpt() {
        cpt = 1;
    }
    
    public boolean equals(Sommet s) {
        return (this==s);
    }
    
    @Override
    public String toString() {
        return "v " + x + " " + y + " " + z + "\r\n";
    }
    
    public void deplacerX(double d) {
        setX(getX() + d);
    }
    
    public void deplacerY(double d) {
        setY(getY() + d);
    }
    
    public void deplacerZ(double d) {
        setZ(getZ() + d);
    }
}
