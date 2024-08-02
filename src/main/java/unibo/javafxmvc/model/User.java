package unibo.javafxmvc.model;

import java.util.Objects;

public class User {
    private String nome;
    private String cognome;
    private String username;
    private String password; // memorizzata come SHA-256 hash

    public User(String nome, String cognome, String username, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
    }

    // equals() e hashCode() sono necessari per la comparazione degli oggetti (User)
    /** @return true <=> nome && cognome && username (!password) */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(nome, user.nome) &&
                Objects.equals(cognome, user.cognome) &&
                Objects.equals(username, user.username);
    }

    // affinchè Objects.equals() funzioni correttamente (funzionerebbe senza ma in
    // previsione di attributi complessi viene overridato)
    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, username, password);
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean checkPassword(String input) {
        return this.password.equals(input);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s%n", nome, cognome, username, password);
    }
}
