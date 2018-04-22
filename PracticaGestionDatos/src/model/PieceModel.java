package model;

public class PieceModel {
    
    private int id;
    private int i;
    private int j;
    
    public PieceModel(int identificador, int fila, int columna){
        id = identificador;
        i = fila;
        j = columna;
    }
    
    public int getId() {
        return id;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

}
