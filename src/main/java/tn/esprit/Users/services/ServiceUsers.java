package tn.esprit.Users.services;

import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.utils.Base;
import org.mindrot.jbcrypt.BCrypt;
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
        String sql = "INSERT INTO `user` (`iyedNomUser`, `iyedEmailUser`, `iyedPhoneUser`, `iyedPasswordUser`, `iyedRoleUser`, `iyedPositionUser`, `iyedSalaireUser`, `iyedDateEmbaucheUser`, `iyedStatutUser`, `iyedIdDepUser`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getIyedNomUser());
            preparedStatement.setString(2, user.getIyedEmailUser());
            preparedStatement.setString(3, user.getIyedPhoneUser());
            preparedStatement.setString(4, user.getIyedPasswordUser()); // New
            preparedStatement.setString(5, user.getIyedRoleUser().name());
            preparedStatement.setString(6, user.getIyedPositionUser());
            preparedStatement.setDouble(7, user.getIyedSalaireUser());
            java.util.Date embaucheDate = user.getIyedDateEmbaucheUser();
            if (embaucheDate == null) {
                embaucheDate = new java.util.Date(System.currentTimeMillis()); // Default to current date
            }

            preparedStatement.setDate(8, new java.sql.Date(embaucheDate.getTime()));
            preparedStatement.setString(9, user.getIyedStatutUser().name());
            preparedStatement.setObject(10, (user.getIyedDepartment() != null) ? user.getIyedDepartment().getIyedIdDep() : null);
            System.out.println("🔑 Stored Hashed Password: " + user.getIyedPasswordUser());



        int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("SQL Query: " + preparedStatement.toString());


            // Check if the email already exists in the database
            if (rowsAffected == 0) {
                throw new SQLException("Email already exists in the database.");
            }

            // Get the generated user ID
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setIyedIdUser(rs.getInt(1));  // Set the new user ID
                }
            }
        } catch (SQLException e) {
            // Handle unique email constraint violation (usually error code 1062 for MySQL)
            if (e.getSQLState().equals("23000")) {
                // Unique constraint violation for email
                throw new SQLException("Email is already taken. Please use a different one.");
            } else {
                throw e; // Rethrow other SQLExceptions
            }
        }
    }




    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE `user` SET `iyedNomUser`=?, `iyedEmailUser`=?, `iyedPhoneUser`=?, `iyedPasswordUser`=?, `iyedRoleUser`=?, `iyedPositionUser`=?, `iyedSalaireUser`=?, `iyedDateEmbaucheUser`=?, `iyedStatutUser`=?, `iyedIdDepUser`=? WHERE `iyedIdUser`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getIyedNomUser());
            preparedStatement.setString(2, user.getIyedEmailUser());
            preparedStatement.setString(3, user.getIyedPhoneUser());
            preparedStatement.setString(4, user.getIyedPasswordUser()); // New
            preparedStatement.setString(5, user.getIyedRoleUser().name());
            preparedStatement.setString(6, user.getIyedPositionUser());
            preparedStatement.setDouble(7, user.getIyedSalaireUser());
            preparedStatement.setDate(8, new java.sql.Date(user.getIyedDateEmbaucheUser().getTime()));
            preparedStatement.setString(9, user.getIyedStatutUser().name());
            preparedStatement.setObject(10, (user.getIyedDepartment() != null) ? user.getIyedDepartment().getIyedIdDep() : null);
            preparedStatement.setInt(11, user.getIyedIdUser());



        int rowsAffected = preparedStatement.executeUpdate();
            String checkSql = "SELECT iyedPasswordUser FROM `user` WHERE iyedEmailUser = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, user.getIyedEmailUser());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHashedPassword = rs.getString("iyedPasswordUser");
                        System.out.println("🔑 Stored Hashed Password in DB: " + storedHashedPassword);
                    }
                }
            }
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

                // Create User object with all 11 arguments (including password)
                User user = new User(
                        rs.getInt("iyedIdUser"),
                        rs.getString("iyedNomUser"),
                        rs.getString("iyedEmailUser"),
                        rs.getString("iyedPhoneUser"),
                        rs.getString("iyedPasswordUser"), // Added missing password field
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
                    rs.getString("iyedPasswordUser"), // New
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
                rs.getString("iyedPasswordUser"), // New
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



    public User login(String email, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE iyedEmailUser = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("iyedPasswordUser");

                    // Debugging: Print passwords
                    System.out.println("Entered Password: " + plainPassword);
                    System.out.println("Hashed password from DB: " + hashedPassword);

                    // ✅ Verify password
                    if (BCrypt.checkpw(plainPassword, hashedPassword)) {
                        String email1 = rs.getString("iyedEmailUser");
                        String name = rs.getString("iyedNomUser");
                        String phone = rs.getString("iyedPhoneUser");
                        int departmentId = rs.getInt("iyedIdDepUser");

                        // Fetch department details based on departmentId
                        Department department = fetchDepartment(departmentId);

                        // Create the User object with the department included
                        return new User(
                                rs.getInt("iyedIdUser"),
                                name,
                                email1,
                                phone,
                                hashedPassword,  // Hashed password from the database
                                UserRole.valueOf(rs.getString("iyedRoleUser")),
                                rs.getString("iyedPositionUser"),
                                rs.getDouble("iyedSalaireUser"),
                                rs.getDate("iyedDateEmbaucheUser"),
                                UserStatus.valueOf(rs.getString("iyedStatutUser")),
                                department // Pass the department object
                        );
                    } else {
                        System.out.println("❌ Password mismatch");
                    }
                }
            }
        }

        return null; // ❌ No user found or password incorrect
    }

    // Helper method to fetch department details based on departmentId
    private Department fetchDepartment(int departmentId) throws SQLException {
        String sql = "SELECT * FROM `department` WHERE iyedIdDep = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, departmentId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("iyedIdDep"),
                            rs.getString("iyedNomDep"),
                            rs.getString("iyedDescriptionDep"),
                            rs.getString("iyedLocationDep"),
                            new ArrayList<>(),
                            null // Assuming manager information is null
                    );
                }
            }
        }
        return null; // Department not found
    }





    public boolean isEmailTaken(String email) {
        String query = "SELECT COUNT(*) FROM `user` WHERE `iyedEmailUser` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email); // Set the email parameter

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count > 0, email is already taken
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
        return false; // Email not found
    }




}

