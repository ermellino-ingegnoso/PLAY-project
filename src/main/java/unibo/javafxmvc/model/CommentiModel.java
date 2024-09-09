package unibo.javafxmvc.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CommentiModel {
    private String foto;
    private ArrayList<String> opzioni;
    private String soluzione;

    public CommentiModel(String foto, ArrayList<String> opzioni, String soluzione) {
        this.foto = foto;
        this.opzioni = opzioni;
        this.soluzione = soluzione;
    }


    public String getPercorsoFoto() {
        return foto;
    }

    public ArrayList<String> getOpzioni() {
        return opzioni;
    }

    public String getSoluzione(){
        return soluzione;
    }
}
