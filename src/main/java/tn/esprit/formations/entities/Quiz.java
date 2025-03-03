package tn.esprit.formations.entities;

import java.util.List;

public class Quiz {
    private int id;
    private String title;
    private List<Question> questions;
    private Float passingscore;

    // Constructor with all fields
    public Quiz(int id, String title, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
        calculatePassingScore();
    }

    // Constructor without ID (for creating new quizzes)
    public Quiz(String title, List<Question> questions) {
        this.title = title;
        this.questions = questions;
        calculatePassingScore();
    }

    // Calculate passing score as total questions / 2
    private void calculatePassingScore() {
        if (questions != null && !questions.isEmpty()) {
            this.passingscore = (float) questions.size() / 2;
        } else {
            this.passingscore = 0.0f;
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        calculatePassingScore();
    }

    public Float getPassingscore() {
        return passingscore;
    }
}