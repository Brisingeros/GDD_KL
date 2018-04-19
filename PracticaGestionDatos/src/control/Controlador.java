package control;

import command.LoadCommand;
import command.MovCommand;
import config.BaseXManager;
import config.Configuracion;
import config.PartidaXML;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Modelo;
import observer.Observer;
import org.basex.core.Context;
import view.BoardView;
import view.InfoView;
import view.PuzzleGUI;

public class Controlador extends AbstractController{
    
    private Stack<MovCommand> movsDes = new Stack<>(); //Atrás
    private Stack<MovCommand> movsRe = new Stack<>(); //Alante
    private Random aleatorio = new Random(System.currentTimeMillis());
    private int desordenes = 200;
    
    private Modelo model;
    private BoardView view;
    
    private BaseXManager manager; // = new BaseXManager();
    private Context contexto; // = new Context();
    
    private String base;
    
    public Controlador(String b){
        
        base = b;
        
        if(base.equals("XML")){
            manager = new BaseXManager();
            contexto = new Context();

            manager.createCollection("Pilas",contexto);

            emptyStacks();
        } else{
            
        }
        
    }

    public void gestorAcciones(String accion){
    
    switch (accion){
            
            case "exit": 
                
                view.borrarImagenes();
                System.exit(0);
            
            break;
            
            case "info": 
                
                try {
                    
                    InfoView info = new InfoView();
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
        
            break;
            
            case "load":
                
                LoadCommand loader = new LoadCommand(this);

                try {
                    
                    loader.execute();
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                    
                }

            break;
            
            case "clutter":

                if(base.equals("XML")){
                
                    manager.limpiarMovCommand(contexto, "rehacer");
                
                    for(int i = 0; i < desordenes; i++){
                
                        MovCommand move = desordenar();
                        if(move.getResul() != null){
                
                            move.redoCommand();
                            manager.addMovCommand(move, contexto, "movsdes");
                        
                        }

                    }
                    
                }
                
            break;
            
            case "solve":
                
                if(base.equals("XML")){
                
                    int[] posi = manager.tomarMovCommand(contexto, "movsdes");
                    
                    while(posi != null){
                    
                        ordenar(posi);
                        posi = manager.tomarMovCommand(contexto, "movsdes");
                    
                   }
                
                }
                
                
            break;
            
            case "deshacer":
                
                if(base.equals("XML")){
                
                    int[] posi = manager.tomarMovCommand(contexto, "movsdes");
                    
                    if(posi != null){

                        manager.addMovCommand(deshacer(posi), contexto, "rehacer");
                    
                   }
                
                }
                
                
            break;
            
            case "rehacer":
                
                if(base.equals("XML")){
                
                    int[] posi = manager.tomarMovCommand(contexto, "rehacer");
                    
                    if(posi != null){

                        manager.addMovCommand(rehacer(posi), contexto, "movsdes");
                    
                   }
                
                }
                
            break;
            
            case "guardaP0":
                try {
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), manager.guardarPartida(contexto, 0, view.getFilas(), view.getTamaño(), view.getPathImagenCompleta()));
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            break;
            
            case "guardaP1":
                
                try {
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), manager.guardarPartida(contexto, 1, view.getFilas(), view.getTamaño(), view.getPathImagenCompleta()));
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            break;
            case "guardaP2": 
                         
                try {
                    JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), manager.guardarPartida(contexto, 2, view.getFilas(), view.getTamaño(), view.getPathImagenCompleta()));
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
  
            break;
            case "cargaP0": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                    this.cargarPartidaXml(0);

            break;
            case "cargaP1": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                    this.cargarPartidaXml(1);
                
            break;
            case "cargaP2": //Leemos un objeto partida y llamamos al método que maneja la creación de nuevo MVC en base a los datos recibidos

                    this.cargarPartidaXml(2);

            break;
            
            default:
                
