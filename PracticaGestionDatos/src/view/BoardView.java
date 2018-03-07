package view;

import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Clase que representa la vista del tablero
 * @author Miguel Ángel
 * @version 1.0
 */
public class BoardView extends JPanel implements Observer {
    
    public static final int imageWidth= 96;
    public static final int imageHeight= 96;
    private ArrayList<PieceView> iconArray = null;
    
    //Nuevas variables
    public int anchoImagen;
    public int altoImagen;
    
    public int columnas;
    public int filas;

    private int piezaBlanca = 0;

    public BoardView(int rowNum, int columnNum,int imageSize, String[] imageList){
        super();
        
        iconArray = new ArrayList<PieceView>();
        
        filas = rowNum;
        columnas = columnNum;
        
        for(int i = 0; i < rowNum; i++){
            for(int j = 0; j < columnNum; j++){
                iconArray.add(new PieceView(i*columnNum + j, i, j, imageSize, imageList[i*columnNum + j]));
            } 
        } 
        
        anchoImagen = iconArray.get(0).getIconWidth();
        altoImagen = iconArray.get(0).getIconHeight();
        
        this.repaint();
    }

    public BoardView(int rowNum, int columnNum, int imageSize, File imageFile) throws IOException{
        
        super();
        iconArray = new ArrayList<>();
        
        filas = rowNum;
        columnas = columnNum;
        
        try{
            BufferedImage img = this.resizeImage(imageFile);
            BufferedImage[] imagenes = this.splitImage(img);

            for(int i = 0; i < rowNum; i++){
                for(int j = 0; j < columnNum; j++){
                    iconArray.add(new PieceView(i*columnNum + j, i, j, imageSize, imagenes[i*columnNum + j]));
                } 
            } 

            anchoImagen = iconArray.get(0).getIconWidth();
            altoImagen = iconArray.get(0).getIconHeight();
        
        }catch(IOException e){
        
            System.out.println("No se pudo establecer boardview: " + e.getMessage());
        
        }
    }

    //redimensionamos la imagen para 96*96
    private BufferedImage resizeImage(File fileImage) throws IOException{
        
        BufferedImage resizedImage = null;
        BufferedImage bufim = null;
        try{

            resizedImage = ImageIO.read(new File(fileImage.getPath()));
            //resizedImage = new BufferedImage(imageWidth,imageHeight,resizedImage.getType());
            
            bufim = new BufferedImage(imageWidth,imageHeight,resizedImage.getType());
            Graphics2D g = bufim.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(resizedImage, 0, 0, imageWidth, imageHeight, 0, 0, resizedImage.getWidth(), resizedImage.getHeight(), null);
            g.dispose();
        
            System.out.println("Imagen redimensionada");
            
        }catch(IOException e){
        
            System.out.println("No se puede cargar el archivo: " + e.getMessage());
        
        }    

        return bufim;
        
    }

    //dividimos la imagen en el número
    private BufferedImage[] splitImage(BufferedImage image) throws IOException{
        //Divisor de imágenes
        
        //COMO ACTUALIZAMOS LA IMAGEN UNA VEZ CARGADA. ACCEDER A BOARDVIEW Y A SUS METODOS PRIVADOS
        //NECESITAMOS DEVOLVER UN ARRAY DE PATHS Y DEVOLVEMOS ARRAY DE BUFFERED IMAGES
        int cuadrados = this.filas * this.columnas;

        int anchoCuadrado = image.getWidth() / this.columnas; // determines the chunk width and height
        int altoCuadrado = image.getHeight() / this.filas;
        int count = 0;
        BufferedImage images[] = new BufferedImage[cuadrados]; //Image array to hold image chunks
        for (int x = 0; x < this.filas; x++) {
            for (int y = 0; y < this.columnas; y++) {
                //Initialize the image array with image chunks
                images[count] = new BufferedImage(anchoCuadrado, altoCuadrado, image.getType());
                // draws the image chunk
                Graphics2D gr = images[count++].createGraphics();
                gr.drawImage(image, 0, 0, anchoCuadrado, altoCuadrado, anchoCuadrado * y, altoCuadrado * x, anchoCuadrado * y + anchoCuadrado, altoCuadrado * x + altoCuadrado, null);
                gr.dispose();
            }
     
        }

        System.out.println("Splitting done");
        
        try{
        
            for (int i = 0; i < images.length; i++) {
            
                ImageIO.write(images[i], "jpg", new File("resources/img" + i + ".jpg"));
            
            }
            
            System.out.println("Mini images created");
        
        }catch(IOException e){
        
            System.out.println("Fallo al dividir: " + e.getMessage());        
        
        }
        
        return(images);
        
    }

    public void update(int blankPos, int movedPos){
        PieceView p = iconArray.get(blankPos);
        PieceView p2 = iconArray.get(movedPos);
        
        int auxX = p.getIndexColumn();
        int auxY = p.getIndexRow();
        
        p.setIndexColumn(p2.getIndexColumn());
        p.setIndexRow(p2.getIndexRow());
        
        p2.setIndexColumn(auxX);
        p2.setIndexRow(auxY);
        
        iconArray.set(blankPos, p2);
        iconArray.set(movedPos, p);
        
        piezaBlanca = movedPos;
        
        this.repaint();
    }

    public void update(Graphics g){
        paint(g);
    }

    public void paint(Graphics g){
        super.paint(g);
        
        for(PieceView iconImage:iconArray){
            g.drawImage(iconImage.getImage(), iconImage.getIndexColumn() * iconImage.getIconWidth(), iconImage.getIndexRow() * iconImage.getIconHeight(), iconImage.getImageSize(), iconImage.getImageSize(), this);
        }
    }

    //Dado una posicion X e Y localizar una pieza
    private int locatePiece(int posX,int posY){
        return(posY * columnas + posX);
    }

    /**
     * Mueve la pieza y devuelve las coordenadas en un array de dos posiciones
     * donde: la primera posicion representa la posicion actual de la pieza blanca
     * y la segunda posicion representa la posicion actual de la pieza a mover.
     * @param posX posicion X del puntero
     * @param posY posicion Y del puntero.
     * @return Array de dos posiciones: posicion actual de la pieza blanca y posicion
     * actual de la pieza que tiene que ser movida.
     */

    public int[] movePiece(int posX,int posY){
        int x = posX / anchoImagen;
        int y = posY / altoImagen;
        
        int piezaId = locatePiece(x, y);
        
        System.out.println("ID a mover: " + piezaId);
        System.out.println("Pieza blanca: " + piezaBlanca);
        
        int distanciaX = Math.abs(x - iconArray.get(piezaBlanca).getIndexColumn());
        int distanciaY = Math.abs(y - iconArray.get(piezaBlanca).getIndexRow());
        
        if(distanciaX + distanciaY == 1){
            System.out.println("///Se mueve///");
            
            int[] resul = {piezaBlanca, piezaId};
            
            return(resul);
        } else{
            return null;
        }
    }

}