package mesh;

import static mesh.Vertices.resetCpt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

public class Mesh {

    //attributs
    private TreeMap<Double,TreeMap<Double,Vertices>> ensembleSommets;
    private TreeMap<Double,TreeMap<Double,Vertices>> ensembleSommetsSocle;
    private LinkedList<Face> ensembleFaces;
    //private ArrayList<Vertices> listeSocle;
    
    public Mesh() {
        ensembleFaces = new LinkedList();
        ensembleSommets = new TreeMap<Double, TreeMap<Double, Vertices>>();
        ensembleSommetsSocle = new TreeMap<Double, TreeMap<Double, Vertices>>();
        //listeSocle = new ArrayList<>();
        resetCpt();
    }
    
    public TreeMap getEnsembleSommets() {
        return ensembleSommets;
    }
    
    public LinkedList<Face> getEnsembleFaces() {
        return ensembleFaces;
    }
    
    public TreeMap getEnsembleSommetsSocle() {
        return ensembleSommetsSocle;
    }
    
    public void ajouterFace(Face f) {
        ensembleFaces.add(f);
    }
    public int nbFace() {
        return ensembleFaces.size();
    }
    
    public void ajouterSommet(double ligne, double colonne, Vertices vertices) {
        if(!this.ensembleSommets.containsKey(ligne)){
            this.ensembleSommets.put(ligne,new TreeMap());
            this.ensembleSommets.get(ligne).put(colonne, vertices);
        }
        else{
            this.ensembleSommets.get(ligne).put(colonne, vertices);
        }
    }
    
    public void ajouterSommetSocle(double ligne, double colonne, Vertices vertices) {
        if(!this.ensembleSommetsSocle.containsKey(ligne)){
            this.ensembleSommetsSocle.put(ligne,new TreeMap());
            this.ensembleSommetsSocle.get(ligne).put(colonne, vertices);
        }
        else{
            this.ensembleSommetsSocle.get(ligne).put(colonne, vertices);
        }
    }
    
    public Vertices getPointSurface(double ligne, double colonne) {
        if (getEnsembleSommets().containsKey(ligne)){
            TreeMap sommetTreeMap = (TreeMap) getEnsembleSommets().get(ligne);
            if (sommetTreeMap.containsKey(colonne)) {
                Vertices vertices = (Vertices) sommetTreeMap.get(colonne);
                return vertices;
            }
        }
        return null;
    }
    
    public Vertices getPointSocle(double ligne, double colonne) {
        if (getEnsembleSommetsSocle().containsKey(ligne)) {
            TreeMap sommetTreeMap = (TreeMap) getEnsembleSommetsSocle().get(ligne);
            if (sommetTreeMap.containsKey(colonne)) {
                Vertices vertices = (Vertices) sommetTreeMap.get(colonne);
                return vertices;
            }
        }
        return null;
    }
}
