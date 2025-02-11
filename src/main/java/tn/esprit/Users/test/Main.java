package tn.esprit.Users.test;

import tn.esprit.Users.entities.User;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.utils.Base;

import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Initialize the database connection
        Base db = Base.getInstance();

        // Create an instance of ServiceUsers
        ServiceUsers su = new ServiceUsers();

        try {
            // Add a new user
            su.ajouter(new User(
                    1,                          // id_emp
                    "John Doe",                  // nom_emp
                    "john.doe@example.com",      // email
                    "123456789",                 // phone
                    "Developer",                // role
                    "Software Engineer",         // position
                    5000.0,                      // salaire
                    Date.valueOf("2025-01-01"),  // date_embauche
                    "Active",                   // statut_emp
                    101                         // id_dep
            ));
            System.out.println("User added successfully!");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
}