package tn.esprit.tache.entities;

import java.util.List;

public class Department {
    private int iyedIdDep;
    private String iyedNomDep;
    private String iyedDescriptionDep;
    private String iyedLocationDep;
    private List<User> iyedEmployees;  // List of employees in the department
    private User iyedManager;  // Manager of the department

    // Constructor
    public Department(int iyedIdDep, String iyedNomDep, String iyedDescriptionDep, String iyedLocationDep, List<User> iyedEmployees, User iyedManager) {
        this.iyedIdDep = iyedIdDep;
        this.iyedNomDep = iyedNomDep;
        this.iyedDescriptionDep = iyedDescriptionDep;
        this.iyedLocationDep = iyedLocationDep;
        this.iyedEmployees = iyedEmployees;
        this.iyedManager = iyedManager;
    }

    // Getters and Setters
    public int getIyedIdDep() {
        return iyedIdDep;
    }

    public void setIyedIdDep(int iyedIdDep) {
        this.iyedIdDep = iyedIdDep;
    }

    public String getIyedNomDep() {
        return iyedNomDep;
    }

    public void setIyedNomDep(String iyedNomDep) {
        this.iyedNomDep = iyedNomDep;
    }

    public String getIyedDescriptionDep() {
        return iyedDescriptionDep;
    }

    public void setIyedDescriptionDep(String iyedDescriptionDep) {
        this.iyedDescriptionDep = iyedDescriptionDep;
    }

    public String getIyedLocationDep() {
        return iyedLocationDep;
    }

    public void setIyedLocationDep(String iyedLocationDep) {
        this.iyedLocationDep = iyedLocationDep;
    }

    public List<User> getIyedEmployees() {
        return iyedEmployees;
    }

    public void setIyedEmployees(List<User> iyedEmployees) {
        this.iyedEmployees = iyedEmployees;
    }

    public User getIyedManager() {
        return iyedManager;
    }

    public void setIyedManager(User iyedManager) {
        this.iyedManager = iyedManager;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Department{" +
                "iyedIdDep=" + iyedIdDep +
                ", iyedNomDep='" + iyedNomDep + '\'' +
                ", iyedDescriptionDep='" + iyedDescriptionDep + '\'' +
                ", iyedLocationDep='" + iyedLocationDep + '\'' +
                ", iyedEmployees=" + iyedEmployees +
                ", iyedManager=" + iyedManager +
                '}';
    }
}