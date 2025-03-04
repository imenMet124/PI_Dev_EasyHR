package tn.esprit.Users.model;

import tn.esprit.formations.entities.User;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;

    private UserSession() {
        // Private constructor for singleton
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        this.loggedInUser = null;
    }
}