package unibo.javafxmvc.exception;

public class ConnectionException extends Exception {
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
        System.out.println("Errore di connessione registrato");
    }
}