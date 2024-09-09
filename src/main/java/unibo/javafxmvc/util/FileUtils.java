package unibo.javafxmvc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileUtils {
    public static void deleteDirectoryContent(Path directoryPath) {
        try (Stream<Path> files = Files.walk(directoryPath)) {
            files.sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(directoryPath))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Errore durante la cancellazione del file: " + path + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Errore durante la pulizia della cartella: " + e.getMessage());
        }
    }
    /**Legge un file di testo dalle risorse e lo converte in una stringa.
     * @param resourcePath Il percorso del file di testo.
     * @return <b>String</b> che rappresenta il contenuto del <b>resourcePath</b>
     * @throws IOException Se si verifica un errore di I/O.
     */
    public static String readFileFromResources(String resourcePath) throws IOException {
        try (InputStream inputStream = FileUtils.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }
}