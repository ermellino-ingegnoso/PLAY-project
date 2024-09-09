package unibo.javafxmvc.model;

import java.util.ArrayList;

public class EsercizioGenerico {
    protected Integer id;
    protected User creatore;
    protected Regola regola;  // il Titolo dell'esercizio è in regola
    protected ArrayList<BloccoGenerico> blocchi;

    public EsercizioGenerico(Integer id, User creatore, Regola regola, ArrayList<BloccoGenerico> blocchi) {
        this.id = id;
        this.creatore = creatore;
        this.regola = regola;
        this.blocchi = blocchi;
    }
    /**Costruttore speciale: assicurarsi che nessuno dei campi sia vuoto*/
    public EsercizioGenerico(EsercizioGenerico eg){
        this.id = eg.getId();
        this.creatore = eg.getCreatore();
        this.regola = eg.getRegola();
        this.blocchi = eg.getBlocchi();
    }
    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getCreatore() {
        return creatore;
    }
    public int getNumeroBlocchiInseriti() {
        return blocchi.size();
    }
    public Regola getRegola() {
        return regola;
    }
    public ArrayList<BloccoGenerico> getBlocchi() {
        return blocchi;
    }
    public void setBlocchi(ArrayList<BloccoGenerico> blocchi) {
        this.blocchi = blocchi;
    }
    public void addBlocco(BloccoGenerico blocco) {
        blocchi.add(blocco);
    }
}
