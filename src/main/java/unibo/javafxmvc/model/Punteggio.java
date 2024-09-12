package unibo.javafxmvc.model;

import java.util.ArrayList;

public class Punteggio { // Il grado di svolgimento è dato dallo svolgimento degli esercizi ad essa
    // associati
    protected Grado grado;
    protected User user;
    protected String titolo;
    protected ArrayList<Integer> punteggi;

    public Punteggio(Grado grado, User user, String titolo, ArrayList<Integer> punteggi) {
        this.grado = grado;
        this.user = user;
        this.punteggi = punteggi;
        this.titolo = titolo;
    }
    /**
     * Crea automaticamente una <code>ArrayList</code> di punteggi vuota
     */
    public Punteggio(Grado grado, User user, String titolo) {
        this.grado = grado;
        this.user = user;
        this.titolo = titolo;
        this.punteggi = new ArrayList<>();
    }
    public void addPunteggio(Integer p) {
        punteggi.add(p);
    }
    /**
     * @return il totale dei punti per l'esercizio relativo
     * */
    public Integer getPunteggio() {
        Integer punti = 0;
        for (Integer p : punteggi) {
            if (p != null) punti += (grado.ordinal() + 1) / 2;
        }
        return (punti == 0) ? null : punti;
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
    public ArrayList<Integer> getPunteggi() {
        return punteggi;
    }
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
}
