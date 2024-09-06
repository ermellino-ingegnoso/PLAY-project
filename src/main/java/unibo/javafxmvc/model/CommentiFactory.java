package unibo.javafxmvc.model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CommentiFactory {
    public static CommentiModel getCommentiModel() {
        return new CommentiModel("/unibo/javafxmvc/Images/Ordina/Prova1.png", new ArrayList<String>(Arrays.asList("gang1", "gang2","gang3")), "gang1");
    }

}
