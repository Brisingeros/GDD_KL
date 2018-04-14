/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import control.Controlador;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.PuzzleGUI;

/**
 *
 * @author Laura
 */
public class LoadCommand implements Command{

    Controlador control;
    
    public void execute() throws IOException {
    
        //Obtenemos la imagen del FileSelector y creamos el nuevo boardView

            File imagen = PuzzleGUI.getInstance().showFileSelector();
            
            if(imagen != null){
                
                control.emptyStacks();
                PuzzleGUI.getInstance().updateBoard(imagen);
                control.addView(PuzzleGUI.getInstance().getBoardView());
                control.Restart(); //Llamada a método de manejo de creación del nuevo modelo
                
            }
        
    }
    
    public LoadCommand(Controlador c){
        control = c;
    }

    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void redoCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
