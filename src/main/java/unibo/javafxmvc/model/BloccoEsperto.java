package unibo.javafxmvc.model;

public class BloccoEsperto {
    private Integer id;
    private BloccoGenerico bloccoGenerico;
    private boolean superato;
    private String codiceUtente;

    /**Costruttore parziale: <br>
     * Imposta <b>codiceUtente</b> come <code>String</code> vuota*/
    public BloccoEsperto(BloccoGenerico bloccoGenerico, boolean superato) {
        this.bloccoGenerico = bloccoGenerico;
        this.superato = superato;
        codiceUtente = "";
    }
    public BloccoEsperto(Integer id, BloccoGenerico bloccoGenerico, boolean superato, String codiceUtente) {
        this.id = id;
        this.bloccoGenerico = bloccoGenerico;
        this.superato = superato;
        this.codiceUtente = codiceUtente;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public String getCodiceUtente() {
        return codiceUtente;
    }
    public void setCodiceUtente(String codiceUtente) {
        this.codiceUtente = codiceUtente;
    }
    public BloccoGenerico getBloccoGenerico() {
        return bloccoGenerico;
    }
    public void setBloccoGenerico(BloccoGenerico bloccoGenerico) {
        this.bloccoGenerico = bloccoGenerico;
    }
    public boolean isSuperato() {
        return superato;
    }
    public void setSuperato(boolean superato) {
        this.superato = superato;
    }
}