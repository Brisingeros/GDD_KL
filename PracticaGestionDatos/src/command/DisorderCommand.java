/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import java.util.Random;
import java.util.Stack;
import view.BoardView;

/**
 *
 * @author Laura
 */
public class DisorderCommand implements Command {
    
    public BoardView tablero;
    public Random aleatorio = new Random(System.currentTimeMillis());
    public Stack<int[]> movs = new Stack();

    public DisorderCommand(BoardView t){
        tablero = t;
    }
    
    @Override
    public void undoCommand() {
        while(!movs.empty()){
            int[] movi = movs.pop();
            tablero.update(movi[1], movi[0]);
        }
    }

    @Override
    public void redoCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void execute() {
        for(int i = 0; i < 99; i++){
            int[] resul = tablero.movePiece(aleatorio.nextInt(97), aleatorio.nextInt(97));
            if(resul != null){
                movs.push(resul);
                tablero.update(resul[0], resul[1]);
            }
        }
    }
    
    public void setTablero(BoardView b){
        tablero = b;
    }
    
    public void addMov(int[] movi){
        movs.push(movi);
    }
    
}
