package tn.esprit.Users.entities;

import java.util.Date;

public class User {
    private int iyedIdUser;
    private String iyedNomUser;
    private String iyedEmailUser;
    private String iyedPhoneUser;
    private UserRole iyedRoleUser;  // Enum for role
    private String iyedPositionUser;
    private double iyedSalaireUser;
    private Date iyedDateEmbaucheUser;
    private UserStatus iyedStatutUser;  // Assuming you have a UserStatus enum
    private Department iyedDepartment;  // Reference to Department

    // Constructor
    public User(int iyedIdUser, String iyedNomUser, String iyedEmailUser, String iyedPhoneUser, UserRole iyedRoleUser, String iyedPositionUser, double iyedSalaireUser, Date iyedDateEmbaucheUser, UserStatus iyedStatutUser, Department iyedDepartment) {
        this.iyedIdUser = iyedIdUser;
        this.iyedNomUser = iyedNomUser;
        this.iyedEmailUser = iyedEmailUser;
        this.iyedPhoneUser = iyedPhoneUser;
        this.iyedRoleUser = iyedRoleUser;
        this.iyedPositionUser = iyedPositionUser;
        this.iyedSalaireUser = iyedSalaireUser;
        this.iyedDateEmbaucheUser = iyedDateEmbaucheUser;
        this.iyedStatutUser = iyedStatutUser;
        this.iyedDepartment = iyedDepartment;
    }

    // Getters and Setters
    public int getIyedIdUser() {
        return iyedIdUser;
    }

    public void setIyedIdUser(int iyedIdUser) {
        this.iyedIdUser = iyedIdUser;
    }

    public String getIyedNomUser() {
        return iyedNomUser;
    }

    public void setIyedNomUser(String iyedNomUser) {
        this.iyedNomUser = iyedNomUser;
    }

    public String getIyedEmailUser() {
        return iyedEmailUser;
    }

    public void setIyedEmailUser(String iyedEmailUser) {
        this.iyedEmailUser = iyedEmailUser;
    }

    public String getIyedPhoneUser() {
        return iyedPhoneUser;
    }

    public void setIyedPhoneUser(String iyedPhoneUser) {
        this.iyedPhoneUser = iyedPhoneUser;
    }

    public UserRole getIyedRoleUser() {
        return iyedRoleUser;
    }

    public void setIyedRoleUser(UserRole iyedRoleUser) {
        this.iyedRoleUser = iyedRoleUser;
    }

    public String getIyedPositionUser() {
        return iyedPositionUser;
    }

    public void setIyedPositionUser(String iyedPositionUser) {
        this.iyedPositionUser = iyedPositionUser;
    }

    public double getIyedSalaireUser() {
        return iyedSalaireUser;
    }

    public void setIyedSalaireUser(double iyedSalaireUser) {
        this.iyedSalaireUser = iyedSalaireUser;
    }

    public Date getIyedDateEmbaucheUser() {
        return iyedDateEmbaucheUser;
    }

    public void setIyedDateEmbaucheUser(Date iyedDateEmbaucheUser) {
        this.iyedDateEmbaucheUser = iyedDateEmbaucheUser;
    }

    public UserStatus getIyedStatutUser() {
        return iyedStatutUser;
    }

    public void setIyedStatutUser(UserStatus iyedStatutUser) {
        this.iyedStatutUser = iyedStatutUser;
    }

    public Department getIyedDepartment() {
        return iyedDepartment;
    }

    public void setIyedDepartment(Department iyedDepartment) {
        this.iyedDepartment = iyedDepartment;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "User{" +
                "iyedIdUser=" + iyedIdUser +
                ", iyedNomUser='" + iyedNomUser + '\'' +
                ", iyedEmailUser='" + iyedEmailUser + '\'' +
                ", iyedPhoneUser='" + iyedPhoneUser + '\'' +
                ", iyedRoleUser='" + iyedRoleUser + '\'' +
                ", iyedPositionUser='" + iyedPositionUser + '\'' +
                ", iyedSalaireUser=" + iyedSalaireUser +
                ", iyedDateEmbaucheUser=" + iyedDateEmbaucheUser +
                ", iyedStatutUser='" + iyedStatutUser + '\'' +
                ", iyedDepartment=" + iyedDepartment.getIyedNomDep() +  // Display department name
                '}';
    }
}