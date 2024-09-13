package unibo.javafxmvc.model.esercizio;

public class Somma {
    public static void main(String[] args) {
        int a = 5;
        int b = 3;

        int somma = calcolaSomma(a, b);
        System.out.println("Correct Sum: " + somma);
    }
    public static int calcolaSomma(int x, int y) {
        return x + y;
    }
    public static int calcolaSommaSB(int x, int y) {
        return x - y;
    }
}