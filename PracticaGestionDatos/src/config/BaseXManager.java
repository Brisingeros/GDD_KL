package config;

import command.MovCommand;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.basex.core.*;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;
import org.xml.sax.SAXException;

public class BaseXManager {

    public void createCollection(String name, Context context){
        try {
            
            new CreateDB(name).execute(context);
            //if(Configuracion.validate("Pilas.xml"))
                
                new Add("Pilas.xml","resources/Pilas.xml").execute(context);   //System.getProperty("user.dir") + System.getProperty("file.separator") + "resources").execute(context);
            
            //if(Configuracion.validate("Partidas.xml"))
                
                new Add("Partidas.xml","resources/Partidas.xml").execute(context);    //System.getProperty("user.dir") + System.getProperty("file.separator") + "resources").execute(context);
            /*System.out.println("Show data base information: ");
            System.out.println(new InfoDB().execute(context));*/
        } catch (BaseXException ex) {
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void queryCatalog(String query, Context context) throws BaseXException{
    
        try{
            
            XQuery xQuery = new XQuery(query);
            System.out.println(xQuery.execute(context));
            
        }catch(Exception e){
        
            System.out.println("No se puede realizar la consulta: " + e.getMessage());
        
        }
    }
    
    public void addMovCommand(MovCommand com, Context context, String type){
        try {

            System.out.println("insert node " + com + " into /pilas/" + type);
            XQuery xQuery = new XQuery("insert node " + com + " into /pilas/" + type); //Almacenamos en el mismo XML ambos tipos de movcommand, separados en dos elementos
            System.out.println(xQuery.execute(context));
            updatePilas(context);
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }
    
    public void updatePilas(Context context) throws BaseXException{
    
        try{
            
            XQuery query = new XQuery("file:write('resources/Pilas.xml', for $item in /pilas \n return $item)");

            query.execute(context);
        
        }catch(Exception e){
        
            System.out.println("No se pudo realizar la operación: " + e.getMessage());
        
        }
    
    }
    
    public void updatePartidaGuardada(Context context) throws BaseXException{
    
        try{
            
            XQuery query = new XQuery("file:write('resources/Partidas.xml', for $item in /partidas \n return $item)");

            System.out.println(query.execute(context));
        
        }catch(Exception e){
        
            System.out.println("No se pudo realizar la operación: " + e.getMessage());
        
        }
    
    }
    
    public String tomarMovCommand(Context context, String type){
        try {
            String res = new XQuery("/pilas/" + type + "/array[last()]/text()").execute(context);
            XQuery query = new XQuery("delete node /pilas/" + type + "/array[last()]");
            query.execute(context);
            updatePilas(context);
            return res;
        } catch (BaseXException ex) {
            //Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void limpiarMovCommand(Context context, String type){
        try {
            XQuery query = new XQuery("delete node /pilas/" + type + "/array*"); //Comprobar si está bien
            query.execute(context);
            updatePilas(context);
        } catch (BaseXException ex) {
            //Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarPartida(Context context, int id, int filas, int tamaño, String path){
    
        try {
            //XQuery query = new XQuery("insert node doc('Pilas.xml')/pilas/. into doc('Partidas.xml')/partida[@id='" + id + "']");
            
            XQuery query = new XQuery("doc('resources/Pilas.xml')/pilas/*");
            XQuery query1 = new XQuery("insert node element partida{attribute id{'" + id + "'}," +
                    "element path{'" + path + "'}," +
                            "element filas{'" + filas + "'}," +
                                    "element tamaño{'" + tamaño + "'}," +
                                            "element pilas{doc('resources/Pilas.xml')/pilas/*}} into /partidas");
            
            query1.execute(context);
            
            queryCatalog("/partidas",context);
            updatePartidaGuardada(context);
        } catch (BaseXException ex) {
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
    
    }
    
}
