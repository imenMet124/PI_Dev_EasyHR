package tn.esprit.Offres.services;

import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Offres.entities.Candidature;
import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.utils.Base;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCandidature implements IService<Candidature> {
    private Connection connection;

    public ServiceCandidature() {
        connection = Base.getInstance().getConnection();
    }

    public void ajouter(Candidature candidature) throws SQLException {
        if (candidature.getCandidat() == null || candidature.getOffre() == null) {
            throw new SQLException("Le candidat et l'offre doivent être spécifiés !");
        }

        String sql = "INSERT INTO candidature (idCandidat, nom, prenom, email, phone, " +
                "position, departement, experienceInterne, competence, statuCandidat, disponibilite, " +
                "idOffre, titreOffre, " + // ✅ Correction : titrePoste au lieu de titreOffre
                "`dateCandidature`, statuCandidature, noteCandidat, commentaires, `dateEntretien`, " + // ✅ Ajout des backticks (`) pour éviter les conflits
                "resultatEntretien, etapeActuelle, `dateMiseAJourStatut`, recruteurResponsable) " + // ✅ Backticks sur les champs date
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //  Insérer les données du candidat
            preparedStatement.setInt(1, candidature.getCandidat().getIdCandidat());
            preparedStatement.setString(2, candidature.getCandidat().getNom());
            preparedStatement.setString(3, candidature.getCandidat().getPrenom());
            preparedStatement.setString(4, candidature.getCandidat().getEmail());
            preparedStatement.setString(5, candidature.getCandidat().getPhone());
            preparedStatement.setString(6, candidature.getCandidat().getPosition());
            preparedStatement.setString(7, candidature.getCandidat().getDepartment());
            preparedStatement.setString(8, candidature.getCandidat().getExperienceInterne());
            preparedStatement.setString(9, candidature.getCandidat().getCompetence());
            preparedStatement.setString(10, candidature.getCandidat().getStatuCandidat().name());
            preparedStatement.setString(11, candidature.getCandidat().getDisponibilite().name());

            // 🟢 Insérer les données de l’offre
            preparedStatement.setInt(12, candidature.getOffre().getIdOffre());
            preparedStatement.setString(13, candidature.getOffre().getTitrePoste());

            // 🟢 Insérer les données spécifiques à la candidature
            preparedStatement.setDate(14, Date.valueOf(candidature.getDateCandidature()));
            preparedStatement.setString(15, candidature.getStatutCandidature().name());
            preparedStatement.setInt(16, candidature.getNoteCandidat());
            preparedStatement.setString(17, candidature.getCommentaires());
            preparedStatement.setDate(18, candidature.getDateEntretien() != null ? Date.valueOf(candidature.getDateEntretien()) : null);
            preparedStatement.setString(19, candidature.getResultatEntretien().name());
            preparedStatement.setString(20, candidature.getEtapeActuelle().name());
            preparedStatement.setDate(21, candidature.getDateMiseAJourStatut() != null ? Date.valueOf(candidature.getDateMiseAJourStatut()) : null);
            preparedStatement.setString(22, candidature.getRecruteurResponsable());

            // 🟢 Exécution de la requête
            preparedStatement.executeUpdate();

            // Récupérer l'ID auto-généré
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    candidature.setIdCandidature(generatedKeys.getInt(1));
                    System.out.println("✅ Candidature ajoutée avec succès ! ID : " + candidature.getIdCandidature());
                }
            }
        }
    }

    public int ajouterCandidature(Candidature candidature) {
        String sql = "INSERT INTO candidature (idCandidat, nom, prenom, email, phone, " +
                "experienceInterne, competence, disponibilite, idOffre, titreOffre) " + // Suppression de la virgule après titreOffre
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Vérification et affichage des valeurs avant l'insertion
            System.out.println("🔍 Vérification des données de la candidature avant insertion :");
            System.out.println("📌 ID Candidat: " + candidature.getCandidat().getIdCandidat());
            System.out.println("📌 Nom: " + candidature.getCandidat().getNom());
            System.out.println("📌 Prénom: " + candidature.getCandidat().getPrenom());
            System.out.println("📌 Email: " + candidature.getCandidat().getEmail());
            System.out.println("📌 Téléphone: " + candidature.getCandidat().getPhone());
            System.out.println("📌 Expérience Interne: " + candidature.getCandidat().getExperienceInterne());
            System.out.println("📌 Compétences: " + candidature.getCandidat().getCompetence());

            // Vérification et assignation d'une valeur par défaut si la disponibilité est NULL
            if (candidature.getCandidat().getDisponibilite() == null) {
                System.out.println("⚠ Disponibilité NULL, assignation à IMMEDIATE par défaut.");
                candidature.getCandidat().setDisponibilite(Candidat.Disponibilite.IMMEDIATE);
            }
            System.out.println("📌 Disponibilité: " + candidature.getCandidat().getDisponibilite().name());

            // Vérification et affichage des valeurs de l'offre
            System.out.println("📌 ID Offre: " + candidature.getOffre().getIdOffre());
            System.out.println("📌 Titre Offre: " + candidature.getOffre().getTitrePoste());

            // Insertion des données du candidat
            preparedStatement.setInt(1, candidature.getCandidat().getIdCandidat());
            preparedStatement.setString(2, candidature.getCandidat().getNom());
            preparedStatement.setString(3, candidature.getCandidat().getPrenom());
            preparedStatement.setString(4, candidature.getCandidat().getEmail());
            preparedStatement.setString(5, candidature.getCandidat().getPhone());
            preparedStatement.setString(6, candidature.getCandidat().getExperienceInterne());
            preparedStatement.setString(7, candidature.getCandidat().getCompetence());
            preparedStatement.setString(8, candidature.getCandidat().getDisponibilite().name());

            // Insertion des données de l’offre
            preparedStatement.setInt(9, candidature.getOffre().getIdOffre());
            preparedStatement.setString(10, candidature.getOffre().getTitrePoste());

            // Exécution de la requête
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("❌ Échec de l'ajout de la candidature, aucune ligne affectée.");
            }

            // Récupération de l'ID généré
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idCandidature = generatedKeys.getInt(1);
                    candidature.setIdCandidature(idCandidature);
                    System.out.println("✅ Candidature ajoutée avec succès ! ID : " + idCandidature);
                    return idCandidature;
                } else {
                    throw new SQLException("❌ Échec de l'ajout de la candidature, aucun ID généré.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("⚠ Erreur SQL lors de l'ajout de la candidature : " + e.getMessage());
            return -1; // Indiquer un échec
        }
    }



    @Override
    public void modifier(Candidature candidature) throws SQLException {
        String sql = "UPDATE candidature SET statuCandidature = ?, noteCandidat = ?, commentaires = ?, " +
                "dateEntretien = ?, resultatEntretien = ?, etapeActuelle = ?, dateMiseAJourStatut = ?, " +
                "recruteurResponsable = ? WHERE idCandidature = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, candidature.getStatutCandidature().name());
            preparedStatement.setInt(2, candidature.getNoteCandidat());
            preparedStatement.setString(3, candidature.getCommentaires());
            preparedStatement.setDate(4, candidature.getDateEntretien() != null ? Date.valueOf(candidature.getDateEntretien()) : null);
            preparedStatement.setString(5, candidature.getResultatEntretien().name());
            preparedStatement.setString(6, candidature.getEtapeActuelle().name());
            preparedStatement.setDate(7, candidature.getDateMiseAJourStatut() != null ? Date.valueOf(candidature.getDateMiseAJourStatut()) : null);
            preparedStatement.setString(8, candidature.getRecruteurResponsable());
            preparedStatement.setInt(9, candidature.getIdCandidature());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM candidature WHERE idCandidature = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Candidature> afficher() throws SQLException {
        List<Candidature> candidatures = new ArrayList<>();
        String sql = "SELECT c.*, ca.*, o.* " +
                "FROM candidature c " +
                "JOIN candidat ca ON c.idCandidat = ca.idCandidat " +
                "JOIN offre_emploi o ON c.idOffre = o.idOffre";

        Map<Integer, Candidat> candidatsMap = new HashMap<>();
        Map<Integer, Offre> offresMap = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int idCandidat = rs.getInt("idCandidat");
                int idOffre = rs.getInt("idOffre");

                Candidat candidat = candidatsMap.computeIfAbsent(idCandidat, id -> {
                    try {
                        return new Candidat(
                                id,
                                rs.getString("nomCandidat"),
                                rs.getString("prenomCandidat"),
                                rs.getString("emailCandidat"),
                                rs.getString("telephoneCandidat"),
                                rs.getString("posteActuel"),
                                rs.getString("departement"),
                                rs.getString("experienceInterne"),
                                rs.getString("competence"),
                                Candidat.StatuCandidat.valueOf(rs.getString("statuCandidat")),
                                Candidat.Disponibilite.valueOf(rs.getString("disponibilite"))
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Offre offre = offresMap.computeIfAbsent(idOffre, id -> {
                    try {
                        return new Offre(
                                id,
                                rs.getString("titrePoste"),
                                rs.getString("description"),
                                rs.getDate("datePublication"),
                                Offre.StatutOffre.valueOf(rs.getString("statuOffre")), // Convertit la valeur ENUM de SQL vers Java
                                rs.getString("departement"), // Le département reste une String
                                rs.getString("recruteurResponsable")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });


                Candidature candidature = new Candidature(
                        rs.getInt("idCandidature"),
                        candidat,
                        offre,
                        rs.getDate("dateCandidature").toLocalDate(),
                        Candidature.StatutCandidature.valueOf(rs.getString("statuCandidature")),
                        rs.getInt("noteCandidat"),
                        rs.getString("commentaires"),
                        rs.getDate("dateEntretien") != null ? rs.getDate("dateEntretien").toLocalDate() : null,
                        Candidature.ResultatEntretien.valueOf(rs.getString("resultatEntretien")),
                        Candidature.EtapeCandidature.valueOf(rs.getString("etapeActuelle")),
                        rs.getDate("dateMiseAJourStatut") != null ? rs.getDate("dateMiseAJourStatut").toLocalDate() : null,
                        rs.getString("recruteurResponsable")
                );

                candidatures.add(candidature);
            }
        }
        return candidatures;
    }

    public List<Candidature> afficherCandidaturesFormatees() throws SQLException {
        List<Candidature> candidatures = new ArrayList<>();
        String sql = "SELECT c.idCandidature, " +
                "ca.nomCandidat, ca.prenomCandidat, ca.emailCandidat, ca.telephoneCandidat, " +
                "ca.posteActuel, ca.departement, ca.experienceInterne, ca.competence, ca.disponibilite, " +
                "o.titrePoste " +
                "FROM candidature c " +
                "JOIN candidat ca ON c.idCandidat = ca.idCandidat " +
                "JOIN offre_emploi o ON c.idOffre = o.idOffre";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                // ✅ Création de l'objet Candidat avec seulement les attributs nécessaires
                Candidat candidat = new Candidat();
                candidat.setNom(rs.getString("nomCandidat"));
                candidat.setPrenom(rs.getString("prenomCandidat"));
                candidat.setEmail(rs.getString("emailCandidat"));
                candidat.setPhone(rs.getString("telephoneCandidat"));
                candidat.setPosition(rs.getString("posteActuel"));
                candidat.setDepartment(rs.getString("departement"));
                candidat.setExperienceInterne(rs.getString("experienceInterne"));
                candidat.setCompetence(rs.getString("competence"));
                candidat.setDisponibilite(Candidat.Disponibilite.valueOf(rs.getString("disponibilite")));

                // ✅ Création de l'objet Offre (uniquement le titre de l’offre)
                Offre offre = new Offre();
                offre.setTitrePoste(rs.getString("titrePoste"));

                // ✅ Création de l'objet Candidature
                Candidature candidature = new Candidature();
                candidature.setIdCandidature(rs.getInt("idCandidature"));
                candidature.setCandidat(candidat);
                candidature.setOffre(offre);

                candidatures.add(candidature);
            }
        }

        return candidatures;
    }


}