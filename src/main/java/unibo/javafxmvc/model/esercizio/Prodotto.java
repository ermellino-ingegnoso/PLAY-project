package unibo.javafxmvc.model.esercizio;

import java.util.ArrayList;

public class Prodotto {
    private String nome;
    private double prezzo;
    private int quantita;

    public Prodotto(String nome, double prezzo, int quantita) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public static Prodotto prodottoPiuCaro(ArrayList<Prodotto> lista) {
        Prodotto prodotto = null;
        double max = Double.MIN_VALUE;
        for (Prodotto p : lista) {
            if (p.getPrezzo() > max) {
                max = p.getPrezzo();
                prodotto = p;
            }
        }
        return prodotto;
    }

    public static void main(String[] args) {
        ArrayList<Prodotto> products = new ArrayList<Prodotto>();
        products.add(new Prodotto("Laptop", 1000.0, 5));
        products.add(new Prodotto("Smartphone", 800.0, 10));
        Prodotto prodotto = prodottoPiuCaro(products);
        if (prodotto == products.get(0))
            System.out.println(true);
        else
            System.out.println(false);
        System.out.println(prodotto.getNome() + " - " + prodotto.getPrezzo());
    }
}
