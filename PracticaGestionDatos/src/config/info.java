/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "puzzle")
@XmlType (propOrder = {"rows", "imageSize", "defecto"})
public class info {
    
    private int rows;
    private int imageSize;
    private String defecto;
    
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
    
    public void setRows(int a){
        this.rows = a;
    }
    
    public void setImageSize(int a){
        this.imageSize = a;
    }
    
    public void setDefecto(String a){
        this.defecto = a;
    }
}
