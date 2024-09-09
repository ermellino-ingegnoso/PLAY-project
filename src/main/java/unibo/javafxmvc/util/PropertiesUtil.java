package unibo.javafxmvc.util;

import unibo.javafxmvc.controller.AuxiliaryController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static String propertiesFilePath;
    private static Properties properties = new Properties();

    public static Boolean initialize(String propertiesFilePath) {
        PropertiesUtil.propertiesFilePath = propertiesFilePath;
        return loadProperties();
    }
    /** Carica le proprietà da un file. <br> Gestione interna delle eccezioni */
    private static Boolean loadProperties() {
        try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
            properties.load(input);
            return true;
        } catch (FileNotFoundException fnfe) {
            System.err.println("Caricamento del .properties: File not found: " + propertiesFilePath + "\t eccezione: " + fnfe.getMessage());
        } catch (SecurityException se) {
            AuxiliaryController.alertWindow("Errore", "Accesso al file negato: Aggiornare i permessi", "Non si dispone dei permessi necessari per accedere al file di configurazione");
            System.err.println("Caricamento del .properties: Accesso al file negato: " + se.getMessage());
        } catch (IOException ioe) {
            System.err.println("Caricamento del .properties: Errore di I/O: " + ioe.getMessage());
        } catch (Exception e) {
            System.err.println("Caricamento del .properties ⚠️ Errore sconosciuto: " + e.getMessage());
        }
        return false;
    }
    /** Modifica una proprietà di un file di configurazione */
    public static Boolean setProperty(String key, String value) throws IOException {
        properties.setProperty(key, value);
        return saveProperties();
    }
    /** Salva le proprietà su un file. <br> Gestione interna delle eccezioni
     * @return <code>true</code> se il salvataggio è avvenuto con successo, <code>false</code> altrimenti
     */
    private static Boolean saveProperties() {
        try (FileOutputStream output = new FileOutputStream(propertiesFilePath)) {
            properties.store(output, null);
            return true;
        } catch (FileNotFoundException fnfe) {
            System.err.println("Salvataggio del .properties: File not found: " + propertiesFilePath + "\t eccezione: " + fnfe.getMessage());
        } catch (SecurityException se) {
            AuxiliaryController.alertWindow("Errore", "Accesso al file negato: Aggiornare i permessi", "Non si dispone dei permessi necessari per accedere al file di configurazione");
            System.err.println("Salvataggio del .properties: Accesso al file negato: " + se.getMessage());
        } catch (IOException ioe) {
            System.err.println("Salvataggio del .properties: Errore di I/O: " + ioe.getMessage());
        } catch (Exception e) {
            System.err.println("Salvataggio del .properties ⚠️ Errore sconosciuto: " + e.getMessage());
        }
        return false;
    }
    /** @return il valore di una proprietà o <br><code>null</code> se la proprietà non esiste */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}