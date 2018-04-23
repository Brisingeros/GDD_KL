package control;

import command.LoadCommand;
import command.MovCommand;
import config.BaseDatos;
import config.BaseXManager;
import config.MongoManager;
import config.Partida;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Modelo;
import observer.Observer;
import org.basex.core.Context;
import view.BoardView;
import view.InfoView;
import view.PuzzleGUI;

public class Controlador extends AbstractController{
    
    private Stack<MovCommand> movsDes = new Stack<>(); //Atrás
    private Stack<MovCommand> movsRe = new Stack<>(); //Alante
    private int desordenes;
    
    private final int megaBytes = 10241024;
    private Runtime gestor;
    private Modelo model;
    private BoardView view;

    private BaseDatos baseD;
    private String base;
    
    public void init(String b){
        base = b;
        desordenes = view.getFilas()*view.getColumnas()*9;
        
        //en funcion de la base de datos que se maneje, se creara un manejador de bdd de un tipo de hijo u otro
        if(base.equals("XML")){

            baseD = new BaseXManager(new Context(), view.getFilas(), view.getTamaño(), view.getPathImagenCompleta());
            emptyStacks();
            
        } else if(base.equals("Mongo")){
            
            baseD = new MongoManager(view.getFilas(), view.getTamaño(), view.getPathImagenCompleta());

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { //Método que maneja los eventos
               
        int[] posi;
        switch (e.getActionCommand()){
            
            case "exit": //borramos las imagenes divididas del directorio, y la partida actual de las bases de datos
                
                view.borrarImagenes();
                baseD.vaciarActual();
                System.exit(0);
            
            break;
            
            case "info": 
                
                new InfoView();
        
            break;
            
            case "load":
                
                LoadCommand loader = new LoadCommand(this);

                try {
                    
                    loader.execute();
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    
                }

            break;
            
            case "clutter":

                desordenar();
 
            break;
            
            case "solve":
                
                double timeSol = System.currentTimeMillis();
                int resols = resolver();
                timeSol = System.currentTimeMillis() - timeSol;
                gestor = Runtime.getRuntime();       
                this.mostrarPanel("Memoria usada: " + ((gestor.maxMemory() - gestor.freeMemory())/megaBytes) + " MB \n" +
                                  "Tiempo usado por movimiento: " + (timeSol/resols)/1000 + " s \n" + 
                                  "Tiempo usado en la operación: " + (timeSol/1000) + " s");
            break;
            
            case "deshacer":

                posi = baseD.tomarMovCommand("movsdes");

                if(posi != null) //si no es nulo el array, ejecutamos el movimiento y lo cambiamos de nodo

                    baseD.addMovCommand(deshacer(posi), "rehacer");

            break;
            
            case "rehacer":
                
                posi = baseD.tomarMovCommand("rehacer");

                if(posi != null) //si no es nulo el array, ejecutamos el movimiento y lo cambiamos de nodo

                    baseD.addMovCommand(rehacer(posi), "movsdes");
                
            break;
            
            case "guardaP0":

                this.guardarCommand(0); //guardamos en el id 0
                
            break;
            
            case "guardaP1":

                this.guardarCommand(1); //guardamos en el id 1
                
            break;
            case "guardaP2": 

                this.guardarCommand(2); //guardamos en el id 2
  
            break;
            case "cargaP0": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos
                
                this.cargarCommand(0);
            break;
            case "cargaP1": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                this.cargarCommand(1);
                
            break;
            case "cargaP2": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                this.cargarCommand(2);
                
            break;
            
            default:
                
            break;
        }
        
    }


    @Override
    public void notifyObservers(int blankPos, int movedPos) {

        for(Observer o:observerList){
            
            o.update(blankPos, movedPos);
            
        }
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if(e.getX() <= BoardView.imageWidth && e.getY() <= BoardView.imageHeight){

            int[] mov = view.movePiece(e.getX(), e.getY());
            
            if(mov != null){
                
                MovCommand move = new MovCommand(this, view, mov);
                //Limpiar rehacer
                baseD.limpiarMovCommand("rehacer");
                //Añadir el movcommand al tipo movsdes
                baseD.addMovCommand(move, "movsdes");
                move.redoCommand();
                
            }
            
        }
        
    }
    
    private void guardarCommand(int id ){ //Guardar partida actual en el id indicado
    
        String panel = null;

        view.guardarImagen(id); //cambiamos el path de la imagen completa a la carpeta resources/default
        
        panel = baseD.guardarPartida(id, view.getPathImagenCompleta()); //guardamos la partida en la base de datos
            
        this.mostrarPanel(panel); 
    
    }
    
    private void mostrarPanel(String p){

        JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), p);
    
    }
    
