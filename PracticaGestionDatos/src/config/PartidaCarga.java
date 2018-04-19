/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author Laura
 */
public class PartidaCarga {
    
    private int tamaño;
    private String path;
    private int filas;
    
    public PartidaCarga(int t, int f, String p){
    
        this.tamaño = t;
        this.filas = f;
        this.path = p;
    
    }

    public int getTamaño() {
        return tamaño;
    }

    public String getPath() {
        return path;
    }

    public int getFilas() {
        return filas;
    }
    
    
}
