package unibo.javafxmvc.model;

import unibo.javafxmvc.Main;

import java.util.ArrayList;
import java.util.Arrays;

public class CommentiFactory {
    ArrayList<CommentiModel> esercizi;

    public CommentiFactory() {
        esercizi = new ArrayList<CommentiModel>();
        esercizi.add(new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova10.png", new ArrayList<String>(Arrays.asList("In queste righe di codice è presente le classe arraymaxvalore2 insieme ad uno scanner system.in per far in modo che vengano letti dei dati da tastiera. Inoltre sono presenti un system.out nel quale vi è scritto \"Inserire la lunghezza dell'array\" ed è dichiarata una variabile n di tipo int la quale verrà usata per DICHIARARE la lunghezza dell'array chiamato numeri di tipo int.", "In queste righe di codice è presente le classe arraymaxvalore2 insieme ad uno scanner system.in per far in modo che vengano letti dei dati da tastiera. Inoltre sono presenti un system.out nel quale vi è scritto \"Inserire la lunghezza dell'array\" ed è dichiarata una variabile n di tipo int la quale verrà usata per RIEMPIRE la lunghezza dell'array chiamato numeri di tipo int.","Nel codice è presente la classe `arraymaxvalore2` che utilizza un `BufferedReader` invece di uno `Scanner` per leggere i dati da un file esterno. Nel codice non è presente alcun `System.out`, e al suo posto c'è una finestra grafica che chiede all'utente: \"Inserire il valore massimo dell'array\". Inoltre, viene dichiarata una variabile `x` di tipo `double` che serve per calcolare l'area di un cerchio. L'array `numeri` è di tipo `String` e viene utilizzato per memorizzare delle parole.")), 0));
        esercizi.add(new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova11.png", new ArrayList<String>(Arrays.asList("In queste righe di codice c'è una variabile `t` di tipo `int` che è inizializzata con l'ultimo elemento dell'array chiamato `numeri`. Poi c'è un ciclo `while` che scorre tutti gli elementi dell'array incluso l'ultimo e sostituisce ogni elemento con quello precedente. Alla fine del ciclo, la variabile `t` viene inserita nella prima posizione dell'array `numeri` e infine l'array viene cancellato dal video.", "In queste righe di codice c'è una variabile `t` di tipo `int` che è inizializzata con l'ultimo elemento dell'array chiamato `numeri`. Poi c'è un ciclo `for` che scorre tutti gli elementi dell'array incluso l'ultimo e sostituisce ogni elemento con quello precedente. Alla fine del ciclo, la variabile `t` viene inserita nella prima posizione dell'array `numeri` e infine l'array viene stampato dal video.","In queste righe di codice è presente una variabile `t` di tipo `int`, inizializzata con il primo elemento dell'array `numeri`. Successivamente, un ciclo `for` scorre tutti gli elementi dell'array, ad eccezione dell'ultimo, sostituendo ciascun elemento con il successivo. Al termine del ciclo, il valore della variabile `t` viene inserito nell'ultimo elemento dell'array `numeri` e, infine, l'array viene stampato a video.")), 2));
        esercizi.add(new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova12.png", new ArrayList<String>(Arrays.asList("In queste righe di codice è presente una variabile n di tipo float e una matrice di tipo int chiamata board che ha dimensione n x n. I due cicli while annidati fanno sì che solo la diagonale principale della matrice venga iterata e ogni indice venga riempito con un numero casuale tra 1 e 9, senza mai stampare nulla.", "In queste righe di codice è presente una variabile n di tipo int ed anche una matrice di tipo char chiamata board che dimensione n. I due cicli for annidati fanno si che tutta la matrice venga iterata e in ogni indice viene riempito con un carattere o “X” oppure “O” e successivamente venga stampato.","In queste righe di codice c'è una variabile `n` di tipo `double` e una matrice di tipo `string` chiamata `board` che ha dimensione `n`. I due cicli `for` iterano solo la prima riga della matrice, riempiendola con i numeri da 1 a `n`, senza mai usare i caratteri “X” o “O” e senza stampare nulla alla fine.")), 1));
    }
    public void VediPunteggio(){
        Main.changeScene("View/Punteggi.fxml");
    }

    public CommentiModel getCommentiModel(int i) {
        if (i < 0 || i >= esercizi.size()) {
            return null;
            //VediPunteggio();
        } else {
            return esercizi.get(i);
        }
    }


    /*
    public void Serialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommentiModel esercizio = new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova1.png", new ArrayList<String>(Arrays.asList("gang1", "gang2","gang3")), "gang1");
        String jsonString = objectMapper.writeValueAsString(esercizio);
        System.out.println("Serialized JSON: " + jsonString);
    }
    public void Deserialize() throws IOException, IllegalArgumentException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL res = getClass().getResource("/unibo/javafxmvc/Exercises/Commenti.json");
        File f = new File(res.getFile());
        System.out.println(json);

        //String json = Files.readString(getClass().getResource("/unibo/javafxmvc/Exercises/Commenti.json"));
        // URL resource = getClass().getClassLoader().getResource("/unibo/javafxmvc/Exercises/Commenti.json");
        /*if (resource == null) {
            throw new IllegalArgumentException("ciaoneee");
        }
        String json = Files.readString(Paths.get(resource.getPath()));
         File file = new File("src/main/resources/unibo/javafxmvc/Exercises/Commenti.json");
        CommentiModel deserializedEsercizio = objectMapper.readValue(json, CommentiModel.class);
        System.out.println("Deserialized Object: " + deserializedEsercizio);
    }
    */
}