            break;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) { //Método que maneja los eventos
        
        this.gestorAcciones(e.getActionCommand());
        
    }


    @Override
    public void notifyObservers(int blankPos, int movedPos) {

        for(Observer o:observerList){
            
            o.update(blankPos, movedPos);
            
        }
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if(e.getX() <= BoardView.imageWidth && e.getY() <= BoardView.imageHeight){

            int[] mov = view.movePiece(e.getX(), e.getY());
            
            if(mov != null){
                
                MovCommand move = new MovCommand(this, view, mov);
                //Limpiar rehacer
                manager.limpiarMovCommand(contexto, "rehacer");
                
                move.redoCommand();
                
                //Añadir el movcommand al tipo movsdes
                manager.addMovCommand(move, contexto, "movsdes");
                
            }
            
        }
        
    }
    
    public MovCommand desordenar(){

        int[] mov = model.getRandomMovement(model.getBlancaAnterior(), view.getPiezaBlanca());
        MovCommand move = new MovCommand(this,view,mov);

        return move;
        
    }
    
    public void ordenar(int[] movimiento){
        
        MovCommand move = new MovCommand(this, view, movimiento);
        move.undoCommand();
        
    }
    
    public MovCommand deshacer(int[] posi){

        MovCommand move = new MovCommand(this, view, posi);
        move.undoCommand();
        
        return move;
        
    }
    
    public MovCommand rehacer(int[] posi){ //PASAMOS INT[] DE PARAMETRO
        

            MovCommand move = new MovCommand(this, view, posi);
            move.redoCommand();

            
        return move;

    }
    
    public void emptyStacks(){
        
        manager.limpiarMovCommand(contexto, "rehacer");
        manager.limpiarMovCommand(contexto, "movsdes");
        
    }
    
    public void addModelo(Modelo m){
        
        model = m;
        
    }
    
    public void addView(BoardView v){
        
        view = v;
        
    }
    
        
    private void cargarPartidaXml(int id){ //PASAMOS STRING DE PARAMETRO PARA EL JOPTIONPANE Y OBJETO DE PARTIDA
        try {
            String query = manager.cargarPartida(contexto, id);
            
            if(query != null){
            
                PartidaXML game = Configuracion.parseXML(query);
            
                removeObserver(model);

                PuzzleGUI.getInstance().initCarga(game.getFilas(), game.getTamaño(), game.getPath());

                view = PuzzleGUI.getInstance().getBoardView();

                model = new Modelo(game.getFilas(), game.getFilas(), game.getTamaño(), view.getPathsPiezas());

                this.addObserver(model);

                this.desordenInicio();
                
                JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "Partida cargada correctamente");
            
            }else
                
                JOptionPane.showMessageDialog(PuzzleGUI.getInstance().getContentPane(), "Error al cargar partida");
                
            
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void desordenInicio(){ //PASAMOS LISTA DE INT[] DE PARAMETRO
        
        //de aqui
        String aux1 = manager.recorridoInicio(contexto);
        
        String[] aux2 = aux1.split("\r\n");
        String[] aux3;
        int[] values;
        
        for(int i = 0; i < aux2.length; i++){
            aux3 = aux2[i].split(",");
            
            values = new int[aux3.length];
            for(int j = 0; j < values.length; j++){
                values[j] = Integer.parseInt(aux3[j]);
            }
            
        //a aqui al metodo de BaseXmanager de recorridoInicio
            MovCommand move = new MovCommand(this, view, values);
            move.redoCommand();
        }
    }
    
    public void Restart(){
        
        removeObserver(model);
        model = new Modelo(view.getFilas(), view.getColumnas(), view.getAltoImagen()*view.getColumnas(), view.getPathsPiezas());
        
        addObserver(model);
        addModelo(model);
        
        desordenes = view.getFilas()*view.getColumnas()*9;
        this.desordenar();
        
    }

    public Modelo getModel() {
        
        return model;
        
    }

    public void setModel(Modelo model) {
        
        this.model = model;
        
    }

    public BoardView getView() {
        
        return view;
        
    }

    public void setView(BoardView view) {
        
        this.view = view;
        
    }

    
    public Stack<MovCommand> getMovsDes() {
        
        return movsDes;
        
    }

    public Stack<MovCommand> getMovsRe() {
        
        return movsRe;
        
    }

    public void setMovsDes(Stack<MovCommand> movsDes) {
        
        this.movsDes = movsDes;
        
    }

    public void setMovsRe(Stack<MovCommand> movsRe) {
        
        this.movsRe = movsRe;
        
    }
    
    
    
}