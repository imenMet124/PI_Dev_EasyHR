package tn.esprit.Offres.test;

import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Offres.entities.Candidature;
import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.services.ServiceCandidat;
import tn.esprit.Offres.entities.User;
import tn.esprit.Offres.services.ServiceCandidature;
import tn.esprit.Offres.services.ServiceOffres;
import tn.esprit.Offres.services.ServiceUser;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialiser les services
        ServiceUser serviceUser = new ServiceUser();
        ServiceCandidat serviceCandidat = new ServiceCandidat();
        ServiceOffres serviceOffres = new ServiceOffres();
        ServiceCandidature serviceCandidature = new ServiceCandidature();

        try {
            User userExistant = new User(1, "Jean Dupont", "jean.dupont@example.com",
                    "0612345678", "CANDIDAT_ROLE", "Développeur",
                    45000.0, null, "Actif", "Informatique");

            //  Sélectionner un candidat existant (idCandidat = 1, lié à userExistant)
            Candidat candidatExistant = new Candidat(1, userExistant, "Jean", "Dupont",
                    "jean.dupont@example.com", "0812345678",
                    "Développeur", "Informatique",
                    "/path/to/experience.pdf", "/path/to/competence.pdf",
                    Candidat.StatuCandidat.SHORTLISTE,
                    Candidat.Disponibilite.IMMEDIATE);

            //  Création d'un LocalDate
            LocalDate localDatePub = LocalDate.of(2025, 2, 24);

            // Conversion en java.util.Date
            java.util.Date utilDatePub = java.util.Date.from(localDatePub.atStartOfDay(ZoneId.systemDefault()).toInstant());

            //  Conversion en java.sql.Date pour l'insertion SQL
            java.sql.Date sqlDatePub = new java.sql.Date(utilDatePub.getTime());

            //  Création de l'objet Offre avec java.util.Date
            Offre offreExistante = new Offre(1, "Développeur Java",
                    "Développement d'applications Java",
                    utilDatePub, // ✅ Maintenant correct
                    Offre.StatutOffre.valueOf("FERME"), "Informatique", "Recruteur1");

            //  Créer une candidature avec ces données
            Candidature candidature = new Candidature(1, candidatExistant, offreExistante,
                    LocalDate.now(), Candidature.StatutCandidature.EN_COURS,
                    85, "Profil intéressant", null,
                    Candidature.ResultatEntretien.EN_ATTENTE,
                    Candidature.EtapeCandidature.ENTRETIEN_TECHNIQUE, LocalDate.now(),
                    "Recruteur1");

            // 🔹 Ajouter la candidature
            serviceCandidature.ajouter(candidature);
            System.out.println("✅ Candidature ajoutée avec succès ! ID : " + candidature.getIdCandidature());

        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de la candidature : " + e.getMessage());
        }
    }
}