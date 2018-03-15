package config;

import command.MovCommand;
import java.util.Stack;
import model.Modelo;

public class PartidaLD {
    
    private Modelo model;
    private Stack<MovCommand> deshacerMovs;
    
    public PartidaLD(Modelo m,Stack<MovCommand> deshacer){
        
        this.model = m;
        this.deshacerMovs = deshacer;
        
    }

    public Modelo getModelo() {
        
        return this.model;
        
    }

    public Stack<MovCommand> getDeshacerMovs() {
        
        return this.deshacerMovs;
        
    }

}
