package unibo.javafxmvc.model;

import unibo.javafxmvc.DAO.EsercizioEspertoDBM;
import unibo.javafxmvc.DAO.PunteggioDBM;
import unibo.javafxmvc.exception.ConnectionException;

import java.util.ArrayList;

public class Punteggio { // Il grado di svolgimento è dato dallo svolgimento degli esercizi ad essa
    // associati
    protected Integer id;
    protected Grado grado;
    protected User user;
    protected String titolo;
    protected ArrayList<Integer> punteggi;

    /**Costruttore parziale <p>Non recupera l'<b> id</b></p>*/
    public Punteggio(Grado grado, User user, String titolo, ArrayList<Integer> punteggi) {
        this.grado = grado;
        this.user = user;
        this.punteggi = punteggi;
        this.titolo = titolo;
    }
    /**Costruttore più completo*/
    public Punteggio(Integer id, Grado grado, User user, String titolo, ArrayList<Integer> punteggi) {
        this.id = id;
        this.grado = grado;
        this.user = user;
        this.punteggi = punteggi;
        this.titolo = titolo;
    }
    /** Crea automaticamente una <code>ArrayList</code> di punteggi vuota <p>Costruttore incompleto</p>*/
    public Punteggio(Grado grado, User user, String titolo) {
        this.grado = grado;
        this.user = user;
        this.titolo = titolo;
        this.punteggi = new ArrayList<>();
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void addPunteggio(Integer p) {
        punteggi.add(p);
    }
    /**@return il totale dei punti per l'esercizio relativo*/
    public Float getPunteggio() {
        Float punti = 0.0f;
        for (Integer p : punteggi) {
            if (p != null) punti += Float.valueOf(p)+((Float.valueOf(p)!=0)?getPeso():0.0f);
        }
        return punti;
    }
    public Float getPuntoPonderato(int index) {
        return Float.valueOf(Float.valueOf(punteggi.get(index))+((Float.valueOf(punteggi.get(index))!=0)?getPeso():0.0f));
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Grado getGrado() {
        return grado;
    }
    public void setGrado(Grado grado) {
        this.grado = grado;
    }
    public ArrayList<Integer> getPunteggi() {return punteggi;}
    public int getNPunti() {return punteggi.size();}
    public void addPunto(Integer punto){
        punteggi.add(punto);
    }
    public void setPunteggi(ArrayList<Integer> punteggi) {
        this.punteggi = punteggi;
    }
    public void setTitolo(String titolo){
        this.titolo = titolo;
    }
    public String getTitolo(){
        return titolo;
    }
    public Integer getPunto(int index){
        return punteggi.get(index);
    }
    public Float getPeso(){
        return ((float)grado.ordinal()+1.0f)/2.0f; // (((float)grado.ordinal()+1.0f)/2.0f);
    }
    /**Restituisce il <code>Punteggio</code> con il valore di punteggio più alto dalla lista di <code>Punteggio</code> fornita.
     * @param punteggi una lista di oggetti <code>Punteggio</code> da cui trovare il punteggio massimo
     * @return l'oggetto <code>Punteggio</code> con il valore di punteggio più alto, o <code>null</code> se la lista è vuota o nulla
     */
    public static Punteggio getMaxPunteggio(ArrayList<Punteggio> punteggi) {
        if (punteggi == null || punteggi.isEmpty()) return null;
        Punteggio maxPunteggio = null;
        float maxPunti = Float.MIN_VALUE;
        for (Punteggio p : punteggi) {
            float punti = p.getPunteggio();
            if (punti > maxPunti) {
                maxPunti = punti;
                maxPunteggio = p;
            }
        }
        return maxPunteggio;
    }
    public ArrayList<Float> getPuntiPonderati(){
        ArrayList<Float> puntiPonderati = new ArrayList<>();
        for (Integer p : punteggi) {
            puntiPonderati.add(Float.valueOf(p)+((Float.valueOf(p)!=0)?getPeso():0.0f));
        }
        return puntiPonderati;
    }
    /**Restituisce l'oggetto Punteggio con il valore di punteggio più alto per un dato utente e grado.
     * @param user l'utente per cui trovare il punteggio massimo
     * @param grado il grado per cui trovare il punteggio massimo
     * @return - l'oggetto Punteggio con il valore di punteggio più alto, o <br> -<code>null</code> se non ci sono punteggi
     */
    public static Punteggio getMaxPunteggioByUserAndGrado(User user, Grado grado) throws ConnectionException {
        if(grado.ordinal()<2) return getMaxPunteggio(PunteggioDBM.getPunteggiByUserGrado(user, grado));
        else {
            ArrayList<EsercizioEsperto> eserciziEsperto = EsercizioEspertoDBM.getEserciziEspertoByUserAndGrado(user, grado);
            ArrayList<Punteggio> punteggi = new ArrayList<>();
            for (EsercizioEsperto esercizio : eserciziEsperto) {
                punteggi.add(esercizio.getPunteggi());
            }
            return getMaxPunteggio(punteggi);
        }
    }
    public static Float getSafePunteggio(ArrayList<Punteggio> punteggi){
        Punteggio maxPunteggio = Punteggio.getMaxPunteggio(punteggi);
        return maxPunteggio != null ? maxPunteggio.getPunteggio() : 0.0f;
    }
    public static Float getMaxPunteggio(User user, Grado grado) throws ConnectionException {
        Punteggio punteggio = Punteggio.getMaxPunteggioByUserAndGrado(user, grado);
        return (punteggio != null) ? punteggio.getPunteggio() : 0.0f;
    }
}