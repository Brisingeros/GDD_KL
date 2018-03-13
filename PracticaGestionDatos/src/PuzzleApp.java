import config.Configuracion;
import config.info;
import control.AbstractController;
import control.Controlador;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import model.Modelo;
import view.BoardView;
import view.PuzzleGUI;
import static view.PuzzleGUI.controller;

/*
 * Copyright 2016 Miguel Ángel Rodríguez-García (miguel.rodriguez@urjc.es).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Clase principal que ejecuta el juego
 * @Author Miguel Ángel
 * @version 1.0
 */


public class PuzzleApp {

    public static void main(String args[]) throws JAXBException, IOException{
        info init = Configuracion.parse();
        //info init = null;
        int imageSize = 32;
        int rowNum = 3;
        int columnNum= 3;
        String path = null;
        
        if(init != null){
            imageSize = init.getImageSize();
            rowNum = init.getRows();
            columnNum= rowNum;
            path = init.getDefecto();
        }
        
        String fileSeparator = System.getProperty("file.separator");
        String imagePath=System.getProperty("user.dir")+fileSeparator+"resources"+fileSeparator;

        
        String[] imageList={imagePath+"blank.gif",imagePath+"one.gif",imagePath+"two.gif",imagePath+"three.gif",imagePath+ "four.gif",
                imagePath+"five.gif",imagePath+"six.gif",imagePath+"seven.gif",imagePath+"eight.gif"};
        
        //Creamos el controlador
        Controlador c  = new Controlador();
        
        if(path == null){
            // Inicializamos la GUI
            PuzzleGUI.initialize(c, rowNum, columnNum, imageSize, imageList);
        } else{
            // Inicializamos la GUI
            PuzzleGUI.initialize(c, rowNum, columnNum, imageSize, path);
        }
        
        // Obtenemos la vista del tablero
        BoardView v = PuzzleGUI.getInstance().getBoardView();
        
        // Creamos el modelo
        Modelo m = new Modelo(rowNum, columnNum, imageSize, v.getPaths());
        
        // Añadimos un nuevo observador al controlador
        c.addObserver(m);
        c.addObserver(v);
        c.addModelo(m);
        c.addView(v);
        // Visualizamos la aplicación.
        PuzzleGUI.getInstance().setVisible(true);
        
    }
}
