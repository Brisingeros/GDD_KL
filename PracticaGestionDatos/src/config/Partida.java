/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author Brisin
 */
public abstract class Partida {
    
    protected String path;
    protected int filas;
    protected int tamaño;
    
    public abstract String getPath();
    
    public abstract int getFilas();
    
    public abstract int getTamaño();
    
}
