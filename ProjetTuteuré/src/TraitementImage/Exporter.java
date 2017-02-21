package TraitementImage;

import Maillage.Face;
import Maillage.Maillage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Exporter {
    
    
    static public void exportToObj(Maillage m, String destFile,String dirName, int partie) throws IOException {
        File fi = new File(destFile + "\\" + dirName + "\\" + dirName + "Partie" + partie + ".obj");
        try (FileWriter fw = new FileWriter(fi)) {
            fw.write("# Fichier réalisé par\n");
            fw.write("# Alexis Dardinier\n");
            fw.write("# Thomas Klein\n");
            fw.write("# Pierre Petit\n");
            fw.write("# Timothé Rouzé\n");
            fw.write("# Mathieu Vincent\n\n");
            fw.write("o maillage\n\n");
            Set set = m.getEnsembleSommets().entrySet();
            Iterator it = set.iterator();
            while(it.hasNext()) {
                Map.Entry mentry = (Map.Entry)it.next();
                fw.write(mentry.getValue().toString());
            }
            for(Face f : m.getEnsembleFaces()) {
                fw.write(f.toString());
            }
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
