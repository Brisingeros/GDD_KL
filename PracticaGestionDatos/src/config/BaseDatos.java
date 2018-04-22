/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import command.MovCommand;
import java.util.ArrayList;

/**
 *
 * @author Laura
 */
public abstract class BaseDatos {
    
    protected String nombre;
    protected int filas;
    protected int tamaño;
    protected String path;
    
    public BaseDatos(String n, int f, int t, String p){
    
        this.nombre = n;
        this.filas = f;
        this.tamaño = t;
        this.path = p;
    
    }
    
    public abstract void addMovCommand(MovCommand mov, String type);
    
    public abstract int[] tomarMovCommand(String type);

    public abstract String guardarPartida(int id, String path);
    
    public abstract Partida cargarPartida(int id);
    
    public abstract void update(int filas, int tamaño, String path);
    
    public abstract ArrayList<int[]> recorridoInicio();
    
    public abstract void limpiarMovCommand(String type);
  
}
