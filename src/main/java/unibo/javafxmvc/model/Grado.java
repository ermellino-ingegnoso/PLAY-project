package unibo.javafxmvc.model;

public enum Grado {
    PRINCIPIANTE,
    INTERMEDIO,
    AVANZATO,
    ESPERTO;

    public static Grado getGradoByOrdinal(int ordinal) {
        for (Grado grado : Grado.values()) {
            if (grado.ordinal() == ordinal) {
                return grado;
            }
        }
        return null;
    }
}