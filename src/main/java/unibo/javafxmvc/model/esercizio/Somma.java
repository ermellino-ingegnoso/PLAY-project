package unibo.javafxmvc.model.esercizio;

public class Somma {
    public static void main(String[] args) {
        int a = 5;
        int b = 3;

        // Correct method call
        int correctSum = calculateSum(a, b);
        System.out.println("Correct Sum: " + correctSum);
    }
    // Correct method
    public static int calculateSum(int x, int y) {
        return x + y;
    }
    // Method with semantic error
    public static int calculateSumWithError(int x, int y) {
        return x - y;  // This line is semantically incorrect
    }
}