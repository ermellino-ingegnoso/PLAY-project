package unibo.javafxmvc.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CommentiModel {
    private String foto;
    private ArrayList<String> opzioni;
    private int soluzione;

    public CommentiModel(String foto, ArrayList<String> opzioni, int soluzioneIndex) {
        this.foto = foto;
        this.opzioni = opzioni;
        soluzione = soluzioneIndex;
    }
    public String getPercorsoFoto() {
        return foto;
    }
    public ArrayList<String> getOpzioni() {
        return opzioni;
    }
    public int getSoluzione() {
        return soluzione;
    }
    public Boolean check(String soluzioneProposta){
        return (soluzioneProposta.equals(opzioni.get(soluzione)));
    }
}
