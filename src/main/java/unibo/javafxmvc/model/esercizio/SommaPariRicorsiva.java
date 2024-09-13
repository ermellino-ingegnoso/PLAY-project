package unibo.javafxmvc.model.esercizio;

public class SommaPariRicorsiva {
    public static void main(String[] args) {
        int[] numeri = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int somma = calcolaSommaPari(numeri, 0);
        System.out.println("Somma dei numeri pari: " + somma);
    }
    public static int calcolaSommaPari(int[] numeri, int index) {
        if (index >= numeri.length) return 0;

        int n = numeri[index];
        int sum = (n % 2 == 0) ? n : 0;
        return sum + calcolaSommaPari(numeri, index + 1);
    }
    public static int calculateEvenSumRecursiveWithError(int[] numeri, int index) {
        if (index >= numeri.length) return 0;

        int n = numeri[index];
        int sum = (n % 1 != 0) ? n : 0;  // Questo controllo è semanticamente errato
        return sum + calculateEvenSumRecursiveWithError(numeri, index + 1);
    }
}