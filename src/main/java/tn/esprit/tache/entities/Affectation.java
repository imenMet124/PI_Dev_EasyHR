package tn.esprit.tache.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "affectation")
public class Affectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAffectation;

    @ManyToOne
    @JoinColumn(name = "id_tache", nullable = false)
    private Tache tache;

    @ManyToOne
    @JoinColumn(name = "id_emp", nullable = false)
    private Employe employe;

    @Temporal(TemporalType.DATE)
    private Date dateAffectation;

    // ✅ Default Constructor (Required for JPA)
    public Affectation() {}

    // ✅ 🔥 NEW Constructor to Fix the Error (Matches SQL Query)
    public Affectation(int idAffectation, int idEmp, int idTache, Date dateAffectation) {
        this.idAffectation = idAffectation;
        this.employe = new Employe();  // Initialize an empty Employe object
        this.employe.setIdEmp(idEmp);  // Set employee ID manually

        this.tache = new Tache();  // Initialize an empty Tache object
        this.tache.setIdTache(idTache);  // Set task ID manually

        this.dateAffectation = dateAffectation;
    }

    // ✅ Getters and Setters
    public int getId() { return idAffectation; }
    public void setId(int idAffectation) { this.idAffectation = idAffectation; }

    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    public Date getDateAffectation() { return dateAffectation; }
    public void setDateAffectation(Date dateAffectation) { this.dateAffectation = dateAffectation; }

    public int getIdAffectation() {
        return idAffectation;
    }

    public int getIdEmp() {
        return (employe != null) ? employe.getIdEmp() : -1;
    }

    public int getIdTache() {
        return (tache != null) ? tache.getIdTache() : -1;
    }

}
