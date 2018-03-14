package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.PuzzleGUI;

/**
 *
 * @author Brisin
 */
public class Modelo extends AbstractModel{

    private ArrayList<PieceModel> iconArray = null;
    
    //Nuevas variables

    public Modelo(int rowNum, int columnNum, int pieceSize, String[] imageList) {
        super(rowNum, columnNum, pieceSize, imageList);
        
        iconArray = new ArrayList<PieceModel>();
        
        for(int i = 0; i < rowNum; i++){
            for(int j = 0; j < columnNum; j++){
                addNewPiece(i*columnNum + j, i, j, imageList[i*columnNum + j]);
            } 
        } 
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol, String imagePath) {
        iconArray.add(new PieceModel(id, indexRow, indexCol, imagePath));
    }

    @Override
    public void addNewPiece(int id, int indexRow, int indexCol) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPuzzleSolve() {
        boolean bien = true;
        
        for(int i = 0; i < rowNum*columnNum && bien; i++){
            bien = iconArray.get(i).getId() == i;
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
        
        PieceModel p = iconArray.get(blankPos);
        PieceModel p2 = iconArray.get(movedPos);
        
        int auxX = p.getJ();
        int auxY = p.getI();
        
        p.setJ(p2.getJ());
        p.setI(p2.getI());
        
        p2.setJ(auxX);
        p2.setI(auxY);
        
        iconArray.set(blankPos, p2);
        iconArray.set(movedPos, p);
        
        if(isPuzzleSolve()){
            try {
                JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "¡Has ganado!");
            } catch (IOException ex) {
                Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<PieceModel> getIconArray() {
        return iconArray;
    }
    
}
