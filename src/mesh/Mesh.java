package mesh;

import static mesh.Vertice.resetCpt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

public class Mesh {

    //attributs
    private TreeMap<Double,TreeMap<Double,Vertice>> ensembleSommets;
    private TreeMap<Double,TreeMap<Double,Vertice>> ensembleSommetsSocle;
    private LinkedList<Face> ensembleFaces;
    //private ArrayList<Vertices> listeSocle;
    
    public Mesh() {
        ensembleFaces = new LinkedList();
        ensembleSommets = new TreeMap<Double, TreeMap<Double, Vertice>>();
        ensembleSommetsSocle = new TreeMap<Double, TreeMap<Double, Vertice>>();
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
    
    public void ajouterSommet(double ligne, double colonne, Vertice vertices) {
        if(!this.ensembleSommets.containsKey(ligne)){
            this.ensembleSommets.put(ligne,new TreeMap());
            this.ensembleSommets.get(ligne).put(colonne, vertices);
        }
        else{
            this.ensembleSommets.get(ligne).put(colonne, vertices);
        }
    }
    
    public void ajouterSommetSocle(double ligne, double colonne, Vertice vertices) {
        if(!this.ensembleSommetsSocle.containsKey(ligne)){
            this.ensembleSommetsSocle.put(ligne,new TreeMap());
            this.ensembleSommetsSocle.get(ligne).put(colonne, vertices);
        }
        else{
            this.ensembleSommetsSocle.get(ligne).put(colonne, vertices);
        }
    }
    
    public Vertice getPointSurface(double ligne, double colonne) {
        if (getEnsembleSommets().containsKey(ligne)){
            TreeMap sommetTreeMap = (TreeMap) getEnsembleSommets().get(ligne);
            if (sommetTreeMap.containsKey(colonne)) {
                Vertice vertices = (Vertice) sommetTreeMap.get(colonne);
                return vertices;
            }
        }
        return null;
    }
    
    public Vertice getPointSocle(double ligne, double colonne) {
        if (getEnsembleSommetsSocle().containsKey(ligne)) {
            TreeMap sommetTreeMap = (TreeMap) getEnsembleSommetsSocle().get(ligne);
            if (sommetTreeMap.containsKey(colonne)) {
                Vertice vertices = (Vertice) sommetTreeMap.get(colonne);
                return vertices;
            }
        }
        return null;
    }
}
