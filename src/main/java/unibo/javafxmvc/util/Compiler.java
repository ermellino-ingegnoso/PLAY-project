package unibo.javafxmvc.util;

import unibo.javafxmvc.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Files;

import java.io.IOException;
import java.nio.file.StandardOpenOption;

public class Compiler {
    /**Compila ed esegue il codice Java fornito.
     * <p>Questo metodo crea un file temporaneo o lo recupera se già presente, lo compila utilizzando <code>javac</code>,
     * e poi esegue la classe compilata utilizzando il <code>java</code>. L'output dell'esecuzione viene restituito come <code>String</code></p>
     * @param combinedCode il codice Java da compilare ed eseguire
     * @return l'output del codice Java (se) eseguito
     * @throws IOException se si verifica un errore di I/O durante la creazione del file, la scrittura o l'esecuzione del processo
     * @throws InterruptedException se il thread corrente viene interrotto mentre attende il completamento del processo di compilazione o esecuzione
     * @throws RuntimeException se il processo di compilazione termina per errrore di compilazione con status != 0; <br> - se il nome della classe non viene trovato nel codice fornito
     */
    public static String compileAndRunCodeVecchio(String combinedCode) throws IOException, InterruptedException, RuntimeException {
        System.out.println("Compilazione ed esecuzione del codice fornito..." +
                "\nCodice fornito:\n" + combinedCode +
                "\n" + "Temp path: " + Main.tempPath +
                "\n" + "Java temp path: " + Path.of(Main.tempPath,  SignatureFinder.extractClassName(combinedCode) + ".java"));
        String className = SignatureFinder.extractClassName(combinedCode);
        if (className == null) throw new RuntimeException("Nome della classe non trovato nel codice fornito.");
        // Recupera il file se esiste già
        Path file = Path.of(Main.tempPath, className + ".java");
        if (!Files.exists(file)) Files.createFile(file);
        // Sovrascrivi il contenuto del file
        try{
            Files.write(file, combinedCode.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ioe){
            throw new IOException("Errore durante la scrittura del file: " + ioe.getMessage());
        }
        // Compila il file
        Process compileProcess = new ProcessBuilder("javac", file.toString()).start();
        compileProcess.waitFor();
        if (compileProcess.exitValue() != 0) throw new RuntimeException("Errore di compilazione: " + new String(compileProcess.getErrorStream().readAllBytes()));
        // Esegui il file compilato
        Process runProcess = new ProcessBuilder("java", file.toString().replace(".java", "")).start();
        runProcess.waitFor();
        return new String(runProcess.getInputStream().readAllBytes());
    }

    public static String compileAndRunCode(String combinedCode) throws IOException, InterruptedException, RuntimeException {
        System.out.println("Compilazione ed esecuzione del codice fornito..." +
                "\nCodice fornito:\n" + combinedCode +
                "\n" + "Temp path: " + Main.tempPath +
                "\n" + "Java temp path: " + Path.of(Main.tempPath, SignatureFinder.extractClassName(combinedCode) + ".java"));
        String className = SignatureFinder.extractClassName(combinedCode);
        if (className == null) throw new RuntimeException("Nome della classe non trovato nel codice fornito.");

        Path filePath = Path.of(Main.tempPath, className + ".java");
        File fileJava = filePath.toFile();
        if (!Files.exists(filePath)) Files.createFile(filePath);
        try {                       Files.write(filePath, combinedCode.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);}
        catch (IOException ioe) {   throw new IOException("Errore durante la scrittura del file: " + ioe.getMessage());}
        // Compila il file Java
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("javac", fileJava.getPath());
        Process compileProcess = compileProcessBuilder.start();
        compileProcess.waitFor();
        if (compileProcess.exitValue() != 0) throw new RuntimeException("Errore di compilazione: " + new String(compileProcess.getErrorStream().readAllBytes()));
        // Esegui il file Java compilato
        ProcessBuilder runProcessBuilder = new ProcessBuilder("java", className);
        runProcessBuilder.directory(fileJava.getParentFile());
        Process runProcess = runProcessBuilder.start();
        // Intercetta l'output del processo
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        } catch (IOException ioe) { throw new IOException("Errore durante la lettura dell'output del processo: " + ioe.getMessage());}
        // Intercetta gli errori del processo
        StringBuilder errorOutput = new StringBuilder();
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append(System.lineSeparator());
            }
        } catch (IOException ioe) { throw new IOException("Errore durante la lettura dell'output del processo: " + ioe.getMessage());}
        // Attendi il termine del processo
        int exitCode = runProcess.waitFor();
        System.out.println("Process terminated with exit code: " + exitCode);
        return output.toString() + errorOutput.toString();
    }
}

