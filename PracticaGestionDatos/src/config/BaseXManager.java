package config;

import command.MovCommand;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.basex.core.*;
import org.basex.core.cmd.Add;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.XQuery;

public class BaseXManager extends BaseDatos {

    private final Context context;
    
    public BaseXManager(Context context,int filas, int tamaño, String path){
    
        super("XML", filas, tamaño, path);
        
        this.context = context;
        
        CreateCollection();
        
        
    }

    private void CreateCollection(){
        
        try {
            
            new CreateDB("Pilas").execute(context);
            new Add("Pilas.xml","resources/Pilas.xml").execute(context);   //System.getProperty("user.dir") + System.getProperty("file.separator") + "resources").execute(context);
            new Add("Partidas.xml","resources/Partidas.xml").execute(context);    //System.getProperty("user.dir") + System.getProperty("file.separator") + "resources").execute(context);

        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }
    //
    public void queryCatalog(String query, Context context){
    
        try{
            
            XQuery xQuery = new XQuery(query);
            System.out.println(xQuery.execute(context));
            
        }catch(BaseXException e){
        
            System.out.println("No se puede realizar la consulta: " + e.getMessage());
        
        }
    }
    
    @Override
    public void addMovCommand(MovCommand com, String type){
        try {

            //System.out.println("insert node " + com + " into /pilas/" + type);
            XQuery xQuery = new XQuery("insert node " + com + " into /pilas/" + type); //Almacenamos en el mismo XML ambos tipos de movcommand, separados en dos elementos
            xQuery.execute(context);
            updatePilas();
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }
    
    private void updatePilas() throws BaseXException{
    
        try{
            
            XQuery query = new XQuery("file:write('resources/Pilas.xml', for $item in /pilas \n return $item)");

            query.execute(context);
        
        }catch(BaseXException e){
        
            System.out.println("No se pudo realizar la operación: " + e.getMessage());
        
        }
    
    }
    
    private void updatePartidaGuardada() throws BaseXException{
    
        try{
            
            XQuery query = new XQuery("file:write('resources/Partidas.xml', for $item in /partidas \n return $item)");

            query.execute(context);
        
        }catch(BaseXException e){
        
            System.out.println("No se pudo realizar la operación: " + e.getMessage());
        
        }
    
    }
    
    private int[] StringToInt(String s){
    
        int[] value = null;
        if(!s.equals("")){
        
            String[] aux = s.split(",");
            value = new int[2];

            for(int i = 0; i < 2; i++)

                value[i] = Integer.parseInt(aux[i]);


        }
        
        return value;

    }
    
    @Override
    public int[] tomarMovCommand(String type){
        
        try {
            String res = new XQuery("/pilas/" + type + "/array[last()]/text()").execute(context);
            XQuery query = new XQuery("delete node /pilas/" + type + "/array[last()]");
            query.execute(context);
            updatePilas();

            return StringToInt(res);
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            
        }
    }
    
    @Override
    public void limpiarMovCommand(String type){
        try {
            XQuery query = new XQuery("delete nodes /pilas/" + type + "/array"); //Comprobar si está bien
            query.execute(context);
            updatePilas();
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    @Override
    public String guardarPartida(int id, String path){
    
        try {
            
            XQuery query = new XQuery("doc('resources/Pilas.xml')/pilas/*");
            XQuery query1 = new XQuery("replace node /partidas/partida[@id='"+ id +"'] with element partida{attribute id{'" + id + "'}," +
                    "element path{'" + path + "'}," +
                            "element filas{'" + filas + "'}," +
                                    "element tamaño{'" + tamaño + "'}," +
                                            "element pilas{doc('resources/Pilas.xml')/pilas/*}}" );
            
            query1.execute(context);
            updatePartidaGuardada();
            
            return "Partida guardada correctamente";
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return "No se pudo guardar la partida";
            
        } 
    
    }
    
    @Override
    public Partida cargarPartida(int id){
        try {
            XQuery query = new XQuery("let $game := partidas/partida[@id='" + id +"'] \n return <partida>{$game/* except $game/pilas}</partida>");
            
            XQuery query2 = new XQuery("doc('resources/Partidas.xml')/partidas/partida[@id='"+ id +"']/pilas");
            XQuery query1 = new XQuery("replace node /pilas with " + query2.execute(context));
            
            query1.execute(context);
            
           
            return Configuracion.parseXML(query.execute(context));
            
        } catch (BaseXException ex) {
            
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            
        }
    }
    
    @Override
    public ArrayList<int[]> recorridoInicio(){
        try {
            
            String mov = new XQuery("for $mov in /pilas/movsdes/array/text() \n return $mov").execute(context);
            
            ArrayList<int[]> val = new ArrayList<>();
            String[] aux2 = mov.split("\r\n");
            String[] aux3;
            
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

    @Override
    public void update(int filas, int tamaño, String path) {
        
        this.filas = filas;
        this.tamaño = tamaño;
        this.path = path;
        
    }
    
    @Override
    public void vaciarActual(){
        try {
            
            
            XQuery q1 = new XQuery("for $a in /pilas/movsdes/array \n return delete node $a");
            XQuery q2 = new XQuery("for $a in /pilas/rehacer/array \n return delete node $a");
            q1.execute(context);
            q2.execute(context);
            updatePilas();

        } catch (BaseXException ex) {
            Logger.getLogger(BaseXManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
