package unibo.javafxmvc.model;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import unibo.javafxmvc.Main;

import java.util.Objects;
/** È buona pratica invocare prima il metodo di deserializzazione in maniera da assicurare uno specifico ClassLoader ️ */
public class JsonSerializer {
    /*⚠️ Le modifiche in questo file sono di rilevanza globale ⚠️*/
    private static Gson gson = new Gson();

    public static <T> boolean serializeToJson(T object, File file) {
        if(checkFile(file)){
            String json = gson.toJson(object);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream)) {
                writer.write(json);
                return true;
            } catch (IOException ioe) {
                System.err.println("Errore di I/O nella scrittura del file: " + ioe.getMessage());
                return false;
            } catch (SecurityException se) {
                System.err.println("Accesso negato per il file " + file.getPath() + "\n" + se.getMessage());
                return false;
            }
        }
        return false;
    }
    public static Boolean checkFile(File file) {
        if (file == null) {
            System.err.println("File non fornito.");
            return false;
        }
        if (!file.exists()) {
            System.err.println("Il file specificato non esiste: " + file.getAbsolutePath());
            return false;
        }
        if (!file.canRead()) {
            System.err.println("Permessi di lettura negati per il file: " + file.getAbsolutePath());
            return false;
        }
        return true;
    }
    public static <T> List<T> deserializeJsonFile(File file, Class<T> returnClass) {
        if(checkFile(file)) {
            Type listType = TypeToken.getParameterized(List.class, returnClass).getType();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                List<T> list = gson.fromJson(reader, listType);
                return (list != null) ? new ArrayList<>(list) : new ArrayList<>();
            } catch (SecurityException e) {
                System.err.println("Errore di sicurezza nella lettura del file: " + e.getMessage());
            }catch (IOException e) {
                System.err.println("Errore nella lettura del file: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Errore durante la deserializzazione: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }
}
