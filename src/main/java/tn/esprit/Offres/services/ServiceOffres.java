package tn.esprit.Offres.services;

import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.utils.Base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffres implements IService<Offre> {
    private final Connection connection;

    public ServiceOffres() {
        connection = Base.getInstance().getConnection();
    }

    @Override
    public void ajouter(Offre offre) throws SQLException {
        String sql = "INSERT INTO `offre_emploi`(`titrePoste`, `description`, `datePublication`, `dateAcceptation`, `timeToHire`, `timeToFill`, `statuOffre`, `departement`, `recruteurResponsable`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, offre.getTitrePoste());
            preparedStatement.setString(2, offre.getDescription());
            preparedStatement.setDate(3, offre.getDatePublication());
            preparedStatement.setDate(4, offre.getDateAcceptation());
            preparedStatement.setInt(5, offre.getTimeToHire());
            preparedStatement.setInt(6, offre.getTimeToFill());
            preparedStatement.setString(7, offre.getStatuOffre().name()); // ENUM -> String
            preparedStatement.setString(8, offre.getDepartement()); // ENUM -> String
            preparedStatement.setString(9, offre.getRecruteurResponsable());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void modifier(Offre offre) throws SQLException {
        String sql = "UPDATE `offre_emploi` SET `titrePoste` = ?, `description` = ?, `datePublication` = ?, `dateAcceptation` = ?, `timeToHire` = ?, `timeToFill` = ?, `statuOffre` = ?, `departement` = ?, `recruteurResponsable` = ? "
                + "WHERE `idOffre` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, offre.getTitrePoste());
            preparedStatement.setString(2, offre.getDescription());
            preparedStatement.setDate(3, offre.getDatePublication());
            preparedStatement.setDate(4, offre.getDateAcceptation());
            preparedStatement.setInt(5, offre.getTimeToHire());
            preparedStatement.setInt(6, offre.getTimeToFill());
            preparedStatement.setString(7, offre.getStatuOffre().name()); // ENUM -> String
            preparedStatement.setString(8, offre.getDepartement()); // ENUM -> String
            preparedStatement.setString(9, offre.getRecruteurResponsable());
            preparedStatement.setInt(10, offre.getIdOffre());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("L'offre a été mise à jour avec succès !");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID.");
            }
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `offre_emploi` WHERE `idOffre` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id); // ID de l'offre à supprimer

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("L'offre a été supprimée avec succès !");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID.");
            }
        }
    }


    @Override
    public List<Offre> afficher() throws SQLException {
        List<Offre> offres = new ArrayList<>();
        String sql = "SELECT * FROM `offre_emploi`";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                // ✅ Gérer les erreurs et éviter le crash
                String statutDB = rs.getString("statuOffre").trim().toUpperCase();
                Offre.StatutOffre statut;
                try {
                    statut = Offre.StatutOffre.valueOf(statutDB);
                } catch (IllegalArgumentException e) {
                    System.err.println("⚠ Erreur : Statut inconnu '" + statutDB + "'. Assignation à 'EN_COURS' par défaut.");
                    statut = Offre.StatutOffre.EN_COURS; // Valeur par défaut
                }

                String departement = rs.getString("departement"); // ✅ Récupération du département sans erreur

                Offre offre = new Offre(
                        rs.getString("titrePoste"),
                        rs.getString("description"),
                        rs.getDate("datePublication"),
                        rs.getDate("dateAcceptation"),
                        rs.getInt("timeToHire"),
                        rs.getInt("timeToFill"),
                        statut, // ✅ Maintenant sûr de ne pas crasher
                        departement,
                        rs.getString("recruteurResponsable")
                );
                offre.setIdOffre(rs.getInt("idOffre")); // Set the ID of the offer
                offres.add(offre);
            }
        }
        return offres;
    }
}


