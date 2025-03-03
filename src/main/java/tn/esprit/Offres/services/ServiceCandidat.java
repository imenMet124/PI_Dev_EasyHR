package tn.esprit.Offres.services;

import tn.esprit.Offres.entities.Candidat;
import tn.esprit.Offres.entities.User;
import tn.esprit.Offres.utils.Base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCandidat implements IService<Candidat> {
    private Connection connection;

    public ServiceCandidat() {
        connection = Base.getInstance().getConnection();
    }

    @Override
    public  void ajouter(Candidat candidat) throws SQLException {
        String sql = "INSERT INTO `candidat`( `nomCandidat`, `prenomCandidat`, `emailCandidat`, `telephoneCandidat`, `posteActuel`, `departement`, `experienceInterne`, `competence`,  `disponibilite`,`id_user`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);


        preparedStatement.setString(1, candidat.getNom());
        preparedStatement.setString(2, candidat.getPrenom());
        preparedStatement.setString(3, candidat.getEmail());
        preparedStatement.setString(4, candidat.getPhone());
        preparedStatement.setString(5, candidat.getPosition());
        preparedStatement.setString(6, candidat.getDepartment());
        preparedStatement.setString(7, candidat.getExperienceInterne());
        preparedStatement.setString(8, candidat.getCompetence());

        preparedStatement.setString(9, candidat.getDisponibilite().name());
        preparedStatement.setInt(10, candidat.getUser().getIdEmp()); // Référence à l'utilisateur

        preparedStatement.executeUpdate();  // Exécuter l'insertion

        // Récupérer l'ID auto-incrémenté
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int idCandidat = generatedKeys.getInt(1); // Récupérer l'ID généré
            candidat.setIdCandidat(idCandidat); // Mettre à jour l'objet Candidat avec l'ID généré
        }
    }

    @Override
    public void modifier(Candidat candidat) throws SQLException {
        String sql = "UPDATE `candidat` SET `experienceInterne` = ?, `competence` = ?, `statuCandidat` = ?, `disponibilite` = ? "
                + "WHERE `idCandidat` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, candidat.getExperienceInterne());
        preparedStatement.setString(2, candidat.getCompetence());
        preparedStatement.setString(3, candidat.getStatuCandidat().name());
        preparedStatement.setString(4, candidat.getDisponibilite().name());
        preparedStatement.setInt(5, candidat.getIdCandidat());

        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Le candidat a été mis à jour avec succès !");
        } else {
            System.out.println("Aucun candidat trouvé avec cet ID.");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `candidat` WHERE `idCandidat` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id); // ID du candidat à supprimer

        int rowsDeleted = preparedStatement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Le candidat a été supprimé avec succès !");
        } else {
            System.out.println("Aucun candidat trouvé avec cet ID.");
        }
    }

    @Override
    public List<Candidat> afficher() throws SQLException {
        List<Candidat> candidats = new ArrayList<>();

        String sql = "SELECT idCandidat, nomCandidat, prenomCandidat, emailCandidat, telephoneCandidat, posteActuel, " +
                "departement, experienceInterne, competence, statuCandidat, disponibilite " +
                "FROM candidat";  // ✅ On ne sélectionne que les informations du candidat

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                // Création de l'objet Candidat **sans User**
                Candidat candidat = new Candidat(
                        rs.getInt("idCandidat"),
                        null, // ✅ On ne récupère pas User ici
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

                // Ajouter le candidat à la liste
                candidats.add(candidat);
            }
        }

        return candidats;
    }

    public Candidat getCandidatById(int idCandidat) {
        String sql = "SELECT * FROM candidat WHERE idCandidat = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idCandidat);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Candidat candidat = new Candidat();
                candidat.setIdCandidat(resultSet.getInt("idCandidat"));
                candidat.setNom(resultSet.getString("nomCandidat"));
                candidat.setPrenom(resultSet.getString("prenomCandidat"));
                candidat.setEmail(resultSet.getString("emailCandidat"));
                candidat.setPhone(resultSet.getString("telephoneCandidat"));
                candidat.setExperienceInterne(resultSet.getString("experienceInterne"));
                candidat.setCompetence(resultSet.getString("competence"));
                candidat.setDisponibilite(Candidat.Disponibilite.valueOf(resultSet.getString("disponibilite")));

                return candidat;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur SQL lors de la récupération du candidat : " + e.getMessage());
        }
        return null;
    }

}