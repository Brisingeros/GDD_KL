package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Configuracion {
    
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public static info parse() throws JAXBException{ //Para leer el fichero de config
        
        JAXBContext jc = JAXBContext.newInstance(info.class);
        
        Unmarshaller unmar = jc.createUnmarshaller();
        
        info jugador = (info) unmar.unmarshal(new File("resources/puzzle.xml"));

        return jugador;
        
    }
    
    //Para guardar partida en Json
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
        
        }
        
    }
    
    //Para leer un fichero Json y parsearlo a Partida
    public static PartidaLD cargarPartida() throws IOException{
        
        FileReader f = null;
        String cadena = "";
        BufferedReader b = null;
        PartidaLD party = null;
        
        try {
            
            f = new FileReader("resources/partida.json");
            b = new BufferedReader(f);
            party = gson.fromJson(b, PartidaLD.class);  
            
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, ex);
            
        }
           
        f.close();
        
        return party;
    }

}