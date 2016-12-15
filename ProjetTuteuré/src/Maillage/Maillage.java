package Maillage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

public class Maillage {

    //attributs
    private TreeMap ensembleSommets;
    private LinkedList<Face> ensembleFaces;
    private ArrayList<Sommet> listeSocle;
    
    public Maillage() {
        ensembleFaces = new LinkedList();
        ensembleSommets = new TreeMap();
        listeSocle = new ArrayList<>();
    }
    
    public TreeMap getEnsembleSommets() {
        return ensembleSommets;
    }
    
    public LinkedList<Face> getEnsembleFaces() {
        return ensembleFaces;
    }
    
    public ArrayList<Sommet> getListeSocle() {
        return listeSocle;
    }
    
    public void ajouterFace(Face f) {
        ensembleFaces.add(f);
    }
    public int nbFace() {
        return ensembleFaces.size();
    }            
}
