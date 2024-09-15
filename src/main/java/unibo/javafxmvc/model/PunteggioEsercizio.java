package unibo.javafxmvc.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class PunteggioEsercizio {
    private final StringProperty grado;
    private final ListProperty<Float> punteggi;
    private final FloatProperty totale;

    public PunteggioEsercizio(Grado grado, ObservableList<Float> punteggi) {
        this.grado = new SimpleStringProperty(grado.toString().toLowerCase()); // "Esercizio " +
        this.punteggi = new SimpleListProperty<>(punteggi);
        this.totale = new SimpleFloatProperty(punteggi.stream().reduce(0.0f, Float::sum));
    }
    public StringProperty gradoProperty() {
        return grado;
    }
    public ListProperty<Float> punteggiProperty() {
        return punteggi;
    }
    public FloatProperty totaleProperty() {
        return totale;
    }
}