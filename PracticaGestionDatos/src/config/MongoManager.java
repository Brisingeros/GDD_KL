package config;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import command.MovCommand;
import javax.swing.text.Document;

public class MongoManager {
    
    MongoDatabase base;

    public void init(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        base = mongoClient.getDatabase("miBD");
    }
    
    public void addColleccion(String name){
        base.createCollection(name);
        
        System.out.println(base.listCollectionNames());
    }
    
    public void insertarMov(MovCommand mov){
        MongoCollection<org.bson.Document> col = base.getCollection("pilas");
        BasicDBObject document = new BasicDBObject();
    }
    
    public void insertarPartida(PartidaXML party){
        MongoCollection<org.bson.Document> col = base.getCollection("pilas");
        BasicDBObject document = new BasicDBObject();
    }
    
}
