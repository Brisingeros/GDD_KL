package view;

import control.AbstractController;

import javax.swing.*;
import java.awt.*;
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


    /**
     * Constructor privado
     */
    private PuzzleGUI(){
        super("GMD PuzzleGUI");
        boardView = new BoardView(rowNum,columnNum,imageSize,imageList);
        boardView.addMouseListener(controller);
        this.getContentPane().setLayout(new BorderLayout());
        this.setJMenuBar(createMenuBar());
        this.getContentPane().add(boardView, BorderLayout.CENTER);
        this.getContentPane().add(createSouthPanel(), BorderLayout.SOUTH);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1770, 1000);
        this.setLocation(centerFrame());
    }

    //Singleton
    public static PuzzleGUI getInstance(){
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

        JMenuItem load = new JMenuItem("Load");
        load.setActionCommand("load");
        JMenuItem exit = new JMenuItem("Exit");
        exit.setActionCommand("exit");
        JMenuItem info = new JMenuItem("Info");
        info.setActionCommand("info");

        archive.add(load);
        archive.add(exit);
        help.add(info);

        menu.add(archive);
        menu.add(help);

        load.addActionListener(controller);
        exit.addActionListener(controller);
        info.addActionListener(controller);

        return(menu);
    }

    //Centrar el frame en el centro de la pantalla.
    private Point centerFrame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xCoord = (screenSize.width - this.getWidth()) / 2;
        int yCoord = (screenSize.height - this.getHeight()) / 2;
        return(new Point(xCoord,yCoord));
    }

    public File showFileSelector(){
        
        File imagenSeleccionada = null;
        //Creamos un nuevo cuadro de diálogo para seleccionar imagen
        JFileChooser selector=new JFileChooser();
        //Le damos un título
        selector.setDialogTitle("Seleccione una imagen");
        //Filtramos los tipos de archivos
        FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("JPG & GIF & BMP & PNG", "jpg", "gif", "bmp","png");
        selector.setFileFilter(filtroImagen);
        //Abrimos el cuadro de diálog
        int flag=selector.showOpenDialog(null);
        //Comprobamos que pulse en aceptar
        if(flag==JFileChooser.APPROVE_OPTION){
            
            //Devuelve el fichero seleccionado
            imagenSeleccionada=selector.getSelectedFile();
            System.out.println("Imagen cargada");
            //Asignamos a la variable bmp la imagen leida
                  
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
        //controller.removeObserver();

        this.remove(boardView);

        //Seleccionar tamaños
        String opFilas = JOptionPane.showInputDialog("Num filas");
        String opColumnas = JOptionPane.showInputDialog("Num columnas");
        String opTamaño = JOptionPane.showInputDialog("Tamaño");

        rowNum = Integer.parseInt(opFilas);
        columnNum = Integer.parseInt(opColumnas);
        imageSize = Integer.parseInt(opTamaño);

        this.boardView = new BoardView(rowNum,columnNum,imageSize, imageFile);

        boardView.addMouseListener(controller);
        this.getContentPane().add(boardView, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();

        controller.addObserver(boardView);
        
    }


    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
