package tn.esprit.tache.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "departement")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDep;

    @Column(nullable = false, length = 100)
    private String nomDep;

    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employe> employes;

    // ✅ Default Constructor (Needed by Hibernate)
    public Departement() {}

    // ✅ Constructor for creating a Departement with a name only
    public Departement(String nomDep) {
        this.nomDep = nomDep;
    }

    // ✅ 🔥 NEW Constructor to Fix the Error (Accepts idDep + nomDep)
    public Departement(int idDep, String nomDep) {
        this.idDep = idDep;
        this.nomDep = nomDep;
    }

    // ✅ Getters and Setters
    public int getIdDep() { return idDep; }
    public void setIdDep(int idDep) { this.idDep = idDep; }

    public String getNomDep() { return nomDep; }
    public void setNomDep(String nomDep) { this.nomDep = nomDep; }

    public List<Employe> getEmployes() { return employes; }
    public void setEmployes(List<Employe> employes) { this.employes = employes; }
}
