/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import java.io.File;
import java.util.Random;
import java.util.Stack;
import view.BoardView;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    
    public File cargarImagen(){
    
        File imagenSeleccionada = null;
        //Creamos un nuevo cuadro de diálogo para seleccionar imagen
        JFileChooser selector=new JFileChooser();
        //Le damos un título
        selector.setDialogTitle("Seleccione una imagen");
        //Filtramos los tipos de archivos
        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG & GIF & BMP", "jpg", "gif", "bmp");
        selector.setFileFilter(filtroImagen);
        //Abrimos el cuadro de diálog
        int flag=selector.showOpenDialog(null);
        //Comprobamos que pulse en aceptar
        if(flag==JFileChooser.APPROVE_OPTION){
            
            try {
                //Devuelve el fichero seleccionado
                imagenSeleccionada=selector.getSelectedFile();
                System.out.println("Imagen cargada");
                //Asignamos a la variable bmp la imagen leida

            } catch (Exception e) {
                
                System.out.println("No se cargó la imagen: " + e.getMessage());
            }
                  
        }else{
        
            System.out.println("Selección cancelada por usuario");
        
        }

        return imagenSeleccionada;
    
    }
}
