package tn.esprit.formations.model;

import tn.esprit.formations.entities.User;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;

    private UserSession() {
        // Private constructor to prevent instantiation
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void clearSession() {
        loggedInUser = null;
    }
}