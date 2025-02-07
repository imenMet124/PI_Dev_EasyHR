package tn.esprit.evenement.services;

import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements IService<Evenement> {
    private Connection connection;

    public ServiceEvenement() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Evenement evenement) throws SQLException {
        String sql = "INSERT INTO `evenement`(`titre`, `description`, `date`, `lieu`, `capacite`) VALUES (?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, evenement.getTitre());
        ps.setString(2, evenement.getDescription());
        ps.setTimestamp(3, evenement.getDate());
        ps.setString(4, evenement.getLieu());
        ps.setInt(5, evenement.getCapacite());
        ps.executeUpdate();
    }

    @Override
    public void modifier(Evenement evenement) throws SQLException {
        String sql = "UPDATE `evenement` SET `titre`=?, `description`=?, `date`=?, `lieu`=?, `capacite`=? WHERE `id`=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, evenement.getTitre());
        ps.setString(2, evenement.getDescription());
        ps.setTimestamp(3, evenement.getDate());  // Utilisation de Timestamp pour gérer la date et l'heure
        ps.setString(4, evenement.getLieu());
        ps.setInt(5, evenement.getCapacite());
        ps.setInt(6, evenement.getId());
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
        String sql = "SELECT * FROM `evenement` WHERE `date` >= CURDATE()";  // Pour afficher les événements futurs
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            Evenement evenement = new Evenement(
                    rs.getInt("Id"),
                    rs.getString("Titre"),
                    rs.getString("Description"),
                    rs.getTimestamp("Date"),  // Utilisation de getTimestamp() pour récupérer un DATETIME
                    rs.getString("Lieu"),
                    rs.getInt("Capacite")
            );
            evenements.add(evenement);
        }
        return evenements;
    }
}
