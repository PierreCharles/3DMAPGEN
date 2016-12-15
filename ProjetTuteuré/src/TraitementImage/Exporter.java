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
    
    static public void ExportToObj(Maillage m, String destFile) throws IOException {
        File fi = new File(destFile + "\\maillage.obj");
        try (FileWriter fw = new FileWriter(fi)) {
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
}
