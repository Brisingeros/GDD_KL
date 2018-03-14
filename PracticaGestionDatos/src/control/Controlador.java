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
import model.Modelo;
import observer.Observer;
import view.BoardView;
import view.PuzzleGUI;


public class Controlador extends AbstractController{
    
    private Stack<MovCommand> movsDes = new Stack<>(); //Atrás
    private Stack<MovCommand> movsRe = new Stack<>(); //Alante
    private Random aleatorio = new Random(System.currentTimeMillis());
    
    public Modelo model;
    public BoardView view;
    
    public int desordenes = 200;
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        switch (e.getActionCommand()){
            case "load":
                LoadCommand loader = new LoadCommand(this);
                System.out.println("Cargar");
                loader.redoCommand();
                
                try {
                    addView(PuzzleGUI.getInstance().getBoardView());
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Restart();
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
            
            case "guardar":
        
                try {
                    view.imagenesGuardadas();
                    model.setPaths(view.getPaths());
                    
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                PartidaLD partida = new PartidaLD(model, movsDes);
                try {
                    Configuracion.toJSON(partida);
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                } 
            break;
            
            case "cargar":
                PartidaLD party = null;
                try {
                    party = Configuracion.cargarPartida();
                    CargarPartida(party);
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
            MovCommand move = new MovCommand(this, view, e.getX(), e.getY());
            move.redoCommand();
            movsDes.push(move);
        }
    }
    
    public void desordenar(){
    
        movsRe.clear();
        for(int i = 0; i < desordenes; i++){
        
            int[] mov = model.getRandomMovement(model.getBlancaAnterior(), view.getPiezaBlanca());
            MovCommand move = new MovCommand(this,view,mov);
            move.redoCommand();
            movsDes.push(move);
            
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

    private void CargarPartida(PartidaLD party) throws IOException {
        removeObserver(model);
        removeObserver(view);
        model = party.getModelo();
        
        PuzzleGUI.getInstance().initCarga(model.getRowCount(), model.getColumnCount(), model.getPieceSize(), model.getIconArray());
        
        addObserver(model);
        view = PuzzleGUI.getInstance().getBoardView();
        movsRe.clear();
        
        movsDes = party.getDeshacerMovs();
    }
    
    
    
}
