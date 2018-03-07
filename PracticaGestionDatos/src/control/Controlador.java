package control;

import command.DisorderCommand;
import command.LoadCommand;
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
    
    public Stack<int[]> movs = new Stack();
    public Random aleatorio = new Random(System.currentTimeMillis());
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getActionCommand());
        
        switch (e.getActionCommand()){
            case "load": 
                LoadCommand loader = new LoadCommand();
                System.out.println("Cargar");
                loader.redoCommand();
            break;
            case "clutter":
                System.out.println("Desordenar");
                //disorder.execute();
                desordenar();
            break;
            
            case "solve":
                System.out.println("Solucionar");
                //disorder.undoCommand();
                ordenar();
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
            
            int[] resul = tablero.movePiece(e.getX(), e.getY());
            if(resul != null){
                movs.push(resul);
                notifyObservers(resul[0], resul[1]);
            }
        }
    }
    
    public void desordenar(){
        BoardView tablero = PuzzleGUI.getInstance().getBoardView();
        
        for(int i = 0; i < 99; i++){
            int[] resul = tablero.movePiece(aleatorio.nextInt(97), aleatorio.nextInt(97));
            if(resul != null){
                movs.push(resul);
                notifyObservers(resul[0], resul[1]);
            }
        }
    }
    
    public void ordenar(){
        while(!movs.empty()){
            int[] movi = movs.pop();
            notifyObservers(movi[1], movi[0]);
        }
    }
    
}
