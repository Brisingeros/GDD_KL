package config;

import com.mongodb.MongoClient;
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

public class MongoManager extends BaseDatos{
    
    MongoDatabase base;
    MongoCollection<Document> col;

    public MongoManager(int filas, int tamaño, String path){
    
        super("Mongo", filas, tamaño, path);
        CreateCollection();
        
    }

    private void CreateCollection(){
        
        MongoClient mongoClient = new MongoClient("localhost", 27017); //Base de datos en local

        /*MongoClientURI uri = new MongoClientURI(
        "mongodb+srv://Brisin:1234@gdd-tdtb1.mongodb.net/"); //Base de datos cluster Frankfurt

        MongoClient mongoClient = new MongoClient(uri);*/
        
	base = mongoClient.getDatabase("miBD");

        col = base.getCollection("games");
        ArrayList<int[]> a1 = new ArrayList<>();
        Document partida;
        
        String c = base.listCollectionNames().first();

        if(c == null){ //Comprobamos si existe la colección y la creamos si fuese necesario, con una estructura básica
        
            base.createCollection("games");
            
            for(int i = 0; i < 3; i++){ //creamos los documentos de las partidas guardadas con estructura basica
                partida = new Document();
                partida.append("_id", i)
                        .append("tamaño", 0)
                        .append("filas", 1)
                        .append("path", "nulo")
                        .append("movsdes", a1)
                        .append("rehacer", a1);

                col.insertOne(partida);
            }
        }
        
        //creamos el documento de la partida actual
        partida = new Document();
        partida.append("_id", -1)
                .append("tamaño", tamaño)
                .append("filas", filas)
                .append("path", path)
                .append("movsdes", a1)
                .append("rehacer", a1);

        col.insertOne(partida);
        
    }

    @Override
    public void addMovCommand(MovCommand mov, String type){
        
        col.updateOne(eq("_id", -1), Updates.push(type, Arrays.asList(mov.getResul()[0], mov.getResul()[1])));
        
    }
    
    @Override
    public int[] tomarMovCommand(String type){
        
        String opuesto = type.equals("movsdes")?"rehacer" : "movsdes";
        
        Bson filter = new Document("_id", -1);
        Bson proj = Projections.fields(Projections.slice(type, -1), Projections.exclude("_id", "filas", "tamaño", "path", opuesto));
        
        Document doc = col.find(filter).projection(proj).first();
        
        ArrayList<ArrayList> arra = doc.get(type, ArrayList.class);
        
        if(!arra.isEmpty()){
        
            int[] a = new int[2];
            a[0] = (int) arra.get(0).get(0);
            a[1] = (int) arra.get(0).get(1);

            col.updateOne(eq("_id", -1), Updates.popLast(type));
            
            return a;
            
        }else
            
            return null;
 
    }
    
    @Override
    public String guardarPartida(int id, String path){
        
        Document doc = col.find(eq("_id", -1)).first();
        
        doc.put("_id", id);
        doc.put("path", path);
        
        col.findOneAndReplace(eq("_id", id), doc);
        
        return (col.find(eq("_id", id)).first().get("movsdes").equals(doc.get("movsdes")))?
                "Partida guardada correctamente":"No se pudo guardar la partida";
 
    }
    
    @Override
    public PartidaCarga cargarPartida(int id){
        
        Document doc = col.find(eq("_id", id)).first();

        if(!doc.get("path").equals("nulo")){
        
            PartidaCarga party = new PartidaCarga(doc.getInteger("tamaño"), doc.getInteger("filas"), doc.getString("path"));
        
            doc.put("_id", -1);

            col.findOneAndReplace(eq("_id", -1), doc);

            return party;
        
        }else
            
            return null;
        
    }
    
    @Override
    public void update(int filas, int tamaño, String path){
       
        Document partida = new Document();
        ArrayList<int[]> a1 = new ArrayList<>();
        partida.append("_id", -1)
                    .append("tamaño", tamaño)
                    .append("filas", filas)
                    .append("path", path)
                    .append("movsdes", a1)
                    .append("rehacer", a1);
        
        col.findOneAndReplace(new Document("_id", -1), partida);
        
        this.filas = filas;
        this.tamaño = tamaño;
        this.path = path;
        
    }
    
    @Override
    public void limpiarMovCommand(String type){

        Document doc = col.find(eq("_id", -1)).first();
        ArrayList<int[]> a1 = new ArrayList<>();
        doc.put(type, a1);
        col.findOneAndReplace(eq("_id",-1), doc );
        
    }
    
    @Override
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
    
    @Override
    public void vaciarActual(){
        col.deleteOne(eq("_id", -1));
    }
    
}
