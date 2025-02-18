package tn.esprit.Users.services;

import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.utils.Base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUsers implements IService<User> {
    private final Connection connection;

    public ServiceUsers() {
        connection = Base.getInstance().getConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String sql = "INSERT INTO `user` (`iyedNomUser`, `iyedEmailUser`, `iyedPhoneUser`, `iyedRoleUser`, `iyedPositionUser`, `iyedSalaireUser`, `iyedDateEmbaucheUser`, `iyedStatutUser`, `iyedIdDepUser`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getIyedNomUser());
            preparedStatement.setString(2, user.getIyedEmailUser());
            preparedStatement.setString(3, user.getIyedPhoneUser());
            preparedStatement.setString(4, user.getIyedRoleUser().name());
            preparedStatement.setString(5, user.getIyedPositionUser());
            preparedStatement.setDouble(6, user.getIyedSalaireUser());
            preparedStatement.setDate(7, new java.sql.Date(user.getIyedDateEmbaucheUser().getTime()));
            preparedStatement.setString(8, user.getIyedStatutUser().name());
            preparedStatement.setObject(9, (user.getIyedDepartment() != null) ? user.getIyedDepartment().getIyedIdDep() : null);

            preparedStatement.executeUpdate();

            // Get the generated user ID
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setIyedIdUser(rs.getInt(1));  // Set the new user ID
                }
            }
        }
    }



    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE `user` SET `iyedNomUser`=?, `iyedEmailUser`=?, `iyedPhoneUser`=?, `iyedRoleUser`=?, `iyedPositionUser`=?, `iyedSalaireUser`=?, `iyedDateEmbaucheUser`=?, `iyedStatutUser`=?, `iyedIdDepUser`=? WHERE `iyedIdUser`=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getIyedNomUser());
            preparedStatement.setString(2, user.getIyedEmailUser());
            preparedStatement.setString(3, user.getIyedPhoneUser());
            preparedStatement.setString(4, user.getIyedRoleUser().name());
            preparedStatement.setString(5, user.getIyedPositionUser());
            preparedStatement.setDouble(6, user.getIyedSalaireUser());
            preparedStatement.setDate(7, new java.sql.Date(user.getIyedDateEmbaucheUser().getTime()));
            preparedStatement.setString(8, user.getIyedStatutUser().name());
            preparedStatement.setObject(9, (user.getIyedDepartment() != null) ? user.getIyedDepartment().getIyedIdDep() : null);
            preparedStatement.setInt(10, user.getIyedIdUser());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("L'utilisateur a été mis à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée pour l'utilisateur !");
            }
        }
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `user` WHERE `iyedIdUser` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("L'utilisateur a été supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        }
    }

    @Override
    public List<User> afficher() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, d.iyedNomDep, d.iyedDescriptionDep, d.iyedLocationDep " +
                "FROM `user` u " +
                "LEFT JOIN `department` d ON u.iyedIdDepUser = d.iyedIdDep";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                // Create Department object
                Department department = new Department(
                        rs.getInt("iyedIdDepUser"),
                        rs.getString("iyedNomDep"),
                        rs.getString("iyedDescriptionDep"),
                        rs.getString("iyedLocationDep"),
                        new ArrayList<>(),  // Employees list not fetched here
                        null  // Manager not fetched here
                );

                // Create User object
                User user = new User(
                        rs.getInt("iyedIdUser"),
                        rs.getString("iyedNomUser"),
                        rs.getString("iyedEmailUser"),
                        rs.getString("iyedPhoneUser"),
                        UserRole.valueOf(rs.getString("iyedRoleUser")),  // Convert string to enum
                        rs.getString("iyedPositionUser"),
                        rs.getDouble("iyedSalaireUser"),
                        rs.getDate("iyedDateEmbaucheUser"),
                        UserStatus.valueOf(rs.getString("iyedStatutUser")),  // Convert string to enum
                        department  // Assign department object
                );

                users.add(user);
            }
        }

        return users;
    }
    public User getById(int id) throws SQLException {
        String query = "SELECT u.*, d.iyedNomDep, d.iyedIdDep " +
                "FROM user u " +
                "LEFT JOIN department d ON u.iyedIdDepUser = d.iyedIdDep " +
                "WHERE u.iyedIdUser = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Department department = rs.getInt("iyedIdDep") != 0 ?
                    new Department(rs.getInt("iyedIdDep"), rs.getString("iyedNomDep"), null, null, null, null) : null;

            return new User(
                    rs.getInt("iyedIdUser"),
                    rs.getString("iyedNomUser"),
                    rs.getString("iyedEmailUser"),
                    rs.getString("iyedPhoneUser"),
                    UserRole.valueOf(rs.getString("iyedRoleUser")),
                    rs.getString("iyedPositionUser"),
                    rs.getDouble("iyedSalaireUser"),
                    rs.getDate("iyedDateEmbaucheUser"),
                    UserStatus.valueOf(rs.getString("iyedStatutUser")),
                    department
            );
        }
        return null;
    }

    private User buildUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("iyedIdUser"),
                rs.getString("iyedNomUser"),
                rs.getString("iyedEmailUser"),
                rs.getString("iyedPhoneUser"),
                UserRole.valueOf(rs.getString("iyedRoleUser")),  // Convert string to enum
                rs.getString("iyedPositionUser"),
                rs.getDouble("iyedSalaireUser"),
                rs.getDate("iyedDateEmbaucheUser"),
                UserStatus.valueOf(rs.getString("iyedStatutUser")),  // Convert string to enum
                new Department(rs.getInt("iyedIdDepUser"), null, null, null, new ArrayList<>(), null) // Only department ID
        );
    }
    public List<User> getUsersByDepartment(int departmentId) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM `user` WHERE `iyedIdDepUser` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, departmentId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    users.add(buildUserFromResultSet(rs));
                }
            }
        }

        return users;
    }
    public int getLastInsertedId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID() AS id";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;  // Return 0 if no ID is found
    }



    private static final String URL = "jdbc:mysql://localhost:3306/Base"; // Replace with your database URL
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = ""; // Replace with your database password

    public int getUserIdByName(String name) {
        String query = "SELECT iyedIdUser FROM user WHERE iyedNomUser = ?";
        int userId = -1; // Default value if not found

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the user name as a parameter
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the result (user id)
                    userId = rs.getInt("iyedIdUser");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }
    public List<String> getUserNames() {
        List<String> userNames = new ArrayList<>();
        String sql = "SELECT iyedNomUser FROM user";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                userNames.add(rs.getString("iyedNomUser"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userNames;
    }
    public User getUserByName(String name) {
        String query = "SELECT * FROM user WHERE iyedNomUser = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




}

