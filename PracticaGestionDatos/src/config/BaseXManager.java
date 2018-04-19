package config;

import command.MovCommand;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.basex.core.*;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.XQuery;

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
    //
    public void queryCatalog(String query, Context context){
    
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
            xQuery.execute(context);
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

            query.execute(context);
        
        }catch(Exception e){
        
            System.out.println("No se pudo realizar la operación: " + e.getMessage());
        
        }
    
    }
    
    private int[] StringToInt(String s){
    
        int[] value = null;
        if(s != ""){
        
            String[] aux = s.split(",");
            value = new int[2];

            for(int i = 0; i < 2; i++)

                value[i] = Integer.parseInt(aux[i]);


        }
        
        return value;

    }
    
    public int[] tomarMovCommand(Context context, String type){
        
        try {
            String res = new XQuery("/pilas/" + type + "/array[last()]/text()").execute(context);
            XQuery query = new XQuery("delete node /pilas/" + type + "/array[last()]");
            query.execute(context);
            updatePilas(context);

            return StringToInt(res);
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            
        }
    }
    
    public void limpiarMovCommand(Context context, String type){
        try {
            XQuery query = new XQuery("delete nodes /pilas/" + type + "/array"); //Comprobar si está bien
            query.execute(context);
            updatePilas(context);
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    public String guardarPartida(Context context, int id, int filas, int tamaño, String path){
    
        try {
            
            XQuery query = new XQuery("doc('resources/Pilas.xml')/pilas/*");
            XQuery query1 = new XQuery("replace node /partidas/partida[@id='"+ id +"'] with element partida{attribute id{'" + id + "'}," +
                    "element path{'" + path + "'}," +
                            "element filas{'" + filas + "'}," +
                                    "element tamaño{'" + tamaño + "'}," +
                                            "element pilas{doc('resources/Pilas.xml')/pilas/*}}" );
            
            query1.execute(context);
            updatePartidaGuardada(context);
            
            return "Partida guardada correctamente";
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return "No se pudo guardar la partida";
            
        } 
    
    }
    
    public String cargarPartida(Context context, int id){
        try {
            XQuery query = new XQuery("let $game := partidas/partida[@id='" + id +"'] \n return <partida>{$game/* except $game/pilas}</partida>");
            
            XQuery query2 = new XQuery("doc('resources/Partidas.xml')/partidas/partida[@id='"+ id +"']/pilas");
            XQuery query1 = new XQuery("replace node /pilas with " + query2.execute(context));
            
            query1.execute(context);
            
            return query.execute(context);
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            
        }
    }
    
    public ArrayList<int[]> recorridoInicio(Context context){
        try {
            
            String mov = new XQuery("for $mov in /pilas/movsdes/array/text() \n return $mov").execute(context);
            
            ArrayList<int[]> val = new ArrayList<int[]>();
            String[] aux2 = mov.split("\r\n");
            String[] aux3 = null;
            
            for(int i = 0; i < aux2.length; i++){
                aux3 = aux2[i].split(",");
                int[] values = {Integer.parseInt(aux3[0]),Integer.parseInt(aux3[1])};
                
                val.add(values);
            
            }
            
            return val;
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            
        }
    }
    
}
