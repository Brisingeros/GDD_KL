package control;

import command.Commands;
import observer.Observable;
import observer.Observer;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

/**
 * Interfaz que tiene que ser implementada por un controlador.
 * @author Miguel Ángel
 * @version 1.0
 */
public abstract class AbstractController extends MouseAdapter implements ActionListener, Observable {
    protected ArrayList<Observer> observerList;
    protected ArrayList<Commands> commandList;

    public AbstractController(){
        observerList = new ArrayList<Observer>();
        commandList = new ArrayList<Commands>();
    }

    public void addObserver(Observer observer){
        if(observer!=null){
            observerList.add(observer);
        }
    }
    
    public void removeObserver(Observer observer){
        if(observer!=null){
            observerList.remove(observer);
        }
    }
    
    public void addCommand(Commands command){

        if(command != null)
            commandList.add(command);
    
    }
    
    public void removeCommand(Commands command){
    
        if(command != null)
            commandList.remove(command);
    
    }

}
