package tn.esprit.Offres.services;

import tn.esprit.Offres.entities.User;
import tn.esprit.Offres.utils.Base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    private Connection connection;

    public ServiceUser() {
        connection = Base.getInstance().getConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String sql = "INSERT INTO `user`(`department`, `nomEmp`, `email`, `phone`, `role`, `position`, `salaire`, `dateEmbauche`, `statutEmp`, `department`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, user.getIdEmp());
        preparedStatement.setString(2, user.getNomEmp());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPhone());
        preparedStatement.setString(5, user.getRole());
        preparedStatement.setString(6, user.getPosition());
        preparedStatement.setDouble(7, user.getSalaire());
        preparedStatement.setDate(8, new java.sql.Date(user.getDateEmbauche().getTime()));
        preparedStatement.setString(9, user.getStatutEmp());
        preparedStatement.setString(10, user.getDepartment());

        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE `user` SET `nomEmp` = ?, `email` = ?, `phone` = ?, `role` = ?, `position` = ?, `salaire` = ?, `dateEmbauche` = ?, `statutEmp` = ?, `department` = ? "
                + "WHERE `department` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getNomEmp());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPhone());
        preparedStatement.setString(4, user.getRole());
        preparedStatement.setString(5, user.getPosition());
        preparedStatement.setDouble(6, user.getSalaire());
        preparedStatement.setDate(7, new java.sql.Date(user.getDateEmbauche().getTime()));
        preparedStatement.setString(8, user.getStatutEmp());
        preparedStatement.setString(9, user.getDepartment());


        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("L'utilisateur a été mis à jour avec succès !");
        } else {
            System.out.println("Aucun utilisateur trouvé avec cet ID.");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `user` WHERE `department` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        int rowsDeleted = preparedStatement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("L'utilisateur a été supprimé avec succès !");
        } else {
            System.out.println("Aucun utilisateur trouvé avec cet ID.");
        }
    }

    @Override
    public List<User> afficher() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM `user`";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            users.add(new User(
                    rs.getInt("department"),
                    rs.getString("nomEmp"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getString("position"),
                    rs.getDouble("salaire"),
                    rs.getDate("dateEmbauche"),
                    rs.getString("statutEmp"),
                    rs.getString("department")
            ));
        }

        return users;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE `department` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new User(
                    rs.getInt("department"),
                    rs.getString("nomEmp"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getString("position"),
                    rs.getDouble("salaire"),
                    rs.getDate("dateEmbauche"),
                    rs.getString("statutEmp"),
                    rs.getString("department")
            );
        } else {
            return null; // Aucun utilisateur trouvé avec cet ID
        }
    }
}