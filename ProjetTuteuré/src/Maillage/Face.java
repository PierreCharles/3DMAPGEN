package Maillage;

public class Face {

    //attributs
    public static int cpt = 1;
    private int id =0;
    private int s1 = 0;
    private int s2 = 0;
    private int s3 = 0;
    
    public Face(int idSommet1, int idSommet2,int idSommet3) {
	this.id = cpt;
        cpt++;
        s1 = idSommet1;
        s2 = idSommet2;
        s3 = idSommet3;
    }
    
    public void setS1(int s1) {
        this.s1 = s1;
    }
    
    public void setS2(int s2) {
        this.s2 = s2;
    }
    
    public void setS3(int s3) {
        this.s3 = s3;
    }
    
    public int getS1() {
        return s1;
    }
    
    public int getS2() {
        return s2;
    }
    
    public int getS3() {
        return s3;
    }
    
    @Override
    public String toString (){
        return "f " + s1 + "// " + s2 + "// " + s3 + "//\r\n";
    }
}
