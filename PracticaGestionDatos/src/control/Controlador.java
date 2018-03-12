package control;

import command.LoadCommand;
import command.MovCommand;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import observer.Observer;
import view.BoardView;
import view.PuzzleGUI;


public class Controlador extends AbstractController{
    
    public Stack<MovCommand> movsDes = new Stack<>(); //Atrás
    public Stack<MovCommand> movsRe = new Stack<>(); //Alante
    public Random aleatorio = new Random(System.currentTimeMillis());
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getActionCommand());
        
        switch (e.getActionCommand()){
            case "load":
                LoadCommand loader = new LoadCommand(this);
                System.out.println("Cargar");
                loader.redoCommand();
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
    
    public void mouseClicked(MouseEvent e) {
        BoardView tablero = PuzzleGUI.getInstance().getBoardView();
        
        if(e.getX() <= tablero.imageWidth && e.getY() <= tablero.imageHeight){
            MovCommand move = new MovCommand(this, tablero, e.getX(), e.getY());
            move.redoCommand();
            movsDes.push(move);
        }
    }
    
    public void desordenar(){
        BoardView tablero = PuzzleGUI.getInstance().getBoardView();
        
        for(int i = 0; i < 99; i++){
            int[] posi = {aleatorio.nextInt(tablero.imageWidth), aleatorio.nextInt(tablero.imageHeight)};
            MovCommand move = new MovCommand(this, tablero, posi[0], posi[1]);
            
            if(!movsDes.empty()){
                
                MovCommand prev = movsDes.peek();
                
                if(!move.compareCommand(prev.getResul())){
                    move.redoCommand();
                    movsDes.push(move);
                }
            } else{
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
    
    public void rehacer(){
        MovCommand move = movsDes.pop();
        move.undoCommand();
        movsRe.push(move);
    }
    
    public void deshacer(){
        MovCommand move = movsRe.pop();
        move.redoCommand();
        movsDes.push(move);
    }
    
    public void emptyStacks(){
        movsRe = new Stack<MovCommand>();
        movsDes = new Stack<MovCommand>();
    }
    
}
