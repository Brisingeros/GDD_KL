package control;

import command.LoadCommand;
import command.MovCommand;
import config.Configuracion;
import config.PartidaLD;
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
                
                try{
                    
                    ordenar();
                    
                } catch(Exception y){
                    
                    System.out.println("Vacío");
                    
                }
                
            break;
            
            case "deshacer":
                
                try{
                    
                    deshacer();
                    
                } catch(Exception y){
                    
                    System.out.println("Vacío");
                    
                }
                
            break;
            
            case "rehacer":
                
                try{
                    
                    rehacer();
                    
                } catch(Exception y){
                    
                    System.out.println("Vacío");
                    
                }
                
            break;
            
            case "guardar": //Creamos un objeto partida con los datos actuales
        
                try {
                    
                    view.imagenesGuardadas();
                    model.setPaths(view.getPaths());
                    PartidaLD partida = new PartidaLD(model, movsDes);            
                    Configuracion.toJSON(partida);
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "Partida guardada");
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    
                } 
                
            break;
            
            case "cargar": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos
                
                PartidaLD party = null;
                
                try {
                    
                    party = Configuracion.cargarPartida();
                    CargarPartida(party);
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "Partida cargada");
                   
                } catch (IOException ex) {
                    
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    
                }  

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
                movsRe.clear();
                move.redoCommand();
                movsDes.push(move);
                
            }
            
        }
        
    }
    
    public void desordenar(){
    
        movsRe.clear();
        for(int i = 0; i < desordenes; i++){
        
            int[] mov = model.getRandomMovement(model.getBlancaAnterior(), view.getPiezaBlanca());
            MovCommand move = new MovCommand(this,view,mov);
            
            if(move.getResul() != null){
                
                move.redoCommand();
                movsDes.push(move);
                
            }
            
        }
    
    }
    
    public void ordenar(){
        
        while(!movsDes.empty()){
            
            MovCommand move = movsDes.pop();
            move.undoCommand();
            movsRe.push(move);
            
        }
        
    }
    
    public void deshacer(){
        
        MovCommand move = movsDes.pop();
        move.undoCommand();
        movsRe.push(move);
        
    }
    
    public void rehacer(){
        
        MovCommand move = movsRe.pop();
        move.redoCommand();
        movsDes.push(move);
        
    }
    
    public void emptyStacks(){
        
        movsRe = new Stack<MovCommand>();
        movsDes = new Stack<MovCommand>();
        
    }
    
    public void addModelo(Modelo m){
        
        model = m;
        
    }
    
    public void addView(BoardView v){
        
        view = v;
        
    }
    
    public void Restart(){
        
        removeObserver(model);
        model = new Modelo(view.getFilas(), view.getColumnas(), view.getAltoImagen()*view.getColumnas(), view.getPaths());
        
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

    private void CargarPartida(PartidaLD party) throws IOException { //Obtenemos el modelo y la pila del fichero de carga
        
        removeObserver(model);
        model = party.getModelo();
        
        System.out.println("Tamaño pieza: " + model.getPieceSize());
        PuzzleGUI.getInstance().initCarga(model.getRowCount(), model.getColumnCount(), model.getPieceSize(), model.getIconArray()); //Método que maneja la creación de un nuevo boardView
        
        addObserver(model);
        view = PuzzleGUI.getInstance().getBoardView();
        movsRe.clear();
        movsDes.clear();
        
        Stack<MovCommand> aux = new Stack<>();
        
        while(!party.getDeshacerMovs().empty()){
            
            aux.push(new MovCommand(this, view,party.getDeshacerMovs().pop().getResul()));
            
        }
        
        while(!aux.empty()){

            movsDes.push(aux.pop());
            
        }
        
    }
 
}