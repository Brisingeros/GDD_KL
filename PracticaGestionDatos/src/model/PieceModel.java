package model;

public class PieceModel {
    
    private int id;
    private int i;
    private int j;
    
    private String path;
    
    public PieceModel(int identificador, int fila, int columna, String camino){
        id = identificador;
        i = fila;
        j = columna;
        path = camino;
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

    public String getPath() {
        return path;
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

    public void setPath(String path) {
        this.path = path;
    }

}
