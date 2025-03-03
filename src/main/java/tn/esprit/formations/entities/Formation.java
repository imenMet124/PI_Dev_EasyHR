package tn.esprit.formations.entities;

import java.time.LocalDateTime;

public class Formation {
    private int id;
    private String titre;
    private String description;
    private String filePath;
    private LocalDateTime dateCreation;
    private Quiz quiz;

    public Formation(int id, String titre, String description, String filePath, LocalDateTime dateCreation) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.filePath = filePath;
        this.dateCreation = dateCreation;
    }

    public Formation(String titre, String description, String filePath, LocalDateTime dateCreation) {
        this.titre = titre;
        this.description = description;
        this.filePath = filePath;
        this.dateCreation = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Formation formation = (Formation) o;

        return id == formation.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
