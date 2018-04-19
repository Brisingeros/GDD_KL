package config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "puzzle")
@XmlType (propOrder = {"rows", "imageSize", "defecto", "base"})
public class info { //Clase para parsear el fichero de config inicial
    
    private int rows;
    private int imageSize;
    private String defecto;
    private String base;
    
    @XmlElement (name = "rows")
    public int getRows(){
        return this.rows;
    }
    
    @XmlElement (name = "imageSize")
    public int getImageSize(){
        return this.imageSize;
    }
    
    @XmlElement (name = "defecto")
    public String getDefecto(){
        return this.defecto;
    }
    @XmlElement (name = "base")
    public String getBase(){
        return this.base;
    }
    
    public void setRows(int a){
        this.rows = a;
    }
    
    public void setImageSize(int a){
        this.imageSize = a;
    }
    
    public void setDefecto(String a){
        this.defecto = a;
    }
    
    public void setBase(String a){
        this.base = a;
    }
}
