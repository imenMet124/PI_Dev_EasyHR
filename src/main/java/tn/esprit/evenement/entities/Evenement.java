package tn.esprit.evenement.entities;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class Evenement {
    private int id;
    private String titre;
    private String description;
    private Date date;
    private Time heure;
    private String lieu;
    private int capacite;
    private int nombreParticipants;
    private String imagePath;

    public Evenement(int eventId, String eventTitre, LocalDate eventDate) {}

    public Evenement(int id, String titre, String description, Date date, Time heure, String lieu, int capacite, int nombreParticipants, String imagePath) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.lieu = lieu;
        this.capacite = capacite;
        this.nombreParticipants = nombreParticipants;
        this.imagePath = imagePath;
    }

    public Evenement(String titre, String description, Date date, Time heure, String lieu, int capacite, String imagePath) {
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.lieu = lieu;
        this.capacite = capacite;
        this.nombreParticipants = 0; // Initialisation à 0
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Time getHeure() { return heure; }
    public void setHeure(Time heure) { this.heure = heure; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public int getNombreParticipants() { return nombreParticipants; }
    public void setNombreParticipants(int nombreParticipants) { this.nombreParticipants = nombreParticipants; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }


    public void incrementerParticipants() {
        this.nombreParticipants++;
    }


    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", heure=" + heure +
                ", lieu='" + lieu + '\'' +
                ", capacite=" + capacite +
                ", nombreParticipants=" + nombreParticipants +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