    private void cargarCommand(int id){ //Cargar partida desde la base de datos
    
        String panel;

        Partida game = baseD.cargarPartida(id); //datos de la partida con el id especificado
        
        if(!game.getPath().equals("")){ //si es distinto de null, la partida existe y se puede cargar

            cargarPartida(game); //configuramos la partida para recuperar el estado de guardado
            desordenInicio(baseD.recorridoInicio());//desordenamos segun lo movimientos realizados en la partida guardada
            panel = "Partida cargada correctamente";

        }else{

            panel = "Error al cargar partida";

        }

        this.mostrarPanel(panel);
    
    }
    
    public void desordenar(){
        
        double time = System.currentTimeMillis();

        baseD.limpiarMovCommand("rehacer");

        for(int i = 0; i < desordenes; i++){

            int[] mov = model.getRandomMovement(model.getBlancaAnterior(), view.getPiezaBlanca());
            MovCommand move = new MovCommand(this,view,mov);

            if(move.getResul() != null){

                move.redoCommand();
                baseD.addMovCommand(move,"movsdes");
            }

        }
        
        time = System.currentTimeMillis() - time;
        gestor = Runtime.getRuntime();
        this.mostrarPanel("Memoria usada: " + ((gestor.maxMemory() - gestor.freeMemory())/megaBytes) + " MB \n" +
                                  "Tiempo usado por movimiento: " + (time/desordenes)/1000 + " s \n" + 
                                  "Tiempo usado en la operación: " + (time/1000) + " s");

    }
    
    public MovCommand deshacer(int[] posi){

        MovCommand move = new MovCommand(this, view, posi);
        move.undoCommand();
        
        return move;
        
    }
    
    public MovCommand rehacer(int[] posi){ //PASAMOS INT[] DE PARAMETRO
        

            MovCommand move = new MovCommand(this, view, posi);
            move.redoCommand();

            
        return move;

    }
    
    public void emptyStacks(){ //limpiar pilas de movimientos

        baseD.limpiarMovCommand("rehacer");
        baseD.limpiarMovCommand("movsdes");

    
    }
    
    public void addModelo(Modelo m){
        
        model = m;
        
    }
    
    public void addView(BoardView v){
        
        view = v;
        
    }
    
    
    private int resolver() {
        
        int[] posi = baseD.tomarMovCommand("movsdes");
        int resols = 0;
        
        while(posi != null){
            int[] posiaux = baseD.tomarMovCommand("movsdes");
            deshacer(posi);
            posi = posiaux;
            resols++;

        }

        return resols;
        
    }
    
    private void cargarPartida(Partida game){ //configuracion de la partida para recuperar el estado de guardado
        try {

                removeObserver(model);

                PuzzleGUI.getInstance().initCarga(game.getFilas(), game.getTamaño(), game.getPath());

                view = PuzzleGUI.getInstance().getBoardView();

                model = new Modelo(game.getFilas(), game.getFilas(), game.getTamaño());

                this.addObserver(model);                         
                
        } catch (IOException ex) {
            
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    private void desordenInicio(ArrayList<int[]> movimientos){ //desorden de la imagen de la partida cargada en funcion de los movimientos que almacena

        for(int[] mov:movimientos){
        
            MovCommand move = new MovCommand(this, view, mov);
            move.redoCommand();
            
        }
        
    }
    
    public void Restart(){ //actualizacion de la partida por carga de nueva imagen
        
        removeObserver(model);
        model = new Modelo(view.getFilas(), view.getColumnas(), view.getAltoImagen()*view.getColumnas());
        
        addObserver(model);
        addModelo(model);
        
        desordenes = view.getFilas()*view.getColumnas()*9;

        baseD.update(view.getFilas(), view.getTamaño(), view.getPathImagenCompleta());//actualizamos los datos en la base de datos
        
        desordenar();
        
    }
    
    
    //metodo para borrar las imagenes divididas y limpiar la partida actual de la base de datos una vez se cierre la ventana pulsando sobre la X
    @Override
    public void windowClosing(WindowEvent we) {
        
        view.borrarImagenes();
        baseD.vaciarActual();
        
    }

    public Modelo getModel() {
        
        return model;
        
    }

    public void setModel(Modelo model) {
        
        this.model = model;
        
    }

    public BoardView getView() {
        
        return view;
        
    }

    public void setView(BoardView view) {
        
        this.view = view;
        
    }

    
    public Stack<MovCommand> getMovsDes() {
        
        return movsDes;
        
    }

    public Stack<MovCommand> getMovsRe() {
        
        return movsRe;
        
    }

    public void setMovsDes(Stack<MovCommand> movsDes) {
        
        this.movsDes = movsDes;
        
    }

    public void setMovsRe(Stack<MovCommand> movsRe) {
        
        this.movsRe = movsRe;
        
    }

    @Override
    public void windowOpened(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void windowClosed(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
}