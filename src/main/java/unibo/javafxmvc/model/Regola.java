package unibo.javafxmvc.model;

public class Regola {
    private String titolo;
    private String domanda;
    private String descrizione;
    private Grado grado;

    public Regola(String titolo, String domanda, String descrizione, Grado grado) {
        this.titolo = titolo;
        this.domanda = domanda;
        this.descrizione = descrizione;
        this.grado = grado;
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
    /**@return <b>grado</b> come <code>String</code>*/
    public String getGradoName() {
        return grado.name();
    }
    /**@return <b>grado</b> come <code>Enum</code> di <code>Grado</code>*/
    public Grado getGrado() {
        return grado;
    }
    /*
    for (Grado grado : Grado.values()) {
            System.out.println(grado + " ordinal: " + grado.ordinal());
        }
    * */
}
