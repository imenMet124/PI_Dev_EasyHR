package tn.esprit.Offres.entities;

import tn.esprit.Offres.controllers.CandidatController;
import tn.esprit.Users.entities.User;

public class Candidat {
    private int idCandidat;
    private User user;// Référence à l'utilisateur (employé)

    private String nom; // Nom de l'utilisateur
    private String prenom; // Prénom de l'utilisateur
    private String email; // Email de l'utilisateur
    private String phone; // Téléphone de l'utilisateur
    private String position; // Poste actuel de l'utilisateur
    private String department; // Département de l'utilisateur
    private String experienceInterne; // Chemin du fichier d'expérience interne
    private String competence; // Chemin du fichier de compétences
    private StatuCandidat statuCandidat = StatuCandidat.EN_ATTENTE; // Statut de la candidature avec valeur initiale
    private Disponibilite disponibilite = Disponibilite.IMMEDIATE; // Disponibilité

    public Candidat(int idCandidat, String nomCandidat, String prenomCandidat, String emailCandidat, String telephoneCandidat, String posteActuel, String departement, String experienceInterne, String competence, StatuCandidat statuCandidat, Disponibilite disponibilite) {
    }

    public Candidat(int idCandidat) {
    }

    public void setDisponibilite(CandidatController.Disponibilite value) {
    }

    public enum StatuCandidat {
        EN_ATTENTE, EN_COURS, SHORTLISTE, ENTREVUE, ACCEPTE, REFUSE
    }

    public enum Disponibilite {
        IMMEDIATE, UN_MOIS, DEUX_MOIS, TROIS_MOIS
    }

    // Constructeurs
    public Candidat() {
    }

    public Candidat(String nom, String email, String phone, String department, String position) {
        this.nom = nom;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.position = position;
    }

    public Candidat(int idCandidat, User user, String nom, String prenom, String email, String phone, String position, String department, String experienceInterne, String competence, StatuCandidat statuCandidat, Disponibilite disponibilite) {
        this.idCandidat = idCandidat;

        this.user = user;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.department = department;
        this.setExperienceInterne(experienceInterne); // Utilisation du setter pour la validation
        this.setCompetence(competence); // Utilisation du setter pour la validation
        this.statuCandidat = statuCandidat;
        this.disponibilite = disponibilite;
    }

    // Getters et setters
    public int getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(int idCandidat) {
        this.idCandidat = idCandidat;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExperienceInterne() {
        return experienceInterne;
    }

    public void setExperienceInterne(String experienceInterne) {
        if (experienceInterne == null || experienceInterne.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier d'expérience interne ne peut pas être vide.");
        }
        this.experienceInterne = experienceInterne;
    }

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        if (competence == null || competence.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier de compétences ne peut pas être vide.");
        }
        this.competence = competence;
    }

    public StatuCandidat getStatuCandidat() {
        return statuCandidat;
    }

    public void setStatuCandidat(StatuCandidat statuCandidat) {
        this.statuCandidat = statuCandidat;
    }

    public Disponibilite getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Disponibilite disponibilite) {
        this.disponibilite = disponibilite;
    }

    public Candidat(User user, String nom, String email, String phone, String position, String department, String experienceInterne, String competence, Disponibilite disponibilite) {
        this.user = user;
        this.nom = nom;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.department = department;
        this.experienceInterne = experienceInterne;
        this.competence = competence;
        this.disponibilite = disponibilite;
    }

    @Override
    public String toString() {
        return "Candidat{" +
                "idCandidat=" + idCandidat +
                ", user=" + user +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", experienceInterne='" + experienceInterne + '\'' +
                ", competence='" + competence + '\'' +
                ", statuCandidat=" + statuCandidat +
                ", disponibilite=" + disponibilite +
                '}';
    }
}