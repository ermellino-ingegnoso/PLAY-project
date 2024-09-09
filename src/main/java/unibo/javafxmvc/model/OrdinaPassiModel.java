package unibo.javafxmvc.model;

import unibo.javafxmvc.controller.OrdinaPassiController;


import java.util.ArrayList;

public class OrdinaPassiModel {
    private ArrayList<String> foto;
    private int punteggio;
    private String soluzione;
    private String autore;  // TO DO admin

    public OrdinaPassiModel(ArrayList<String> foto, int punteggio, String soluzione, String autore){
        this.foto = foto;
        this.punteggio = punteggio;
        this.soluzione = soluzione;
        this.autore = autore;
    }
    public ArrayList<String> getFoto(){
        return foto;
    }
    public int getPunteggio(){
        return punteggio;
    }
    public String getSoluzione() {
        return soluzione;
    }
    public String gteAutore(){
        return autore;
    }

    public String toString(){
        return punteggio + " - " + soluzione + " - " + autore;
    }

}
