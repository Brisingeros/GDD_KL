package config;

import command.MovCommand;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.basex.core.*;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.XQuery;

public class BaseXManager {

    public void createCollection(String name, Context context){
        try {
            new CreateDB(name).execute(context);
            new Add("Pilas.xml","resources").execute(context);
            
            /*System.out.println("Show data base information: ");
            System.out.println(new InfoDB().execute(context));*/
        } catch (BaseXException ex) {
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

            //System.out.println("insert node " + com + " into /pilas/" + type);
            XQuery xQuery = new XQuery("insert node " + com + " into /pilas/" + type); //Almacenamos en el mismo XML ambos tipos de movcommand, separados en dos elementos
            System.out.println(xQuery.execute(context));
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }
    
    public String tomarMovCommand(Context context, String type){
        try {
            String res = new XQuery("/pilas/" + type + "/array[last()]/text()").execute(context);
            XQuery query = new XQuery("delete node /pilas/" + type + "/array[last()]");
            query.execute(context);
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
        } catch (BaseXException ex) {
            //Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
