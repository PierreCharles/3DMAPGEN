package Maillage;

import static Maillage.Sommet.resetCpt;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

public class Maillage {

    //attributs
    private TreeMap<Double,TreeMap<Double,Sommet>> ensembleSommets;
    private TreeMap<Double,TreeMap<Double,Sommet>> ensembleSommetsSocle;
    private LinkedList<Face> ensembleFaces;
    //private ArrayList<Sommet> listeSocle;
    
    public Maillage() {
        ensembleFaces = new LinkedList();
        ensembleSommets = new TreeMap<Double, TreeMap<Double, Sommet>>();
        ensembleSommetsSocle = new TreeMap<Double, TreeMap<Double, Sommet>>();
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
    
    public void ajouterSommet(double ligne, double colonne, Sommet sommet) {
        if(!this.ensembleSommets.containsKey(ligne)){
            this.ensembleSommets.put(ligne,new TreeMap());
            this.ensembleSommets.get(ligne).put(colonne, sommet);
        }
        else{
            this.ensembleSommets.get(ligne).put(colonne, sommet);
        }
    }
    
    public void ajouterSommetSocle(double ligne, double colonne, Sommet sommet) {
        if(!this.ensembleSommetsSocle.containsKey(ligne)){
            this.ensembleSommetsSocle.put(ligne,new TreeMap());
            this.ensembleSommetsSocle.get(ligne).put(colonne, sommet);
        }
        else{
            this.ensembleSommetsSocle.get(ligne).put(colonne, sommet);
        }
    }
}
