package config;

import command.MovCommand;
import java.util.Stack;
import model.Modelo;

public class PartidaLD {
    
    private Modelo model;
    private Stack<MovCommand> deshacerMovs;
    
    public PartidaLD(Modelo m,Stack<MovCommand> deshacer){
        model = m;
        deshacerMovs = deshacer;
    }

    public Modelo getModelo() {
        return model;
    }

    public Stack<MovCommand> getDeshacerMovs() {
        return deshacerMovs;
    }

}
