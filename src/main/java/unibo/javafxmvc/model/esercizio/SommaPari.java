package unibo.javafxmvc.model.esercizio;

public class SommaPari {
    public static void main(String[] args) {
        int[] numeri = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int somma = calcolaSommaPari(numeri);
        System.out.println("Somma dei numeri pari: " + somma);
    }

    // Metodo corretto
    public static int calcolaSommaPari(int[] numeri) {
        int somma = 0;
        for (int n : numeri) {
            if (n % 2 == 0) {
                somma += n;
            }
        }
        return somma;
    }
    // Metodo con errore semantico
    public static int calculateEvenSumWithError(int[] numeri) {
        int somma = 0;
        for (int n : numeri) {
            if (n % 2 != 0) {  // Questo controllo è semanticamente errato
                somma += n;
            }
        }
        return somma;
    }
}