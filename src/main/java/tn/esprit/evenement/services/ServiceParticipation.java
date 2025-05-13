package tn.esprit.evenement.services;

import tn.esprit.Users.entities.User;
import tn.esprit.Users.model.UserSession;
import tn.esprit.evenement.utils.MyDataBase;
import tn.esprit.evenement.entities.Participation;
import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.entities.Utilisateur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipation {
    private Connection connection;

    public ServiceParticipation() {
        this.connection = MyDataBase.getInstance().getConnection();
    }


    public void ajouterParticipation(Participation p) {
        String req = "INSERT INTO participation (Id_Evenement, Participant, `Date inscription`, Statut) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, p.getEvenement().getId()); // Utilisation de l'objet `Evenement`

            // Obtenir l'utilisateur connecté
            User loggedInUser = UserSession.getInstance().getLoggedInUser();
            if (loggedInUser == null) {
                System.out.println("❌ Aucun utilisateur connecté !");
                return;
            }

            ps.setInt(2, loggedInUser.getIyedIdUser()); // Assigner l'utilisateur connecté
            ps.setDate(3, Date.valueOf(p.getDateInscription()));
            ps.setString(4, p.getStatut());

            ps.executeUpdate();
            System.out.println("✅ Participation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de la participation : " + e.getMessage());
        }
    }



    public void modifierStatut(int idParticipation, String nouveauStatut) {
        String req = "UPDATE participation SET Statut = ? WHERE Id_Participation = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, nouveauStatut);
            ps.setInt(2, idParticipation);
            ps.executeUpdate();
            System.out.println("✅ Statut mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour du statut : " + e.getMessage());
        }
    }

    public void supprimerParticipation(int idParticipation) {
        String req = "DELETE FROM participation WHERE Id_Participation = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, idParticipation);
            ps.executeUpdate();
            System.out.println("✅ Participation supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de la participation : " + e.getMessage());
        }
    }

    public List<Participation> getParticipationsParUtilisateur(int idUser) {
        List<Participation> participations = new ArrayList<>();
        String req = "SELECT p.Id_Participation, p.`Date inscription`, p.Statut, " +
                "e.Id AS event_id, e.Titre AS event_titre, e.Date AS event_date, " +
                "u.Id AS user_id, u.Nom AS user_nom " +
                "FROM participation p " +
                "JOIN evenement e ON p.Id_Evenement = e.Id " +
                "JOIN utilisateur u ON p.Participant = u.Id " +
                "WHERE p.Participant = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Evenement event = new Evenement(rs.getInt("event_id"), rs.getString("event_titre"), rs.getDate("event_date").toLocalDate());
                Utilisateur user = new Utilisateur(rs.getInt("user_id"), rs.getString("user_nom"));


                Participation participation = new Participation(
                        rs.getInt("Id_Participation"),
                        event,  // Passer un objet `Evenement`
                        user,   // Passer un objet `Utilisateur`
                        rs.getDate("Date inscription").toLocalDate(),
                        rs.getString("Statut")
                );

                participations.add(participation);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des participations : " + e.getMessage());
        }
        return participations;
    }
}
