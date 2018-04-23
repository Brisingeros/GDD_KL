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
    
    protected String nombre; //nombre de la base de datos (xml, mongo, etc)
    protected int filas;
    protected int tamaño;
    protected String path;
    
    public BaseDatos(String n, int f, int t, String p){
    
        this.nombre = n;
        this.filas = f;
        this.tamaño = t;
        this.path = p;
    
    }
    
    //metodo para insertar un nuevo movimiento en la base de datos segun su tipo
    public abstract void addMovCommand(MovCommand mov, String type);
    
    //metodo para recuperar el ultimo movimiento de la base de datos en funcion de su tipo
    public abstract int[] tomarMovCommand(String type);

    //metodo para guardar partida en la base de datos en funcion de su id
    public abstract String guardarPartida(int id, String path);
    
    //metodo para recuperar una partida de la base de datos en funcion de su id
    public abstract Partida cargarPartida(int id);
    
    //metodo para actualizar los datos de la partida actual almacenada en la base de datos
    public abstract void update(int filas, int tamaño, String path);
    
    //metodo para recuperar todos los movimientos realizados en una partida
    public abstract ArrayList<int[]> recorridoInicio();
    
    //metodo para borrar una pila de movimientos en funcion de su tipo
    public abstract void limpiarMovCommand(String type);
    
    //metodo para borrar la partida actual de la base de datos
    public abstract void vaciarActual();
  
}
