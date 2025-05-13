package tn.esprit.evenement.entities;

import java.time.LocalDate;

public class Participation {
    private int idParticipation;
    private Evenement evenement;
    private Utilisateur participant;
    private LocalDate dateInscription;
    private String statut; // "Confirmé", "Annulé", "En attente"

    public Participation(int idParticipation, Evenement evenement, Utilisateur participant, LocalDate dateInscription, String statut) {
        this.idParticipation = idParticipation;
        this.evenement = evenement;
        this.participant = participant;
        this.dateInscription = dateInscription;
        this.statut = statut;
    }

    public Participation(Evenement evenement, Utilisateur participant, LocalDate dateInscription, String statut) {
        this.evenement = evenement;
        this.participant = participant;
        this.dateInscription = dateInscription;
        this.statut = statut;
    }

    public int getIdParticipation() {
        return idParticipation;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public Utilisateur getParticipant() {
        return participant;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public void setParticipant(Utilisateur participant) {
        this.participant = participant;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "idParticipation=" + idParticipation +
                ", evenement=" + evenement.getTitre() +  // 🔹 Affichage du titre de l'événement
                ", participant=" + participant.getNom() + " " + participant.getPrenom() +  // 🔹 Affichage du nom du participant
                ", dateInscription=" + dateInscription +
                ", statut='" + statut + '\'' +
                '}';
    }
}
