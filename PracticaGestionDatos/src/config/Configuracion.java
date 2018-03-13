/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Laura
 */
public class Configuracion {
    
    public static File archivo = new File("resources/puzzle.xml");
    
    public static info parse() throws JAXBException{
        
        JAXBContext jc = JAXBContext.newInstance(info.class);
        
        Unmarshaller unmar = jc.createUnmarshaller();
        
        info jugador = (info) unmar.unmarshal(archivo);

        return jugador;
        
    }
}
