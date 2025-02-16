package tn.esprit.Users.test;

import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.Base;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize the database connection
        Base db = Base.getInstance();

        // Create an instance of ServiceUsers
        ServiceUsers su = new ServiceUsers();

        try {
            // Add a new user with the correct role and status enums
            su.ajouter(new User(
                    0,                             // Auto-generated ID
                    "Iyed Blick",                  // iyedNomUser
                    "iyed.blick@example.com",      // iyedEmailUser
                    "667788990",                   // iyedPhoneUser
                    UserRole.EMPLOYE,              // iyedRoleUser
                    "General Director",            // iyedPositionUser
                    12000.0,                       // iyedSalaireUser
                    Date.valueOf("2023-06-01"),    // iyedDateEmbaucheUser
                    UserStatus.ACTIVE,             // iyedStatutUser
                    144                            // iyedIdDepUser
            ));
            System.out.println("User added successfully!");

            // Test afficher to get and print the list of users
            List<User> users = su.afficher();
            if (users.isEmpty()) {
                System.out.println("No users found in the database.");
            } else {
                System.out.println("Users in the database:");
                for (User user : users) {
                    System.out.println("ID: " + user.getIyedIdUser() +
                            ", Name: " + user.getIyedNomUser() +
                            ", Email: " + user.getIyedEmailUser() +
                            ", Phone: " + user.getIyedPhoneUser() +
                            ", Role: " + user.getIyedRoleUser() +
                            ", Position: " + user.getIyedPositionUser() +
                            ", Salary: " + user.getIyedSalaireUser() +
                            ", Hire Date: " + user.getIyedDateEmbaucheUser() +
                            ", Status: " + user.getIyedStatutUser() +
                            ", Department ID: " + user.getIyedIdDepUser());
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
}
