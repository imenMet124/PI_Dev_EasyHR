package tn.esprit.evenement.services;

import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements IService<Evenement> {
    private Connection connection;

    public ServiceEvenement() {
        connection = MyDataBase.getInstance().getConnection();
    }

    // ✅ Ajouter un événement avec image et heure
    @Override
    public void ajouter(Evenement evenement) throws SQLException {
        String sql = "INSERT INTO `evenement`(`titre`, `description`, `date`, `heure`, `lieu`, `capacite`, `nombreParticipants`, `image_path`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1 , evenement.getTitre());
        ps.setString(2 , evenement.getDescription());
        ps.setDate(3 , evenement.getDate());
        ps.setTime(4 , evenement.getHeure()); // Nouveau champ heure
        ps.setString(5 , evenement.getLieu());
        ps.setInt(6 , evenement.getCapacite());
        ps.setInt(7 , evenement.getNombreParticipants());
        ps.setString(8, evenement.getImagePath()); // Nouveau champ image_path
        ps.executeUpdate();
    }

    @Override
    public void modifier(Evenement evenement) throws SQLException {
        String sql = "UPDATE `evenement` SET `titre`=?, `description`=?, `date`=?, `heure`=?, `lieu`=?, `capacite`=?, `image_path`=? WHERE `id`=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, evenement.getTitre());
        ps.setString(2, evenement.getDescription());
        ps.setDate(3, evenement.getDate());
        ps.setTime(4, evenement.getHeure());
        ps.setString(5, evenement.getLieu());
        ps.setInt(6, evenement.getCapacite());
        ps.setString(7, evenement.getImagePath());  // ✅ Ajout de l'image
        ps.setInt(8, evenement.getId());

        ps.executeUpdate();
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `evenement` WHERE `id`=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Evenement> afficher() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM `evenement`";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            Evenement evenement = new Evenement(
                    rs.getInt("Id"),
                    rs.getString("Titre"),
                    rs.getString("Description"),
                    rs.getDate("Date"),
                    rs.getTime("Heure"), // Ajout de l'heure
                    rs.getString("Lieu"),
                    rs.getInt("Capacite"),
                    rs.getInt("NombreParticipants"),
                    rs.getString("image_path") // Ajout de l'image
            );
            evenements.add(evenement);
        }
        return evenements;
    }

    public void incrementerParticipants(int evenementId) throws SQLException {
        String sql = "UPDATE `evenement` SET `nombreParticipants` = `nombreParticipants` + 1 WHERE `id` = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, evenementId);
        ps.executeUpdate();
    }
    public List<Evenement> getEventsByDay(LocalDate date) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM `evenement` WHERE DATE(`date`) = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDate(1, java.sql.Date.valueOf(date));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Evenement evenement = new Evenement(
                    rs.getInt("Id"),
                    rs.getString("Titre"),
                    rs.getString("Description"),
                    rs.getDate("Date"),
                    rs.getTime("Heure"),
                    rs.getString("Lieu"),
                    rs.getInt("Capacite"),
                    rs.getInt("NombreParticipants"),
                    rs.getString("image_path")
            );
            evenements.add(evenement);
        }
        return evenements;
    }
    public List<Evenement> getEventsByWeek(LocalDate startOfWeek) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();

        LocalDate endOfWeek = startOfWeek.plusDays(6);
        String sql = "SELECT * FROM evenement WHERE DATE(date) BETWEEN ? AND ? ORDER BY date, heure";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDate(1, java.sql.Date.valueOf(startOfWeek));
        ps.setDate(2, java.sql.Date.valueOf(endOfWeek));

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Evenement evenement = new Evenement(
                    rs.getInt("Id"),
                    rs.getString("Titre"),
                    rs.getString("Description"),
                    rs.getDate("Date"),
                    rs.getTime("Heure"),
                    rs.getString("Lieu"),
                    rs.getInt("Capacite"),
                    rs.getInt("NombreParticipants"),
                    rs.getString("image_path")
            );
            evenements.add(evenement);
        }
        return evenements;
    }



}
