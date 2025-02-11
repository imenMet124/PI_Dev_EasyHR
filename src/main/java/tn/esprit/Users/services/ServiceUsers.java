package tn.esprit.Users.services;

import tn.esprit.Users.entities.User;
import tn.esprit.Users.utils.Base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUsers implements IService<User> {
    private Connection connection;

    public ServiceUsers() {
        connection = Base.getInstance().getConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String sql = "INSERT INTO `user`(`id_emp`, `nom_emp`, `email`, `phone`, `role`, `position`, `salaire`, `date_embauche`, `statut_emp`, `id_dep`) "
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
        preparedStatement.setInt(10, user.getIdDep());

        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE `user` SET `nom_emp` = ?, `email` = ?, `phone` = ?, `role` = ?, `position` = ?, `salaire` = ?, `date_embauche` = ?, `statut_emp` = ?, `id_dep` = ? "
                + "WHERE `id_emp` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getNomEmp());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPhone());
        preparedStatement.setString(4, user.getRole());
        preparedStatement.setString(5, user.getPosition());
        preparedStatement.setDouble(6, user.getSalaire());
        preparedStatement.setDate(7, new java.sql.Date(user.getDateEmbauche().getTime()));
        preparedStatement.setString(8, user.getStatutEmp());
        preparedStatement.setInt(9, user.getIdDep());
        preparedStatement.setInt(10, user.getIdEmp());

        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("L'utilisateur a été mis à jour avec succès !");
        } else {
            System.out.println("Aucun utilisateur trouvé avec cet ID.");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `user` WHERE `id_emp` = ?";

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
                    rs.getInt("id_emp"),
                    rs.getString("nom_emp"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getString("position"),
                    rs.getDouble("salaire"),
                    rs.getDate("date_embauche"),
                    rs.getString("statut_emp"),
                    rs.getInt("id_dep")
            ));
        }

        return users;
    }
}