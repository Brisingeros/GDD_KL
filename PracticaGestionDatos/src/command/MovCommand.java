package command;
import control.Controlador;
import view.BoardView;

public class MovCommand implements Command{
    
    private transient BoardView tablero;
    private transient Controlador control;
    
    private int[] resul;

    public MovCommand(Controlador c, BoardView tab, int[] x){ //Constructor para movimiento aleatorio
        
        this.tablero = tab;
        this.control = c;  
        this.resul = x;
        
    }
    
    @Override
    public void undoCommand() {
        
        this.control.notifyObservers(this.resul[1], this.resul[0]);
        
    }

    @Override
    public void redoCommand() {

        this.control.notifyObservers(this.resul[0], this.resul[1]);
        
    }

    public int[] getResul(){
        
        return this.resul;
        
    }
    
    @Override
    public String toString(){
        return ("element array{'" + resul[0] + "," + resul[1] + "'}");
    }
     
}
