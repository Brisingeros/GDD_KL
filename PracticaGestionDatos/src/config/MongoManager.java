package config;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import command.MovCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.conversions.Bson;
import org.bson.Document;

public class MongoManager {
    
    MongoDatabase base;
    MongoCollection<Document> col;

    public void createCollection(int filas, int tamaño, String path){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        base = mongoClient.getDatabase("miBD");

        col = base.getCollection("games");
        ArrayList<int[]> a1 = new ArrayList<>();
        Document partida = null;
        
        if(col.find() == null){
        
            base.createCollection("games");
            for(int i = -1; i < 3; i++){
                partida = new Document();
                partida.append("_id", i)
                        .append("tamaño", 0)
                        .append("filas", 1)
                        .append("path", "abcd")
                        .append("movsdes", a1)
                        .append("rehacer", a1);

                col.insertOne(partida);
            }
        }
        
        this.updatePartida(filas, tamaño, path);
        
    }
    
    //Going good
    public void addMovCommand(MovCommand mov, String type){
        col.updateOne(eq("_id", -1), Updates.push(type, Arrays.asList(mov.getResul()[0], mov.getResul()[1])));
    }
    
    public int[] tomarMovCommand(String type){
        
        String opuesto;
        
        if(type.equals("movsdes"))
            
            opuesto = "rehacer";
        
        else
            
            opuesto = "movsdes";
        
        Bson filter = new Document("_id", -1);
        Bson proj = Projections.fields(Projections.slice(type, -1), Projections.exclude("_id", "filas", "tamaño", "path", opuesto));
        
        Document doc = col.find(filter).projection(proj).first();
        
        ArrayList<ArrayList> arra = doc.get(type, ArrayList.class);
        
        if(arra.size() != 0){
        
            int[] a = new int[2];
            a[0] = (int) arra.get(0).get(0);
            a[1] = (int) arra.get(0).get(1);

            col.updateOne(eq("_id", -1), Updates.popLast(type));
        
            return a;
            
        }else
            
            return null;
 
    }
    
    public String guardarPartida(int id){
        
        Document doc = col.find(eq("_id", -1)).first();
        
        doc.put("_id", id);
        
        col.findOneAndReplace(eq("_id", id), doc);
        
        if(col.find(eq("_id", id)).first().get("movsdes").equals(doc.get("movsdes"))){
            
            return "Partida guardada correctamente";
            
        } else{
            
            return "No se pudo guardar la partida";
            
        }
        
    }
    
    public PartidaCarga cargarPartida(int id){
        Document doc = col.find(eq("_id", id)).first();

        PartidaCarga party = new PartidaCarga(doc.getInteger("tamaño"), doc.getInteger("filas"), doc.getString("path"));
        
        doc.put("_id", -1);
        
        col.findOneAndReplace(eq("_id", -1), doc);
        
        return party;
    }
    
    public void updatePartida(int filas, int tamaño, String path){
       
        Document partida = new Document();
        ArrayList<int[]> a1 = new ArrayList<>();
        partida.append("_id", -1)
                    .append("tamaño", tamaño)
                    .append("filas", filas)
                    .append("path", path)
                    .append("movsdes", a1)
                    .append("rehacer", a1);
        
        col.findOneAndReplace(new Document("_id", -1), partida);
        
    
    }
    
    public void limpiarStack(String type){

        Document doc = col.find(eq("_id", -1)).first();
        ArrayList<int[]> a1 = new ArrayList<>();
        doc.put(type, a1);
        col.findOneAndReplace(eq("_id",-1), doc );
        
    }
    
    public ArrayList<int[]> recorridoInicio(){
        Bson filter = new Document("_id", -1);
        Document doc = col.find(filter).first();

        ArrayList<ArrayList> lista = doc.get("movsdes", ArrayList.class);
        
        ArrayList<int[]> aux = new ArrayList();
        
        
        for(ArrayList a : lista){
        
            int[] a1 = new int[2];
            a1[0] = (int) a.toArray()[0];
            a1[1] = (int) a.toArray()[1];
            
            aux.add(a1);

        }
  
        return aux;
        
    }
    
}
