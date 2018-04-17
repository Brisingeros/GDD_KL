package view;

import control.AbstractController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Clase que representa la GUI principal.
 * @author Miguel Ángel
 * @version 1.0
 */
public class PuzzleGUI extends JFrame{

    //Instancia singleton
    public static PuzzleGUI instance = null;
    //Controlador
    public static AbstractController controller;
    //Número de filas
    public static int rowNum=0;
    //Número de columnas
    public static int columnNum =0;
    //Tamaño de imagen
    public static int imageSize =0;
    //Array de imagenes
    public static String[] imageList = null;
    //Panel de juego
    private BoardView boardView;
    
    //Nueva variable
    public static String imagePath = null;

    //Tamaño de pantalla
    private static int ancho;
    private static int alto;
    /**
     * Constructor privado
     */
    private PuzzleGUI() throws IOException{
        super("GMD PuzzleGUI");
        if(imagePath == null){
            boardView = new BoardView(rowNum,columnNum,imageSize,imageList);
            this.setBounds((ancho / 2) - (this.getWidth() / 2), (alto / 2) - (this.getHeight() / 2), 500,250);
        } else{
            boardView = new BoardView(rowNum,columnNum,imageSize,new File(imagePath));
            this.setBounds((ancho / 2) - (this.getWidth() / 2), (alto / 2) - (this.getHeight() / 2), 530,600);
        }
        boardView.addMouseListener(controller);
        
        this.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                boardView.borrarImagenes();
            }
            
        });
        this.getContentPane().setLayout(new BorderLayout());
        this.setJMenuBar(createMenuBar());
        this.getContentPane().add(boardView, BorderLayout.CENTER);
        this.getContentPane().add(createSouthPanel(), BorderLayout.SOUTH);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        //COGEMOS ANCHO Y ALTO DE LA PANTALLA
        ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        
        this.setLocation(centerFrame());
        
    }

    //Singleton
    public static PuzzleGUI getInstance() throws IOException{
        if(instance==null){
            instance = new PuzzleGUI();
        }
        return(instance);
    }
    
    public static void initialize(AbstractController controller, int rowNum,int columnNum,int imageSize,String[] imageList){
        PuzzleGUI.controller = controller;
        PuzzleGUI.rowNum = rowNum;
        PuzzleGUI.columnNum = columnNum;
        PuzzleGUI.imageSize = imageSize;
        PuzzleGUI.imageList = imageList;
        PuzzleGUI.imagePath = null;
        
    }
    
    public static void initialize(AbstractController controller, int rowNum,int columnNum,int imageSize,String imagePath){
        
        PuzzleGUI.controller = controller;
        PuzzleGUI.rowNum = rowNum;
        PuzzleGUI.columnNum = columnNum;
        PuzzleGUI.imageSize = imageSize;
        PuzzleGUI.imageList = null;
        PuzzleGUI.imagePath = imagePath;
        
    }

    //Método que crea el panel inferior
    private JPanel createSouthPanel(){
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton clutterButton = new JButton("Desordenar");//botón de desordenar
        clutterButton.setActionCommand("clutter");
        JButton solveButton = new JButton("Resolver");
        solveButton.setActionCommand("solve");
        
        //Nuevos Botones
        JButton deshacerButton = new JButton("Deshacer");
        deshacerButton.setActionCommand("deshacer");
        JButton rehacerButton = new JButton("Rehacer");
        rehacerButton.setActionCommand("rehacer");

        clutterButton.addActionListener(controller);
        solveButton.addActionListener(controller);
        
        //Nuevos botones
        deshacerButton.addActionListener(controller);
        rehacerButton.addActionListener(controller);


        southPanel.add(clutterButton);
        southPanel.add(solveButton);
        
        //Nuevos botones
        southPanel.add(deshacerButton);
        southPanel.add(rehacerButton);

        return(southPanel);
    }

    //Método que genera la barra de menus
    private JMenuBar createMenuBar(){
        JMenuBar menu = new JMenuBar();
        JMenu archive = new JMenu("Archive");
        JMenu help = new JMenu("Help");
        JMenu partida = new JMenu("Partida");

        JMenuItem load = new JMenuItem("Load");
        load.setActionCommand("load");
        JMenuItem exit = new JMenuItem("Exit");
        exit.setActionCommand("exit");
        JMenuItem info = new JMenuItem("Info");
        info.setActionCommand("info");
        
        JMenu cargar = new JMenu("Cargar");
        JMenuItem p0 = new JMenuItem("Partida 0");
        JMenuItem p1 = new JMenuItem("Partida 1");
        JMenuItem p2 = new JMenuItem("Partida 2");
        
        p0.setActionCommand("cargaP0");
        p1.setActionCommand("cargaP1");
        p2.setActionCommand("cargaP2");
        //cargar.setActionCommand("cargar");
        JMenu guardar = new JMenu("Guardar");
        JMenuItem p00 = new JMenuItem("Partida 0");
        JMenuItem p11 = new JMenuItem("Partida 1");
        JMenuItem p22 = new JMenuItem("Partida 2");
        
        p00.setActionCommand("guardaP0");
        p11.setActionCommand("guardaP1");
        p22.setActionCommand("guardaP2");
        //guardar.setActionCommand("guardar");

        archive.add(load);
        archive.add(exit);
        help.add(info);

        menu.add(archive);
        menu.add(help);
        menu.add(partida);
        
        cargar.add(p0);
        cargar.add(p1);
        cargar.add(p2);
        partida.add(cargar);
        
        guardar.add(p00);
        guardar.add(p11);
        guardar.add(p22);
        partida.add(guardar);
        
        load.addActionListener(controller);
        exit.addActionListener(controller);
        info.addActionListener(controller);
        //cargar.addActionListener(controller);
        p0.addActionListener(controller);
        p1.addActionListener(controller);
        p2.addActionListener(controller);
        p00.addActionListener(controller);
        p11.addActionListener(controller);
        p22.addActionListener(controller);
        //guardar.addActionListener(controller);

        return(menu);
    }

    //Centrar el frame en el centro de la pantalla.
    private Point centerFrame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xCoord = (screenSize.width - this.getWidth()) / 2;
        int yCoord = (screenSize.height - this.getHeight()) / 2;
        return(new Point(xCoord,yCoord));
    }

    public File showFileSelector(){ //Creamos el panel de selección y leemos la imagen deseada en caso de ser posible
        
        File imagenSeleccionada = null;
        JFileChooser selector=new JFileChooser();
        selector.setDialogTitle("Seleccione una imagen");
        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG & GIF & BMP & PNG", "jpg", "gif", "bmp","png");
        selector.setFileFilter(filtroImagen);
        int flag=selector.showOpenDialog(null);
        
        if(flag==JFileChooser.APPROVE_OPTION){

            imagenSeleccionada=selector.getSelectedFile();
                  
        }else{
        
            System.out.println("Selección cancelada por usuario");
        
        }
        
        return imagenSeleccionada;
        
    }

    public BoardView getBoardView(){
        
        return(this.boardView);
        
    }

    //Método para actualizar la imagen del tablero
    public void updateBoard(File imageFile) throws IOException{
        
        controller.removeObserver(boardView);
        this.remove(boardView);

        int piezas = 0; 
        
        //Seleccionar tamaños
        do{
            String opPiezas = JOptionPane.showInputDialog("Número de piezas (Minimo 9)");
            piezas = Integer.parseInt(opPiezas);
        }while(piezas < 9);
        
        do{
            String opTamaño = JOptionPane.showInputDialog("Tamaño deseado de la imagen completa (Mínimo 206)");
            imageSize = Integer.parseInt(opTamaño);
        }while(imageSize <= 206);
        
        rowNum = (int) Math.sqrt(piezas);
        columnNum = rowNum;        

        this.boardView = new BoardView(rowNum,columnNum,imageSize, imageFile);

        boardView.addMouseListener(controller);
        this.getContentPane().add(boardView, BorderLayout.CENTER);

        this.revalidate();
        this.setBounds((ancho / 2) - (this.getWidth() / 2), (alto / 2) - (this.getHeight() / 2),(int) (BoardView.getImageWidth() * 1.03), (int) (BoardView.getImageHeight() * 1.03) + 85);
        this.repaint();

        controller.addObserver(boardView);
        
    }
    
    //UpdateBoard para cuando cargamos partida y no cuando cargamos una nueva imagen
    public void initCarga(int rows, int tamañoImagen, String path) throws IOException{
        controller.removeObserver(boardView);
        this.remove(boardView);

        rowNum = rows;
        columnNum = rowNum;
        imageSize = tamañoImagen;

        this.boardView = new BoardView(rowNum,columnNum,imageSize, new File(path));

        boardView.addMouseListener(controller);
        this.getContentPane().add(boardView, BorderLayout.CENTER);

        this.revalidate();
        this.setBounds((ancho / 2) - (this.getWidth() / 2), (alto / 2) - (this.getHeight() / 2),(int) (BoardView.getImageWidth() * 1.03), (int) (BoardView.getImageHeight() * 1.03) + 85);
        this.repaint();

        controller.addObserver(boardView);
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    
}
