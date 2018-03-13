package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.PieceView;
import view.PuzzleGUI;

/**
 *
 * @author Brisin
 */
public class Modelo extends AbstractModel{

    public int[] piezas;
    
    //Nuevas variables

    public Modelo(int rowNum, int columnNum, int pieceSize, String[] imageList) {
        super(rowNum, columnNum, pieceSize, imageList);
        
        piezas = new int[rowNum*columnNum];
        
        for(int i = 0; i < rowNum; i++){
            for(int j = 0; j < columnNum; j++){
                piezas[i*columnNum + j] = i*columnNum + j;
            } 
        } 
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPuzzleSolve() {
        boolean bien = true;
        
        for(int i = 0; i < rowNum*columnNum && bien; i++){
            bien = piezas[i] == i;
        }
        
        return bien;
    }

    @Override
    public int[] getRandomMovement(int lastPos, int pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //Aquí haremos el desordenar, comprobando no hacer bucles simples
    }

    @Override
    public void update(int blankPos, int movedPos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int aux = piezas[blankPos];
        
        piezas[blankPos] = piezas[movedPos];
        piezas[movedPos] = aux;
        
        if(isPuzzleSolve()){
            try {
                JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "¡Has ganado!");
            } catch (IOException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
