package unibo.javafxmvc.model;

public class RowData {
    private final Integer exercise;
    private final Integer score;

    public RowData(Integer exercise, Integer score) {
        this.exercise = exercise;
        this.score = score;
    }

    public Integer getExercise() {
        return exercise;
    }

    public Integer getScore() {
        return score;
    }
}
