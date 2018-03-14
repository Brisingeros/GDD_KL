/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import command.MovCommand;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Laura
 */
public class Configuracion {
    
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static File archivo = new File("resources/puzzle.xml");
    
    public static info parse() throws JAXBException{
        
        JAXBContext jc = JAXBContext.newInstance(info.class);
        
        Unmarshaller unmar = jc.createUnmarshaller();
        
        info jugador = (info) unmar.unmarshal(archivo);

        return jugador;
        
    }
    
    public static void toJSON(PartidaLD partida) throws IOException{
    
        String cadena = gson.toJson(partida);
        añadirFichero(cadena);
      
    }
    private static void añadirFichero(String c) throws FileNotFoundException, IOException{
        
        try{

            PrintStream lectura = new PrintStream(new FileOutputStream("resources/partida.json"),false);
            lectura.print(c);
            lectura.close();

        }catch(FileNotFoundException e){
        
            System.out.println("Fichero no encontrado");
        
        }catch(IOException e){
        
            System.out.println(e);
        
        }
        
    }
    
    public static PartidaLD cargarPartida() throws IOException{
        FileReader f = null;
        String cadena = "";
        BufferedReader b = null;
        try {
            f = new FileReader("resources/partida.json");
            b = new BufferedReader(f);
            /*while((cadena = b.readLine()) != null){ //Aquí leemos el fichero y lo vamos guardando en cadena
                //System.out.println(cadena);
            }*/
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PartidaLD party = gson.fromJson(b, PartidaLD.class);
        
        f.close();
        return party;
    }

}
