package tn.esprit.Users.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Base {
    private static Base instance;
    private Connection connection;

    private Base() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserDB", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Base getInstance() {
        if (instance == null) {
            instance = new Base();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) { // Reconnect if closed
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserDB", "root", "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
