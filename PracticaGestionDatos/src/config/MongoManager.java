package config;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import command.MovCommand;
import java.util.ArrayList;
import javax.swing.text.Document;
import org.bson.conversions.Bson;

public class MongoManager {
    
    MongoDatabase base;
    MongoCollection<org.bson.Document> col;

    public void createCollection(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        base = mongoClient.getDatabase("miBD");
        
        base.createCollection("games");
        col = base.getCollection("games");
        
        int[][] a1 = new int[1][2];
        //a1[1] = new int[2];
        for(int i = -1; i < 3; i++){
            org.bson.Document partida = new org.bson.Document();
            partida.append("_id", i)
                    .append("tamaño", 0)
                    .append("filas", 1)
                    .append("path", "abcd")
                    .append("movsdes", a1)
                    .append("rehacer", a1);
            
            col.insertOne(partida);
        }
    }
    
    public void addMovCommand(MovCommand mov, String type){
        col = base.getCollection("games");
        //BasicDBObject document = new BasicDBObject();
        
        //col.updateOne(eq("_id", -1), "$push");
        
        String a = mov.getResul()[0] + "," + mov.getResul()[1];
        
        //BasicDBObject doc = new BasicDBObject("mov", a);
        
        org.bson.Document update = new org.bson.Document("id","-1");
        
        org.bson.Document command = new org.bson.Document("$push", new org.bson.Document(type, mov.getResul()));
        
    }
    
    public int[] tomarMovCommand(String type){
        Bson filter = new org.bson.Document("_id", -1).append(type + ".$", -1);
        
        ArrayList<Integer> a = col.find(filter).into(new ArrayList());
        col.findOneAndDelete(filter);
        
        int[] array = new int[2];
        array[0] = a.get(0);
        array[1] = a.get(1);
        
        return array;
    }
    
    public void guardarPartida(int id){
    
    }
    
    public void cargarPartida(int id){
    
    }
    
    /*
    public void insertarPartida(PartidaXML party){
        col = base.getCollection("pilas");
        BasicDBObject document = new BasicDBObject();
    }*/
    
}
