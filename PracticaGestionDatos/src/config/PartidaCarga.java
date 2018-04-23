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
public class PartidaCarga extends Partida{ //clase para cargar partida desde mongo
    
    private int tamaño;
    private String path;
    private int filas;
    
    public PartidaCarga(int t, int f, String p){
    
        this.tamaño = t;
        this.filas = f;
        this.path = p;
    
    }

    @Override
    public int getTamaño() {
        return tamaño;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getFilas() {
        return filas;
    }
    
    
}
