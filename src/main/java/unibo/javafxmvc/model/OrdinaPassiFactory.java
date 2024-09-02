package unibo.javafxmvc.model;

import java.util.ArrayList;
import java.util.Arrays;

public class OrdinaPassiFactory {
    public static OrdinaPassiModel getOrdinaPassiModel(){
        return new OrdinaPassiModel(new ArrayList<String>(Arrays.asList("/unibo/javafxmvc/Images/Ordina/Prova1.png","/unibo/javafxmvc/Images/Ordina/Prova2.png","/unibo/javafxmvc/Images/Ordina/Prova3.png")), 1, "132", "Enrico");
        /*
        return new OrdinaPassiModel(new ArrayList<String>(Arrays.asList("/unibo/javafxmvc/Images/Ordina/Prova4.png","/unibo/javafxmvc/Images/Ordina/Prova5.png","/unibo/javafxmvc/Images/Ordina/Prova6.png")), 2, "213", "Enrico");
        return new OrdinaPassiModel(new ArrayList<String>(Arrays.asList("/unibo/javafxmvc/Images/Ordina/Prova7.png","/unibo/javafxmvc/Images/Ordina/Prova8.png","/unibo/javafxmvc/Images/Ordina/Prova9.png")), 3, "312", "Enrico");

         */
    }

    public OrdinaPassiFactory(){

    }

}
