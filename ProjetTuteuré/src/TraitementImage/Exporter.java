package TraitementImage;

import Maillage.Face;
import Maillage.Maillage;
import Maillage.Sommet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import static TraitementImage.Traitement.getNbAttache;
import java.util.TreeMap;

public class Exporter {
    
    
    static public void exportToObj(Maillage m, String destFile,String dirName, int partie) throws IOException {
        File fi = new File(destFile + "\\" + dirName + "\\" + dirName + "Partie" + partie + ".obj");
        try (FileWriter fw = new FileWriter(fi)) {
            fw.write("# Fichier réalisé par\r\n");
            fw.write("# Alexis Dardinier\r\n");
            fw.write("# Thomas Klein\r\n");
            fw.write("# Pierre Petit\r\n");
            fw.write("# Timothé Rouzé\r\n");
            fw.write("# Mathieu Vincent\r\n");
            fw.write("o maillage\r\n");
           
            /**
             * Ecriture de l'ensemble des points de la surface
             */
            Set<Map.Entry<Double, TreeMap>> setLigne = m.getEnsembleSommets().entrySet();
            Iterator<Map.Entry<Double, TreeMap>> it = setLigne.iterator();
            while(it.hasNext()){
                Map.Entry<Double, TreeMap> e = it.next();
                TreeMap sommetTreeMap = e.getValue();
                
                
                Set<Map.Entry<Double,Sommet>> setColonne = sommetTreeMap.entrySet();
                Iterator<Map.Entry<Double,Sommet>> it2 = setColonne.iterator();
                
                while(it2.hasNext()){
                    Map.Entry<Double, Sommet> sommetEntry = it2.next();
                    fw.write(sommetEntry.getValue().toString());
                }
                
            }
            
            /**
             * Ecriture de l'ensemble des points du socle
             */
            Set<Map.Entry<Double, TreeMap>> setLigneSocle = m.getEnsembleSommetsSocle().entrySet();
            Iterator<Map.Entry<Double, TreeMap>> it3 = setLigneSocle.iterator();
            while(it3.hasNext()){
                Map.Entry<Double, TreeMap> e2 = it3.next();
                TreeMap sommetTreeMapSocle = e2.getValue();
                
                
                Set<Map.Entry<Double,Sommet>> setColonneSocle = sommetTreeMapSocle.entrySet();
                Iterator<Map.Entry<Double,Sommet>> it4 = setColonneSocle.iterator();
                
                while(it4.hasNext()){
                    Map.Entry<Double, Sommet> sommetEntrySocle = it4.next();
                    fw.write(sommetEntrySocle.getValue().toString());
                }
                
            }
            
            for(Face f : m.getEnsembleFaces()) {
                fw.write(f.toString());
            }
            
            fw.close();
            System.out.println("Fichier exporté dans");
            System.out.println(fi.getAbsolutePath());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    static public void createDirectory(String destFile, String dirName) {
       String path = destFile + "\\" + dirName;
       File dir = new File(path);
       dir.mkdir();
    }
    
    static public void exportAttacheToObj(String destFile, String dirName, Maillage attache) {
        File fi = new File(destFile + "\\" + dirName + "\\" + "Attache.obj");
        try (FileWriter fw = new FileWriter(fi)) {
            fw.write("# Fichier réalisé par\n");
            fw.write("# Alexis Dardinier\n");
            fw.write("# Thomas Klein\n");
            fw.write("# Pierre Petit\n");
            fw.write("# Timothé Rouzé\n");
            fw.write("# Mathieu Vincent\n\n");
            fw.write("#Pièce à imprimer " + getNbAttache(Decoupage.getNbDecoupeLargeur(), Decoupage.getNbDecoupeHauteur()).toString() + " fois\n");
            fw.write("o attache\n\n");
            Set set = attache.getEnsembleSommets().entrySet();
            Iterator it = set.iterator();
            while(it.hasNext()) {
                Map.Entry mentry = (Map.Entry)it.next();
                fw.write(mentry.getValue().toString());
            }
            for(Face f : attache.getEnsembleFaces()) {
                fw.write(f.toString());
            }
            System.out.println("attache exporté dans");
            System.out.println(fi.getAbsolutePath());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
