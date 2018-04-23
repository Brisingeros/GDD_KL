
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Brisin
 */
@XmlRootElement(name = "partida")
@XmlType (propOrder = {"path", "filas", "tamaño"})
public class PartidaXML extends Partida{ //clase para cargar partida desde xml
    
    private String path;
    private int filas;
    private int tamaño;

    @XmlElement (name = "path")
    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @XmlElement (name = "filas")
    @Override
    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    @XmlElement (name = "tamaño")
    @Override
    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }
    
}
