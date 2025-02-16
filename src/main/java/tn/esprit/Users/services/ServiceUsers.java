package tn.esprit.Users.services;

import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
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
        String sql = "INSERT INTO `user`(`iyedIdUser`, `iyedNomUser`, `iyedEmailUser`, `iyedPhoneUser`, `iyedRoleUser`, `iyedPositionUser`, `iyedSalaireUser`, `iyedDateEmbaucheUser`, `iyedStatutUser`, `iyedIdDepUser`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, user.getIyedIdUser());
        preparedStatement.setString(2, user.getIyedNomUser());
        preparedStatement.setString(3, user.getIyedEmailUser());
        preparedStatement.setString(4, user.getIyedPhoneUser());
        preparedStatement.setString(5, user.getIyedRoleUser().name());  // Convert enum to string
        preparedStatement.setString(6, user.getIyedPositionUser());
        preparedStatement.setDouble(7, user.getIyedSalaireUser());
        preparedStatement.setDate(8, new java.sql.Date(user.getIyedDateEmbaucheUser().getTime()));
        preparedStatement.setString(9, user.getIyedStatutUser().name());  // Convert enum to string
        preparedStatement.setInt(10, user.getIyedIdDepUser());

        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE `user` SET `iyedNomUser` = ?, `iyedEmailUser` = ?, `iyedPhoneUser` = ?, `iyedRoleUser` = ?, `iyedPositionUser` = ?, `iyedSalaireUser` = ?, `iyedDateEmbaucheUser` = ?, `iyedStatutUser` = ?, `iyedIdDepUser` = ? "
                + "WHERE `iyedIdUser` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getIyedNomUser());
        preparedStatement.setString(2, user.getIyedEmailUser());
        preparedStatement.setString(3, user.getIyedPhoneUser());
        preparedStatement.setString(4, user.getIyedRoleUser().name());  // Convert enum to string
        preparedStatement.setString(5, user.getIyedPositionUser());
        preparedStatement.setDouble(6, user.getIyedSalaireUser());
        preparedStatement.setDate(7, new java.sql.Date(user.getIyedDateEmbaucheUser().getTime()));
        preparedStatement.setString(8, user.getIyedStatutUser().name());  // Convert enum to string
        preparedStatement.setInt(9, user.getIyedIdDepUser());
        preparedStatement.setInt(10, user.getIyedIdUser());

        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("L'utilisateur a été mis à jour avec succès !");
        } else {
            System.out.println("Aucun utilisateur trouvé avec cet ID.");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `user` WHERE `iyedIdUser` = ?";

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
                    rs.getInt("iyedIdUser"),
                    rs.getString("iyedNomUser"),
                    rs.getString("iyedEmailUser"),
                    rs.getString("iyedPhoneUser"),
                    UserRole.valueOf(rs.getString("iyedRoleUser")),  // Convert string to enum
                    rs.getString("iyedPositionUser"),
                    rs.getDouble("iyedSalaireUser"),
                    rs.getDate("iyedDateEmbaucheUser"),
                    UserStatus.valueOf(rs.getString("iyedStatutUser")),  // Convert string to enum
                    rs.getInt("iyedIdDepUser")
            ));
        }

        return users;
    }
}
