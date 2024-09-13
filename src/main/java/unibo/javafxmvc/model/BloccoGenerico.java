package unibo.javafxmvc.model;
//  Si potrebbe pernsare ad una gestione interna degli ID e della loro caratteristica di non nullità
public class BloccoGenerico {
    private Integer id;
    private String consegna;
    private String codice;
    private String bloccoCorretto;

    public BloccoGenerico(int id, String consegna, String codice, String bloccoCorretto) {
        this.id = id;
        this.consegna = consegna;
        this.codice = codice;
        this.bloccoCorretto = bloccoCorretto;
    }
    /**Assicurarsi di inserire successivamente i valori di <b>id</b> e <b>esercizioGenericoEspertoId</b>*/
    public BloccoGenerico(String consegna, String codice, String metodoCorretto) {
        this.consegna = consegna;
        this.codice = codice;
        this.bloccoCorretto = metodoCorretto;
        id = null;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getConsegna() {
        return consegna;
    }
    public void setConsegna(String consegna) {
        this.consegna = consegna;
    }
    public String getCodice() {
        return codice;
    }
    public void setCodice(String codice) {
        this.codice = codice;
    }
    public String getMetodo() {
        return bloccoCorretto;
    }
    public void setMetodoCorretto(String bloccoCorretto) {
        this.bloccoCorretto = bloccoCorretto;
    }
}