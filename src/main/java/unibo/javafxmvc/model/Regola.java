package unibo.javafxmvc.model;

public class Regola {
    private String titolo;
    private String domanda;
    private String descrizione;

    public Regola(String titolo, String domanda, String descrizione) {
        this.titolo = titolo;
        this.domanda = domanda;
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public String getDomanda() {
        return domanda;
    }
    public String getTitolo() {
        return titolo;
    }
}
