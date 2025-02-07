package tn.esprit.evenement.entities;

import java.sql.Timestamp;
import java.util.Date;

public class Evenement {
    private int id;
    private String titre;
    private String description;
    private Timestamp date;
    private String lieu;
    private int capacite;

    public Evenement() {}

    public Evenement(int id, String titre, String description, Timestamp date, String lieu, int capacite) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.capacite = capacite;
    }
    public Evenement(String titre, String description, Timestamp date, String lieu, int capacite) {
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.capacite = capacite;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", lieu='" + lieu + '\'' +
                ", capacite=" + capacite +
                '}';
    }
}
