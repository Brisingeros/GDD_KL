package control;

import command.LoadCommand;
import command.MovCommand;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Modelo;
import observer.Observer;
import view.BoardView;
import view.PuzzleGUI;


public class Controlador extends AbstractController{
    
    public Stack<MovCommand> movsDes = new Stack<>(); //Atrás
    public Stack<MovCommand> movsRe = new Stack<>(); //Alante
    public Random aleatorio = new Random(System.currentTimeMillis());
    
    public Modelo model;
    public BoardView view;
    
    public int desordenes = 18;
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getActionCommand());
        
        switch (e.getActionCommand()){
            case "load":
                LoadCommand loader = new LoadCommand(this);
                System.out.println("Cargar");
                loader.redoCommand();
                
                addView(PuzzleGUI.getInstance().getBoardView());
                
                Restart();
            break;
            case "clutter":
                desordenar();
            break;
            
            case "solve":
                try{
                    ordenar();
                } catch(Exception y){
                    System.out.println("Vacío");
                }
            break;
            
            case "deshacer":
                try{
                    deshacer();
                } catch(Exception y){
                    System.out.println("Vacío");
                }
            break;
            
            case "rehacer":
                try{
                    rehacer();
                } catch(Exception y){
                    System.out.println("Vacío");
                }
            break;
            
            default:
            break;
        }
    }

    @Override
    public void notifyObservers(int blankPos, int movedPos) {

        for(Observer o:observerList){
            o.update(blankPos, movedPos);
        }
        
    }
    
    public void mouseClicked(MouseEvent e) {
        if(e.getX() <= view.imageWidth && e.getY() <= view.imageHeight){
            MovCommand move = new MovCommand(this, view, e.getX(), e.getY());
            move.redoCommand();
            movsDes.push(move);
        }
    }
    
    public void desordenar(){
        
        for(int i = 0; i < desordenes; i++){
            MovCommand  move;
            do{
                int[] posi = {aleatorio.nextInt(view.imageWidth),aleatorio.nextInt(view.imageHeight)};
                move = new MovCommand(this, view, posi[0], posi[1]);
                
            }while(move.getResul() == null);
            
            if(!movsDes.empty()){
                MovCommand prev = movsDes.peek();

                if(!move.compareCommand(prev.getResul())){
                    move.redoCommand();
                    movsDes.push(move);
                }
                
            } else{
                move.redoCommand();
                movsDes.push(move);
            }
        }
    }
    
    public void ordenar(){
        while(!movsDes.empty()){
            MovCommand move = movsDes.pop();
            move.undoCommand();
            movsRe.push(move);
        }
    }
    
    public void deshacer(){
        MovCommand move = movsDes.pop();
        move.undoCommand();
        movsRe.push(move);
    }
    
    public void rehacer(){
        MovCommand move = movsRe.pop();
        move.redoCommand();
        movsDes.push(move);
    }
    
    public void emptyStacks(){
        movsRe = new Stack<MovCommand>();
        movsDes = new Stack<MovCommand>();
    }
    
    public void addModelo(Modelo m){
        model = m;
    }
    
    public void addView(BoardView v){
        view = v;
    }
    
    public void Restart(){
        removeObserver(model);
        
        BufferedImage[] im = view.imagenes;
        
        String[] imagePaths = new String[im.length];
        
        for(int i = 0; i < im.length; i++){
            imagePaths[i] = "resources/img" + i + ".jpg";
        }
        
        model = new Modelo(view.filas, view.columnas, view.altoImagen, imagePaths);
        
        addObserver(model);
        addModelo(model);
        
        desordenes = view.filas*view.columnas*2;
    }
    
}
