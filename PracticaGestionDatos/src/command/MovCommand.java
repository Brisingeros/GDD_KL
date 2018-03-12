package command;

import control.Controlador;
import view.BoardView;

public class MovCommand implements Command{
    
    private BoardView tablero;
    private Controlador control;
    
    private int[] resul;

    public MovCommand(Controlador c, BoardView tab, int x, int y){
        tablero = tab;
        control = c;
        
        resul = tablero.movePiece(x, y);
    }
    
    public MovCommand(Controlador c, BoardView tab, int[] x){
        tablero = tab;
        control = c;
        
        resul = x;
    }
    
    @Override
    public void undoCommand() {
        if(resul != null)
        control.notifyObservers(resul[1], resul[0]);
    }

    @Override
    public void redoCommand() {
        if(resul != null)
        control.notifyObservers(resul[0], resul[1]);
    }
    
    public boolean compareCommand(int[] c){
        boolean igual = true;
        int index = 0;
        int index2 = c.length-1;
        
        while((igual) && (index < resul.length)){
            igual = c[index2] == resul[index];
            index++;
            index2--;
        }
        
        return igual;
    }

    public int[] getResul(){
        return resul;
    }
    
}
