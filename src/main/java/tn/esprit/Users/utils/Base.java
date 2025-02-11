package tn.esprit.Users.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Base {
    private final String URL = "jdbc:mysql://localhost:3306/UserDB"; // Database URL for User
    private final String USER = "root"; // Database username
    private final String PSW = ""; // Database password

    private Connection connection; // Database connection object
    private static Base instance; // Singleton instance

    // Private constructor to prevent instantiation from outside
    private Base() {
        try {
            // Establish the database connection
            connection = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("Connected to the User database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the User database: " + e.getMessage());
        }
    }

    // Singleton instance getter
    public static Base getInstance() {
        if (instance == null) {
            instance = new Base();
        }
        return instance;
    }

    // Getter for the database connection
    public Connection getConnection() {
        return connection;
    }
}