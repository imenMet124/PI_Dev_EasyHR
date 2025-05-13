package tn.esprit.tache.entities;

import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private int iyedIdUser;
    private String iyedNomUser;
    private String iyedEmailUser;
    private String iyedPhoneUser;
    private UserRole iyedRoleUser;  // Enum for role
    private String iyedPositionUser;
    private double iyedSalaireUser;
    private Date iyedDateEmbaucheUser;
    private UserStatus iyedStatutUser;  // Enum for status
    private Department iyedDepartment;  // Reference to Department
    private String iyedPasswordUser;  // New attribute for password

    // Constructor with password
    public User(int iyedIdUser, String iyedNomUser, String iyedEmailUser, String iyedPhoneUser,
                String iyedPasswordUser, UserRole iyedRoleUser, String iyedPositionUser,
                double iyedSalaireUser, Date iyedDateEmbaucheUser, UserStatus iyedStatutUser,
                Department iyedDepartment) {
        this.iyedIdUser = iyedIdUser;
        this.iyedNomUser = iyedNomUser;
        this.iyedEmailUser = iyedEmailUser;
        this.iyedPhoneUser = iyedPhoneUser;
        this.iyedPasswordUser = iyedPasswordUser;
        this.iyedRoleUser = iyedRoleUser;
        this.iyedPositionUser = iyedPositionUser;
        this.iyedSalaireUser = iyedSalaireUser;
        this.iyedDateEmbaucheUser = iyedDateEmbaucheUser;
        this.iyedStatutUser = iyedStatutUser;
        this.iyedDepartment = iyedDepartment;
    }

    public User() {

    }

    // Method to hash the password before storing
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method to check if a given password matches the stored hash
    public boolean checkPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.iyedPasswordUser);
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

    public String getIyedPasswordUser() {
        return iyedPasswordUser;
    }

    public void setIyedPasswordUser(String iyedPasswordUser) {
        this.iyedPasswordUser = iyedPasswordUser;
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
                ", iyedDepartment=" + (iyedDepartment != null ? iyedDepartment.getIyedNomDep() : "No Department") +
                ", iyedPasswordUser='******'" + // Hide actual password in toString
                '}';
    }
}
