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
import org.bson.conversions.Bson;
import org.bson.Document;

public class MongoManager {
    
    MongoDatabase base;
    MongoCollection<Document> col;

    public void createCollection(int filas, int tamaño, String path){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        base = mongoClient.getDatabase("miBD");
        
        //base.createCollection("games");
        col = base.getCollection("games");
        
        //int[][] a1 = new int[1][2];
        ArrayList<int[]> a1 = new ArrayList<int[]>();
        //a1[1] = new int[2];
        
        Document partida = null;
        /*
        for(int i = -1; i < 3; i++){
            partida = new Document();
            partida.append("_id", i)
                    .append("tamaño", 0)
                    .append("filas", 1)
                    .append("path", "abcd")
                    .append("movsdes", a2)
                    .append("rehacer", a2);
            
            col.insertOne(partida);
        }*/
        
        partida = new Document();
        partida.append("_id", -1)
                    .append("tamaño", tamaño)
                    .append("filas", filas)
                    .append("path", path)
                    .append("movsdes", a1)
                    .append("rehacer", a1);
        
        col.findOneAndReplace(new Document("_id", -1), partida);
    }
    
    //Going good
    public void addMovCommand(MovCommand mov, String type){
        col.updateOne(eq("_id", -1), Updates.push(type, Arrays.asList(mov.getResul()[0], mov.getResul()[1])));
    }
    
    public int[] tomarMovCommand(String type){
        
        Bson filter = new Document("_id", -1);
        Bson proj = Projections.fields(Projections.slice(type, -1), Projections.exclude("_id", "filas", "tamaño", "path", "rehacer"));
        
        FindIterable<Document> doc = col.find(filter).projection(proj);
        
        System.out.println(doc);
        
        Document arra = doc.first();
        
        String json = arra.toJson();
        Gson gson = new Gson();
        
        System.out.println(json);
        
        ArrayList<int[]> array = gson.fromJson(json, ArrayList.class);
        
        System.out.println(array.get(0));
        
        col.updateOne(eq("_id", -1), Updates.popLast(type));
        
        return array.get(0);
        
        //return null;
    }
    
    public void guardarPartida(int id){
    
    }
    
    public void cargarPartida(int id){
    
    }
    
    public void limpiarStack(String type){
        Bson filter = new Document("_id", -1);
        col.replaceOne(filter, new Document().append(type, null));
    }
    
    /*
    public void insertarPartida(PartidaXML party){
        col = base.getCollection("pilas");
        BasicDBObject document = new BasicDBObject();
    }*/
    
    public ArrayList<int[]> recorridoInicio(){
        Bson filter = new Document("_id", -1);
        Bson proj = Projections.fields(Projections.include("movsdes"), Projections.exclude("_id"));
        
        Document doc = col.find(filter).projection(proj).first();
        
        String json = doc.toJson();
        Gson gson = new Gson();
        ArrayList<int[]> lista = gson.fromJson(json, ArrayList.class);
        
        
        return lista;
    }
    
}
