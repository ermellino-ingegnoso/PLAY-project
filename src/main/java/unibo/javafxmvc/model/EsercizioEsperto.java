package unibo.javafxmvc.model;

import java.util.ArrayList;

public class EsercizioEsperto extends EsercizioGenerico{
    private Integer id;
    private User utente;
    private ArrayList<BloccoEsperto> blocchiEsperto;

    public EsercizioEsperto(Integer id, EsercizioGenerico eg, ArrayList<BloccoEsperto> blocchiEsperto, User utente) {
        super(eg);
        this.blocchiEsperto = blocchiEsperto;
        this.utente = utente;
    }
    /**
     * @param id ID dell'esercizio,<br> in caso di <b>null</b> è essenziale che l'ID venga inserito successivamente
     * */
    public EsercizioEsperto(Integer id, EsercizioGenerico eg , User utente) {
        super(eg);
        this.id = id;
        this.utente = utente;
    }
    public int getNblocchiNonSuperati(){
        int count = 0;
        for(BloccoEsperto be : blocchiEsperto){
            if(!be.isSuperato()) count++;
        }
        return count;
    }
    /**@return l'ID dell'esercizio esperto*/
    public Integer getId() {
        return id;
    }
    /**@param id l'ID dell'esercizio esperto*/
    public void setId(Integer id) {
        this.id = id;
    }
    public BloccoEsperto getBloccoEsperto(int i){
        return blocchiEsperto.get(i);
    }
    public ArrayList<BloccoEsperto> getBlocchiEsperto() {
        return blocchiEsperto;
    }
    public int getNblocchiEsperto() {
        return blocchiEsperto.size();
    }
    public void setBlocchiEsperto(ArrayList<BloccoEsperto> blocchiEsperto) {
        this.blocchiEsperto = blocchiEsperto;
    }
    public User getUtente() {
        return utente;
    }
    public void setUtente(User utente) {
        this.utente = utente;
    }
    public Punteggio getPunteggi(){
        return new Punteggio(getRegola().getGrado(), utente, getRegola().getTitolo(), getPunti());
    }
    public ArrayList<Integer> getPunti(){
        ArrayList<Integer> punti = new ArrayList<>();
        for(Integer i = 0; i < blocchiEsperto.size(); i++){
            punti.add(blocchiEsperto.get(i).isSuperato() ? (i+1) : 0);
        }
        return punti;
    }
}
