package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.PuzzleGUI;
import model.PieceModel;
/**
 *
 * @author Brisin
 */
public class Modelo extends AbstractModel{

    private ArrayList<PieceModel> iconArray = null;
    private int blancaAnterior;
    //Nuevas variables

    public Modelo(int rowNum, int columnNum, int pieceSize, String[] imageList) {
        super(rowNum, columnNum, pieceSize, imageList);
        
        blancaAnterior = 0;
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

    public void setPaths(String[] newPaths){
    
        /*for(int i = 0; i < rowNum; i++){
        
            for(int j = 0; j < columnNum; j++){
            
                for(PieceModel p:iconArray){
            
                    if(p.getId() == i*columnNum+j)
                        p.setPath(newPaths[i*columnNum+j]);
                }
                
            
            }
        
        }*/
        
        for(int i = 0; i < rowNum*columnNum; i++){
        
            for(PieceModel p:iconArray){
                
                if(p.getId() == i)
                    p.setPath(newPaths[i]);
            
            }
        
        }
    
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
        
        int[] vecinos = this.vecinos(pos);
        int random = 0;
        
        do{
        
            random = (int) Math.floor(Math.random()*4);
            
        }while(vecinos[random] == -1 || vecinos[random] == lastPos);
        
        int[] resultado = {pos,vecinos[random]};
        
        return resultado;
        
    }

    public int getBlancaAnterior() {
        return blancaAnterior;
    }

    private int[] vecinos(int pos){
    
        int[] vecinos = new int[4];
        vecinos[0] = iconArray.get(pos).getJ() < columnNum-1?iconArray.get(pos).getI()*columnNum+iconArray.get(pos).getJ() + 1:-1;
        vecinos[1] = iconArray.get(pos).getJ() > 0?iconArray.get(pos).getI()*columnNum+iconArray.get(pos).getJ() - 1:-1;
        vecinos[2] = iconArray.get(pos).getI() < rowNum-1?iconArray.get(pos).getI()*columnNum+iconArray.get(pos).getJ() + columnNum:-1;
        vecinos[3] = iconArray.get(pos).getI() > 0?iconArray.get(pos).getI()*columnNum+iconArray.get(pos).getJ() - columnNum:-1;
    
        return vecinos;
    }
    @Override
    public void update(int blankPos, int movedPos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        blancaAnterior = blankPos;
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
