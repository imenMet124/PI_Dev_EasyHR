package tn.esprit.formations.entities;

import java.util.List;

public class Question {
    private int id;
    private String text;
    private List<Option> options;
    private Quiz quiz; // Optional: reference back to the quiz it belongs to

    // Constructor with all fields
    public Question(int id, String text, List<Option> options, Quiz quiz) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.quiz = quiz;
    }

    // Constructor without ID (for creating new questions)
    public Question(String text, List<Option> options, Quiz quiz) {
        this.text = text;
        this.options = options;
        this.quiz = quiz;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}