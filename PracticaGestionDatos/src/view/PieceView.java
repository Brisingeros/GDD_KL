package view;
import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ángel
 * @version 1.0
 */
public class PieceView extends ImageIcon implements Cloneable{

    //id de la imagen
    private int id;
    //índice de fila
    private int indexRow;
    //índice de columna
    private int indexColumn;
    //Tamaño de la imagen
    private int imageSize;


    /**
     * Constructor de una clase
     * @param indexRow indice de fila
     * @param indexColumn indice de columna
     * @param imagePath ubicación de la imagen.
     */
    public PieceView(int id,int indexRow, int indexColumn,int imageSize,String imagePath){
        super(imagePath);
        
        this.id = id;
        this.indexRow = indexRow;
        this.indexColumn = indexColumn;
        this.imageSize = imageSize;
    }

    public PieceView(int id, int indexRow, int indexColumn,int imageSize,Image image){
        super(image);
        
        this.id = id;
        this.indexRow = indexRow;
        this.indexColumn = indexColumn;
        this.imageSize = imageSize;
        
    }


    public int getIndexRow() {
        return indexRow;
    }

    public int getIndexColumn() {
        return indexColumn;
    }
    
    public void setIndexRow(int x) {
        indexRow = x;
    }

    public void setIndexColumn(int x) {
        indexColumn = x;
    }


    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getId(){
        return this.id;
    }

    public String toString(){
        return("id:"+id);
    }

}
