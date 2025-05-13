package tn.esprit.tache.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "affectation")
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAffectation;

    // Relationship with Tache (Task)
    @ManyToOne
    @JoinColumn(name = "id_tache", nullable = false)  // The foreign key column name in the database
    private Tache tache;

    // Relationship with User (Employee)
    @ManyToOne
    @JoinColumn(name = "id_emp", nullable = false)  // The foreign key column name in the database
    private User employe;

    @Temporal(TemporalType.DATE)
    private Date dateAffectation;

    // Default Constructor (Required by JPA)
    public Affectation() {}

    // New Constructor to Fix the Error (Matches SQL Query)
    public Affectation(int idAffectation, int idEmp, int idTache, Date dateAffectation) {
        this.idAffectation = idAffectation;
        this.employe = new User();  // Initialize an empty User object
        this.employe.setIyedIdUser(idEmp);  // Set employee ID manually

        this.tache = new Tache();  // Initialize an empty Tache object
        this.tache.setIdTache(idTache);  // Set task ID manually

        this.dateAffectation = dateAffectation;
    }

    // Getters and Setters
    public int getId() { return idAffectation; }
    public void setId(int idAffectation) { this.idAffectation = idAffectation; }

    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }

    public User getEmploye() { return employe; }
    public void setEmploye(User employe) { this.employe = employe; }

    public Date getDateAffectation() { return dateAffectation; }
    public void setDateAffectation(Date dateAffectation) { this.dateAffectation = dateAffectation; }

    // Helper methods to retrieve ID from related entities
    public int getIdAffectation() {
        return idAffectation;
    }

    public int getIdEmp() {
        return (employe != null) ? employe.getIyedIdUser() : -1;  // Return employee ID or -1 if employe is null
    }

    public int getIdTache() {
        return (tache != null) ? tache.getIdTache() : -1;  // Return task ID or -1 if tache is null
    }

}
