package unibo.javafxmvc.model.esercizio;

import unibo.javafxmvc.util.Compiler;
import unibo.javafxmvc.util.PropertiesUtil;

import java.util.ArrayList;

public class User {
    private String nome;
    public String cognome;
    public String anni;

    public User(String nome, String cognome, String anni) {
        this.nome = nome;
        this.cognome = cognome;
        this.anni = anni;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public String getAnni() {
        return anni;
    }
    public void setAnni(String anni) {
        this.anni = anni;
    }
    public static User userConMenoAnni(ArrayList<User> lista) {
        User user = null;
        int min = Integer.MAX_VALUE;
        for (User u : lista) {
            int anni = Integer.parseInt(u.getAnni());
            if (anni < min) {
                min = anni;
                user = u;
            }
        }
        return user;
    }
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User("Mario", "Rossi", "25"));
        users.add(new User("Luca", "Bianchi", "30"));
        users.add(new User("Mario", "Rossi", "20"));
        System.out.println(userConMenoAnni(users) == users.get(2));
    }
}