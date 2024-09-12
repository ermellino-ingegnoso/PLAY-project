package unibo.javafxmvc.util;

import unibo.javafxmvc.controller.AuxiliaryController;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* Tool per il controllo delle regex: https://regex101.com/
   Queste classi di utilità sono zeppe di commenti allo scopo di dare una spiegazione dettagliata di codici poco intuitivi e scarsamente usati.
 */
public class CodeValidator {    // I criteri di separazione logica non sono rispettati in quanto si fa largo uso dell'AuxialiaryController
    /**
     * Verifica se il codice della classe e il codice del metodo sono validi.
     * Controlli eseguiti internamente:
     * <ul>
     *   <li>Verifica che <b>codiceClasse</b> contenga una dichiarazione di classe;</li>
     *   <li>Verifica che <b>codiceClasse</b> contenga un metodo <code>main</code>;</li>
     *   <li>Trova la firma del metodo in <b>codiceMetodo</b>;</li>
     *   <li>Verifica che il metodo <code>main</code> richiami la firma del metodo trovata in <b>codiceMetodo</b>;</li>
     * </ul>
     * </p>
     *
     * @param codiceClasse Il codice della classe da verificare
     * @param codiceMetodo Il codice del metodo da verificare
     * @return true se il codice della classe e del metodo sono validi, <br>altrimenti false
     */
    public static Boolean checkCodice(String codiceClasse, String codiceMetodo) {
        if (!containsPattern(codiceClasse, "public class (\\w+)", "Classe mancante", "Il codice deve contenere una classe")) return false;
        // Pattern per trovare il metodo main
        Matcher mainMatcher = getMatcher(codiceClasse, "public static void main\\(String\\[] args\\)\\s*\\{([^}]*)\\}");
        if (mainMatcher == null) {
            AuxiliaryController.alertWindow("Errore", "Metodo main mancante", "Il codice deve contenere un metodo main");
            return false;
        }
        // Estrai la firma del metodo da codiceMetodo
        String methodSignature = SignatureFinder.extractSignature(codiceMetodo);
        if (methodSignature == null) AuxiliaryController.alertWindow("Errore", "Firma del metodo mancante", "Il codice del metodo deve contenere una firma valida.");
        // Verifica che il metodo main richiami la firma del metodo
        if (!containsPattern(mainMatcher.group(1), SignatureFinder.extractMethodName(methodSignature) + "\\s*\\(", "Firma del metodo non richiamata", "Il metodo main deve richiamare la firma del metodo.")) return false;
        return true;    // Se tutti i controlli passano (il codice è completamente validato)
    }
    public static Boolean checkCodiceAvanzato(String codiceClasse, String codiceMetodo) {
        Matcher mainMatcher = CodeValidator.getMatcher(codiceClasse, "public static void main\\(String\\[] args\\)\\s*\\{([^}]*)\\}");
        if (mainMatcher == null) {
            AuxiliaryController.alertWindow("Errore", "Metodo main mancante", "Il codice deve contenere un metodo main");
            return false;
        }
        String methodSignature = SignatureFinder.extractSignature(codiceMetodo);
        if (methodSignature == null) {
            AuxiliaryController.alertWindow("Errore", "Firma del metodo mancante", "Il codice del metodo deve contenere una firma valida.");
            return false;
        }
        String dynamicRegex = SignatureFinder.getDynamicMethodRegex(methodSignature);
        if (!CodeValidator.containsPattern(codiceClasse, dynamicRegex, "Metodo non trovato", "Il codice della classe non contiene un metodo con la stessa firma del metodo specificato.")) return false;
        return true;
    }
    private static boolean containsPattern(String text, String pattern, String errorTitle, String errorMessage) {
        if (!Pattern.compile(pattern).matcher(text).find()) {
            AuxiliaryController.alertWindow("Errore", errorTitle, errorMessage);
            return false;
        }
        return true;
    }
    private static Matcher getMatcher(String text, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        return matcher.find() ? matcher : null;
    }
    /** Aggrega il codice interno alla firma di <b>methodCode</b> all'interno della classe <b>classCode</b>. L'unico controllo effettuato è la presenza del blocco <code>main</code>
     * @param classCode il codice della classe
     * @param methodCode il codice del metodo da inserire nella classe (firma e corpo)
     * @return - <b>methodCode</b> aggregato a <b>classCode</b> o <br> -<code>null</code> nel caso in cui non sia presente un metodo <code>main</code> all'interno di <b>classCode</b>
     * */
    public static String combineCode(String classCode, String methodCode) {
        // Trova la posizione del metodo main in classCode
        Matcher mainMatcher = Pattern.compile("public static void main\\(String\\[] args\\)\\s*\\{").matcher(classCode);
        if (!mainMatcher.find()) return null;
        // Inserisci methodCode prima del metodo main in classCode
        int mainStartIndex = mainMatcher.start();
        return classCode.substring(0, mainStartIndex) + methodCode + "\n" + classCode.substring(mainStartIndex);
    }
    /*
    public static Boolean checkCodice(String codiceClasse, String codiceMetodo) {
        if (!containsPattern(codiceClasse, "public class (\\w+)", "Classe mancante", "Il codice deve contenere una classe.")) return false;
        //                                              Pattern per trovare il metodo main
        Matcher mainMatcher = getMatcher(codiceClasse, "public static void main\\(String\\[] args\\)\\s*\\{([^}]*)\\}");
        if (mainMatcher == null) {
            AuxiliaryController.alertWindow("Errore", "Metodo main mancante", "Il codice deve contenere un metodo main.");
            return false;
        }
        // methodSignaturesEmpty indica i metodi senza corpo o con corpo interamente commentato ovvero metodi non implementati
        ArrayList<String> methodSignaturesEmpty = new ArrayList<>(SignatureFinder.findEmptyMethodSignatures(codiceClasse));
        if (methodSignaturesEmpty.isEmpty()) {  // Se non ci sono metodi non implementati
            AuxiliaryController.alertWindow("Errore", "Nessun metodo non implementanto", "Deve essere presente un metodo non implementato. Ricorda che la firma del metodo può contenere solamente caratteri alfanumerici e _");
            return false;
        }   // Se c'è sono più di un metodo non implementato
        if (methodSignaturesEmpty.size() > 1) {
            AuxiliaryController.alertWindow("Errore", "Troppi metodi non implementati", "Il metodo non implementato deve essere uno solo.");
            return false;
        }   // Se il metodo non implementato è già implementato:
            // methodSignature è la firma del metodo non implementato
        String methodSignature = methodSignaturesEmpty.get(0);
        String dynamicRegex = SignatureFinder.getDynamicMethodRegex(methodSignature);
        //  ⬇  verifica che la firma del metodo (codiceMetodo) corrisponda alla firma del metodo non implementato (methodSignature) trovata nella classe:
        if (!containsPattern(codiceMetodo, dynamicRegex, "Metodo non implementato correttamente", "Il metodo implementato non è corretto.")) return false;
        return true;    // Se tutti i controlli passano (il codice è completamente validato)
        * */
}
