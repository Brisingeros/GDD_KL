package command;
import control.Controlador;
import view.BoardView;

public class MovCommand implements Command{
    
    private transient BoardView tablero;
    private transient Controlador control;
    
    private int[] resul;

    public MovCommand(Controlador c, BoardView tab, int x, int y){
        
        this.tablero = tab;
        this.control = c;
        this.resul = tablero.movePiece(x, y);
        
    }
    
    public MovCommand(Controlador c, BoardView tab, int[] x){
        
        this.tablero = tab;
        this.control = c;  
        this.resul = x;
        
    }
    
    @Override
    public void undoCommand() {
        
        if(resul != null)
            
            this.control.notifyObservers(this.resul[1], this.resul[0]);
        
    }

    @Override
    public void redoCommand() {
        
        if(this.resul != null)
            
            this.control.notifyObservers(this.resul[0], this.resul[1]);
        
    }

    public int[] getResul(){
        
        return this.resul;
        
    }
     
}
