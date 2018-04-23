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

    //metodo para recuperar el path de la imagen completa
    public abstract String getPath();
    
    //metodo para recuperar el numero de filas de la imagen
    public abstract int getFilas();
    
    //metodo para recuperar el tamaño de la imagen
    public abstract int getTamaño();
    
}
