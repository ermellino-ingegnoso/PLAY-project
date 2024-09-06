package unibo.javafxmvc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.h2.store.fs.FileUtils;
import unibo.javafxmvc.Main;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CommentiFactory {
    ArrayList<CommentiModel> esercizi;

    public CommentiFactory() {
        esercizi = new ArrayList<CommentiModel>();
        esercizi.add(new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova1.png", new ArrayList<String>(Arrays.asList("gang1", "gang2","gang3")), "gang1"));
        esercizi.add(new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova2.png", new ArrayList<String>(Arrays.asList("gang1", "gang2","gang3")), "gang2"));
        esercizi.add(new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova3.png", new ArrayList<String>(Arrays.asList("gang1", "gang2","gang3")), "gang3"));
    }

    public CommentiModel getCommentiModel(int i) {
        if (i < 0 || i >= esercizi.size()) {
            return null;
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
