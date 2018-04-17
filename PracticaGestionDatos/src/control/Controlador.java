package control;

import command.LoadCommand;
import command.MovCommand;
import config.BaseXManager;
import config.Configuracion;
import config.PartidaXML;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
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
    private int desordenes = 200;
    
    private Modelo model;
    private BoardView view;
    
    private BaseXManager manager; // = new BaseXManager();
    private Context contexto; // = new Context();

    @Override
    public void actionPerformed(ActionEvent e) { //Método que maneja los eventos
        
        switch (e.getActionCommand()){
            
            case "exit": 
                
                view.borrarImagenes();
                System.exit(0);
            
            break;
            
            case "info": 
                
                try {
                    
                    InfoView info = new InfoView();
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
        
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
                
                ordenar();
                
            break;
            
            case "deshacer":
                
                deshacer();
                
            break;
            
            case "rehacer":
                
                rehacer();
                
            break;
            
            case "guardaP0":
                try {
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), manager.guardarPartida(contexto, 0, view.getFilas(), view.getTamaño(), view.getPathImagenCompleta()));
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            break;
            
            case "guardaP1":
                
                try {
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), manager.guardarPartida(contexto, 1, view.getFilas(), view.getTamaño(), view.getPathImagenCompleta()));
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            break;
            case "guardaP2": 
                         
                try {
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), manager.guardarPartida(contexto, 2, view.getFilas(), view.getTamaño(), view.getPathImagenCompleta()));
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
  
            break;
            case "cargaP0": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                    this.cargarPartidaXml(0);

            break;
            case "cargaP1": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                    this.cargarPartidaXml(1);
                
            break;
            case "cargaP2": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                    this.cargarPartidaXml(2);

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
                manager.limpiarMovCommand(contexto, "rehacer");
                
                move.redoCommand();
                
                //Añadir el movcommand al tipo movsdes
                manager.addMovCommand(move, contexto, "movsdes");
                
            }
            
        }
        
    }
    
    public void inicioContexto(){
        
        manager = new BaseXManager();
        contexto = new Context();

        manager.createCollection("Pilas",contexto);
        
        emptyStacks();
        
    }
    
    public void desordenar(){
        
        manager.limpiarMovCommand(contexto, "rehacer");
        
        for(int i = 0; i < desordenes; i++){
            int[] mov = model.getRandomMovement(model.getBlancaAnterior(), view.getPiezaBlanca());
            MovCommand move = new MovCommand(this,view,mov);
            
            if(move.getResul() != null){
                
                move.redoCommand();
                manager.addMovCommand(move, contexto, "movsdes");
                
            }
        }
    
    }
    
    public void ordenar(){
        
        String posi = manager.tomarMovCommand(contexto, "movsdes");
        String[] aux;
        int[] values;
        
        while(posi != ""){
            aux = posi.split(",");
            values = new int[2];
            for(int i = 0; i < values.length; i++){
                values[i] = Integer.parseInt(aux[i]);
            }

            MovCommand move = new MovCommand(this, view, values);
            move.undoCommand();
            
            //manager.addMovCommand(move, contexto, "rehacer"); //No creo que deban poder rehacerse
            
            posi = manager.tomarMovCommand(contexto, "movsdes");
        }
        
    }
    
    public void deshacer(){

        String posi = manager.tomarMovCommand(contexto, "movsdes");
        String[] aux;
        int[] values;
        
        if(posi != ""){
        
            aux = posi.split(",");
            values = new int[2];
            for(int i = 0; i < values.length; i++){
                values[i] = Integer.parseInt(aux[i]);
            }

            MovCommand move = new MovCommand(this, view, values);
            move.undoCommand();

            manager.addMovCommand(move, contexto, "rehacer");
            
        }
        
    }
    
    public void rehacer(){
        
        String posi = manager.tomarMovCommand(contexto, "rehacer");
        String[] aux;
        int[] values;
        
        if(posi != ""){
        
            aux = posi.split(",");
            values = new int[2];
            for(int i = 0; i < values.length; i++){
                values[i] = Integer.parseInt(aux[i]);
            }

            MovCommand move = new MovCommand(this, view, values);
            move.redoCommand();

            manager.addMovCommand(move, contexto, "movsdes");
            
        }

    }
    
    public void emptyStacks(){
        
        manager.limpiarMovCommand(contexto, "rehacer");
        manager.limpiarMovCommand(contexto, "movsdes");
        
    }
    
    public void addModelo(Modelo m){
        
        model = m;
        
    }
    
    public void addView(BoardView v){
        
        view = v;
        
    }
    
        
    private void cargarPartidaXml(int id){
        try {
            String query = manager.cargarPartida(contexto, id);
            
            if(query != null){
            
                PartidaXML game = Configuracion.parseXML(query);
            
                removeObserver(model);

                PuzzleGUI.getInstance().initCarga(game.getFilas(), game.getTamaño(), game.getPath());

                view = PuzzleGUI.getInstance().getBoardView();

                model = new Modelo(game.getFilas(), game.getFilas(), game.getTamaño(), view.getPathsPiezas());

                this.addObserver(model);

                this.desordenInicio();
                
                JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "Partida cargada correctamente");
            
            }else
                
                JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "Error al cargar partida");
                
            
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void desordenInicio(){
        String aux1 = manager.recorridoInicio(contexto);
        
        String[] aux2 = aux1.split("\r\n");
        String[] aux3;
        int[] values;
        
        for(int i = 0; i < aux2.length; i++){
            aux3 = aux2[i].split(",");
            
            values = new int[aux3.length];
            for(int j = 0; j < values.length; j++){
                values[j] = Integer.parseInt(aux3[j]);
            }
            
            MovCommand move = new MovCommand(this, view, values);
            move.redoCommand();
        }
    }
    
    public void Restart(){
        
        removeObserver(model);
        model = new Modelo(view.getFilas(), view.getColumnas(), view.getAltoImagen()*view.getColumnas(), view.getPathsPiezas());
        
        addObserver(model);
        addModelo(model);
        
        desordenes = view.getFilas()*view.getColumnas()*9;
        this.desordenar();
        
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
    
}