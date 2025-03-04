package tn.esprit.Users.test;
import java.sql.*;
public class testgfdg {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/UserDB"; // Replace with your database URL
        String user = "root"; // Replace with your database username
        String password = ""; // Replace with your database password
        String query = "SELECT iyedIdDep FROM department WHERE iyedNomDep = ?";

        // Replace with the department name you're looking for
        String departmentName = "IT";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the department name as a parameter
            stmt.setString(1, departmentName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the result
                    int iyedIdDep = rs.getInt("iyedIdDep");
                    System.out.println("Department ID: " + iyedIdDep);
                } else {
                    System.out.println("No department found with the name: " + departmentName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
