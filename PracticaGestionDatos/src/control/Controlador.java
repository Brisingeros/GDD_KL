package control;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import observer.Observer;
import view.BoardView;
import view.PuzzleGUI;

/**
 *
 * @author Brisin
 */
public class Controlador extends AbstractController{
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getActionCommand());
        
        switch (e.getActionCommand()){
            case "clutter":
                System.out.println("Desordenar");
                disorder.execute();
            break;
            
            case "solve":
                System.out.println("Solucionar");
                disorder.undoCommand();
            break;
            
            default:
            break;
        }
    }

    @Override
    public void notifyObservers(int blankPos, int movedPos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        for(Observer o:observerList){
            o.update(blankPos, movedPos);
        }
    }
    
    public void mouseClicked(MouseEvent e) {
        BoardView tablero = PuzzleGUI.getInstance().getBoardView();
        
        if(e.getX() <= tablero.imageWidth && e.getY() <= tablero.imageHeight){
            
            int[] resul = tablero.movePiece(e.getX(), e.getY());
            if(resul != null){
                
                notifyObservers(resul[0], resul[1]);
                disorder.addMov(resul);
            }
        }
    }
    
}
