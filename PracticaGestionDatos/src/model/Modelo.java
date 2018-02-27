package model;

/**
 *
 * @author Brisin
 */
public class Modelo extends AbstractModel{

    public Modelo(int rowNum, int columnNum, int pieceSize, String[] imageList) {
        super(rowNum, columnNum, pieceSize, imageList);
        /*
        for(int i = 0; i < rowNum; i++){
            for(int j = 0; j < columnNum; j++){
                addNewPiece((columnNum * i + j), i, j);
            } 
        }
        */
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getRandomMovement(int lastPos, int pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(int blankPos, int movedPos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
