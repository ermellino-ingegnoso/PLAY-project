package unibo.javafxmvc.model;

import javafx.beans.property.*;

public class UserScore {
    private final User user;
    private final StringProperty username;
    private final FloatProperty principiante;
    private final FloatProperty intermedio;
    private final FloatProperty avanzato;
    private final FloatProperty esperto;
    private final FloatProperty totale;

    public UserScore(User user, Float principiante, Float intermedio, Float avanzato, Float esperto, Float totale) {
        this.user = user;
        username = new SimpleStringProperty(user.getUsername());
        this.principiante = new SimpleFloatProperty(principiante);
        this.intermedio = new SimpleFloatProperty(intermedio);
        this.avanzato = new SimpleFloatProperty(avanzato);
        this.esperto = new SimpleFloatProperty(esperto);
        this.totale = new SimpleFloatProperty(totale);
    }
    public User getUser() {
        return user;
    }
    public FloatProperty principianteProperty() {
        return principiante;
    }
    public FloatProperty intermedioProperty() {
        return intermedio;
    }
    public FloatProperty avanzatoProperty() {
        return avanzato;
    }
    public FloatProperty espertoProperty() {
        return esperto;
    }
    public FloatProperty totaleProperty() {
        return totale;
    }
    public StringProperty usernameProperty() {return username;}
}