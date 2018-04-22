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
import java.util.Random;
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
    private Random aleatorio = new Random(System.currentTimeMillis());
    private int desordenes = 20;
    
    private final int megaBytes = 10241024;
    private Runtime gestor;
    private Modelo model;
    private BoardView view;

    private BaseDatos baseD;
    private String base;
    
    public void init(String b){
        base = b;
        
        if(base.equals("XML")){

            baseD = new BaseXManager(new Context(), view.getFilas(), view.getTamaño(), view.getPathImagenCompleta());
            emptyStacks();
            
        } else{
            
            baseD = new MongoManager(view.getFilas(), view.getTamaño(), view.getPathImagenCompleta());

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { //Método que maneja los eventos
               
        int[] posi;
        switch (e.getActionCommand()){
            
            case "exit": 
                
                view.borrarImagenes();
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

                double time = System.currentTimeMillis();
                
                desordenar();

                time = System.currentTimeMillis() - time;
                gestor = Runtime.getRuntime();
                this.mostrarPanel("Memoria usada: " + ((gestor.maxMemory() - gestor.freeMemory())/megaBytes) + " MB \n" +
                                  "Tiempo usado por movimiento: " + (time/desordenes)/1000 + " s \n" + 
                                  "Tiempo usado en la operación: " + (time/1000) + " s");
                
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

                if(posi != null)

                    baseD.addMovCommand(deshacer(posi), "rehacer");

            break;
            
            case "rehacer":
                
                posi = baseD.tomarMovCommand("rehacer");

                if(posi != null)

                    baseD.addMovCommand(rehacer(posi), "movsdes");
                
            break;
            
            case "guardaP0":

                this.guardarCommand(0);
                
            break;
            
            case "guardaP1":

                this.guardarCommand(1);
                
            break;
            case "guardaP2": 

                this.guardarCommand(2);
  
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
    
    private void guardarCommand(int id ){
    
        String panel = null;

        view.guardarImagen(id);

        if(base.equals("XML")){

            panel = baseD.guardarPartida(id, view.getPathImagenCompleta());

        }else if(base.equals("Mongo")){

            panel = baseD.guardarPartida(id, view.getPathImagenCompleta());

        }
            
        this.mostrarPanel(panel); 
    
    }
    
    private void mostrarPanel(String p){

        JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), p);
    
    }
    
    private void cargarCommand(int id){
    
        String panel;

        Partida game = baseD.cargarPartida(id);
        if(game != null){

            cargarPartida(game);
            desordenInicio(baseD.recorridoInicio());
            panel = "Partida cargada correctamente";

        }else{

            panel = "Error al cargar partida";

        }

        this.mostrarPanel(panel);
    
    }
    
    public void desordenar(){

        baseD.limpiarMovCommand("rehacer");

        for(int i = 0; i < desordenes; i++){

            int[] mov = model.getRandomMovement(model.getBlancaAnterior(), view.getPiezaBlanca());
            MovCommand move = new MovCommand(this,view,mov);

            if(move.getResul() != null){

                move.redoCommand();
                baseD.addMovCommand(move,"movsdes");
            }

        }

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
    
    public void emptyStacks(){

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
    
    private void cargarPartida(Partida game){ //PASAMOS STRING DE PARAMETRO PARA EL JOPTIONPANE Y OBJETO DE PARTIDA
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
    
    private void desordenInicio(ArrayList<int[]> movimientos){ //PASAMOS LISTA DE INT[] DE PARAMETRO

        for(int[] mov:movimientos){
        
            MovCommand move = new MovCommand(this, view, mov);
            move.redoCommand();
            
        }
        
    }
    
    public void Restart(){
        
        removeObserver(model);
        model = new Modelo(view.getFilas(), view.getColumnas(), view.getAltoImagen()*view.getColumnas());
        
        addObserver(model);
        addModelo(model);
        
        desordenes = view.getFilas()*view.getColumnas()*9;

        baseD.update(view.getFilas(), view.getTamaño(), view.getPathImagenCompleta());
        
        desordenar();
        
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
    public void windowClosing(WindowEvent we) {
        view.borrarImagenes();
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