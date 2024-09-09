package unibo.javafxmvc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

/**Classe di utilità per trovare le firme dei metodi e i loro corpi in un dato contenuto di classe.
 */
public class SignatureFinder {
    /**
     * Estrae il contenuto della classe senza il blocco main.
     * @param classContent il contenuto della classe
     * @return <b>classContent</b> senza il blocco <code>main</code>
     */
    public static String extractClassWithoutMain(String classContent) {
        Pattern mainPattern = Pattern.compile("public\\s+static\\s+void\\s+main\\s*\\([^)]*\\)\\s*\\{");
        Matcher matcher = mainPattern.matcher(classContent);
        if (matcher.find()) {
            int mainStart = matcher.start();
            int mainEnd = findMethodEnd(classContent, matcher.end());
            return classContent.substring(0, mainStart) + classContent.substring(mainEnd);
        }
        return classContent;
    }
    /**
     * @param classContent il contenuto della classe
     * @return - il nome della classe;<br>-<code>null</code> se non è presente alcun nome di classe;
     * */
    public static String extractClassName(String classContent) {
        Matcher matcher = Pattern.compile("class\\s+(\\w+)").matcher(classContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    /**
     * Estrae il nome del metodo dalla sua firma.
     * @param methodSignature La firma del metodo
     * @return - Il nome del metodo o <br> - <code>null</code> se il nome del metodo non è trovato
     */
    public static String extractMethodName(String methodSignature) {
        // Regex per trovare il nome del metodo
        Pattern pattern = Pattern.compile("\\b(\\w+)\\s*\\(");
        Matcher matcher = pattern.matcher(methodSignature);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null; // Restituisce null se il nome del metodo non è trovato
    }
    /**Trova tutti i metodi (firme e corpi) nel contenuto della classe fornito.
     * @param classContent il contenuto della classe
     * @return una lista di metodi (firme e corpi) trovati nel contenuto della classe
     */
    public static List<String> findAllMethods(String classContent) {
        List<String> methods = new ArrayList<>();
        Pattern pattern = Pattern.compile("(public|protected|private)\\s+(static\\s+)?[\\w<>\\[\\]]+\\s+\\w+\\s*\\([^)]*\\)\\s*\\{");
        Matcher matcher = pattern.matcher(classContent);
        while (matcher.find()) methods.add(classContent.substring(matcher.start(), findMethodEnd(classContent, matcher.end())));// matcher.end() -->    Restituisce l'indice dell'ultimo carattere della corrispondenza trovata
        return methods;
    }
    /**Trova l'indice di fine del corpo di un metodo nel contenuto fornito a partire dall'indice specificato.
     * @param content il contenuto della classe
     * @param startIndex l'indice di partenza per cercare la fine del metodo
     * @return l'indice di fine del corpo del metodo
     * @see #findAllMethods(String)
     */
    private static int findMethodEnd(String content, int startIndex) {
        int openBraces = 1;
        for (int i = startIndex; i < content.length(); i++) {
            if (content.charAt(i) == '{')  openBraces++;
            else if (content.charAt(i) == '}') {
                openBraces--;
                if (openBraces == 0) return i + 1;
            }
        }
        return content.length();
    }
    /**Trova tutte le firme dei metodi senza corpo (o con corpo interamente commentato con <code>/ * commento su più righe * /</code> ) nel contenuto della classe fornito.
     * @param classContent il contenuto della classe
     * @return una lista di firme di metodi senza corpo trovate nel contenuto della classe
     */
    public static List<String> findEmptyMethodSignatures(String classContent) {
        List<String> emptyMethods = new ArrayList<>();
    //                                           modificatori di accesso | spazi | static | tipo di ritorno | spazi | nome del metodo | parametri | corpo del metodo
        Pattern pattern = Pattern.compile("(public|protected|private)\\s+(static\\s+)?[\\w<>\\[\\]]+\\s+\\w+\\s*\\([^)]*\\)\\s*\\{\\s*(//.*|/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/\\s*)*\\}");
        Matcher matcher = pattern.matcher(classContent);
        while (matcher.find()) {
            emptyMethods.add(matcher.group());
        }
        return emptyMethods;
    }
    /** Estrae la firma del metodo da un metodo fornito senza <code>{ contenuto eventuale del metodo }</code>.
     * @param method il metodo da cui estrarre la firma
     * @return la firma del metodo
     */
    public static String extractSignature(String method) {
        int braceIndex = method.indexOf('{');
        if (braceIndex > 0) return method.substring(0, braceIndex).trim();
        return method.trim();
    }
    /**
     * @param method la firma del metodo da convertire in regex
     * @return la regex per la firma del metodo fornita
     * @see #extractSignature(String)
     */
    public static String getDynamicMethodRegex(String method) {
        return SignatureFinder.extractSignature(method).replace(" ", "\\s*").replace("(", "\\(").replace(")", "\\)").replace("{", "\\{").replace("}", "\\}");
    }
}