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
    
    public static int imageWidth= 96;
    public static int imageHeight= 96;
    private ArrayList<PieceView> iconArray = null;

    private int anchoImagen;
    private int altoImagen;
    
    private int columnas;
    private int filas;
    private int tamaño;
    private int piezaBlanca;
    private String[] paths;
    private String path;
    private BufferedImage[] imagenes;

    public BoardView(int rowNum, int columnNum,int imageSize, String[] imageList){
        super();
        
        iconArray = new ArrayList<PieceView>();
        imagenes = null;
        piezaBlanca = 0;
        paths = imageList;
        imageWidth = imageSize;
        imageHeight = imageSize;
        filas = rowNum;
        columnas = columnNum;
        tamaño = imageSize;
        for(int i = 0; i < rowNum; i++){
            
            for(int j = 0; j < columnNum; j++){
                
                iconArray.add(new PieceView(i*columnNum + j, i, j, imageSize, imageList[i*columnNum + j]));
                
            } 
            
        } 
        
        anchoImagen = iconArray.get(0).getIconWidth();
        altoImagen = iconArray.get(0).getIconHeight();
        
    }

    public BoardView(int rowNum, int columnNum, int imageSize, File imageFile) throws IOException{ //Constructor de boardview cuando le enviamos una imagen completa a dividir
        
        super();
        
        iconArray = new ArrayList<>();
        
        piezaBlanca = 0;
        
        path = imageFile.getPath();
        filas = rowNum;
        columnas = columnNum;
        
        imageWidth = imageSize;
        imageHeight = imageSize;
        paths = new String[rowNum*columnNum];
        tamaño = imageSize;
        try{
            
            BufferedImage img = this.resizeImage(imageFile);
            imagenes = this.splitImage(img);
            
            for(int i = 0; i < rowNum; i++){
                
                for(int j = 0; j < columnNum; j++){
                    
                    iconArray.add(new PieceView(i*columnNum + j, i, j, imagenes[i*columnNum + j].getHeight(), imagenes[i*columnNum + j]));
                    
                } 
                
            } 

            anchoImagen = iconArray.get(0).getIconWidth();
            altoImagen = iconArray.get(0).getIconHeight();

        }catch(IOException e){
        
            System.out.println("No se pudo establecer boardview: " + e.getMessage());
        
        }
        
    }

    private BufferedImage resizeImage(File fileImage) throws IOException{ //Damos a la imagen el tamaño deseado, obteniendo un bufferedImage a ser spliteado
        
        BufferedImage resizedImage = null;
        BufferedImage bufim = null;
        
        try{

            resizedImage = ImageIO.read(fileImage);
            //imageHeight = imageHeight * imageWidth/resizedImage.getWidth();
            bufim = new BufferedImage(imageWidth,imageHeight,resizedImage.getType());
            Graphics2D g = bufim.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(resizedImage, 0, 0, imageWidth, imageHeight, 0, 0, resizedImage.getWidth(), resizedImage.getHeight(), null);
            g.dispose();
            
        }catch(IOException e){
        
            System.out.println("No se puede cargar el archivo: " + e.getMessage());
        
        }    

        return bufim;
        
    }
    
    private BufferedImage resizeBlanca(File fileImage) throws IOException{
        
        BufferedImage resizedImage = null;
        BufferedImage bufim = null;
        
        try{

            resizedImage = ImageIO.read(new File(fileImage.getPath()));  
            bufim = new BufferedImage(imageWidth/columnas,imageHeight/filas,resizedImage.getType());
            Graphics2D g = bufim.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(resizedImage, 0, 0, imageWidth/columnas, imageHeight/filas, 0, 0, resizedImage.getWidth(), resizedImage.getHeight(), null);
            g.dispose();

        }catch(IOException e){
        
            System.out.println("No se puede cargar el archivo: " + e.getMessage());
        
        }    

        return bufim;
        
    }

    //dividimos la imagen en el número de piezas necesarias
    private BufferedImage[] splitImage(BufferedImage image) throws IOException{

        int cuadrados = this.filas * this.columnas;

        int anchoCuadrado = image.getWidth() / this.columnas;
        int altoCuadrado = image.getHeight() / this.filas;
        BufferedImage images[] = new BufferedImage[cuadrados]; 
        
        for (int x = 0; x < this.filas; x++) {
            
            for (int y = 0; y < this.columnas; y++) {

                images[x*columnas+y] = new BufferedImage(anchoCuadrado, altoCuadrado, image.getType());
                Graphics2D gr = images[x*columnas+y].createGraphics();
                gr.drawImage(image, 0, 0, anchoCuadrado-2, altoCuadrado-2, anchoCuadrado * y, altoCuadrado * x, anchoCuadrado * y + anchoCuadrado, altoCuadrado * x + altoCuadrado, null);
                gr.dispose();
                
            }
     
        }

        try{
        
            BufferedImage temp = this.resizeBlanca(new File("resources/blank.gif"));
            
            images[0] = temp;
            
            for (int i = 0; i < filas; i++) {
            
                for(int j = 0; j < columnas; j++){
                
                    ImageIO.write(images[i*columnas+j], "jpg", new File("resources/img" + (i*columnas+j) + ".jpg"));
                    paths[i*columnas+j] = "resources/img" + (i*columnas+j) + ".jpg";
                
                }
                
            }
        
        }catch(IOException e){
        
            System.out.println("Fallo al dividir: " + e.getMessage());        
        
        }
        
        return(images);
        
    }
    
    public void borrarImagenes(){

        for (int i = 0; i < filas; i++) {
            
            for(int j = 0; j < columnas; j++){
                
                File im = new File(paths[i*columnas+j]);
                im.delete();
                
            }
            
        }    
    
    }
    public void update(int blankPos, int movedPos){ //Cambiamos de posición los PieceView
        
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
            
            //g.drawImage(iconImage.getImage(), iconImage.getIndexColumn() * iconImage.getIconWidth(), iconImage.getIndexRow() * iconImage.getIconHeight(), iconImage.getImageSize(), iconImage.getImageSize(), this);
            g.drawImage(iconImage.getImage(), iconImage.getIndexColumn() * imageWidth/columnas, iconImage.getIndexRow() * imageHeight/filas, imageWidth/columnas, imageHeight/filas, this);
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
        
        int distanciaX = Math.abs(x - iconArray.get(piezaBlanca).getIndexColumn());
        int distanciaY = Math.abs(y - iconArray.get(piezaBlanca).getIndexRow());
        
        if(distanciaX + distanciaY == 1){
            
            int[] resul = {piezaBlanca, piezaId};
            
            return(resul);
            
        } else{
            
            return null;
            
        }
        
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public static int getImageWidth() {
        
        return imageWidth;
        
    }

    public static int getImageHeight() {
        
        return imageHeight;
        
    }

    public ArrayList<PieceView> getIconArray() {
        
        return iconArray;
        
    }

    public int getAnchoImagen() {
        
        return anchoImagen;
        
    }

    public int getAltoImagen() {
        
        return altoImagen;
        
    }

    public int getColumnas() {
        
        return columnas;
        
    }

    public int getFilas() {
        
        return filas;
        
    }

    public int getPiezaBlanca() {
        
        return piezaBlanca;
        
    }

    public BufferedImage[] getImagenes() {
        
        return imagenes;
        
    }

    
    public String[] getPathsPiezas(){

            return this.paths;

    }
    
    public String getPathImagenCompleta(){
    
        return this.path;
        
    }
    public static void setImageWidth(int imageWidth) {
        
        BoardView.imageWidth = imageWidth;
        
    }

    public static void setImageHeight(int imageHeight) {
        
        BoardView.imageHeight = imageHeight;
        
    }

    public void setIconArray(ArrayList<PieceView> iconArray) {
        
        this.iconArray = iconArray;
        
    }

    public void setAnchoImagen(int anchoImagen) {
        
        this.anchoImagen = anchoImagen;
        
    }

    public void setAltoImagen(int altoImagen) {
        
        this.altoImagen = altoImagen;
        
    }

    public void setColumnas(int columnas) {
        
        this.columnas = columnas;
        
    }

    public void setFilas(int filas) {
        
        this.filas = filas;
        
    }

    public void setPiezaBlanca(int piezaBlanca) {
        
        this.piezaBlanca = piezaBlanca;
        
    }

    public void setPaths(String[] paths) {
        
        this.paths = paths;
        
    }

    public void setImagenes(BufferedImage[] imagenes) {
        
        this.imagenes = imagenes;
        
    } 
    
}