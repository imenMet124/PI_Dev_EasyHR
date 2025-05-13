package tn.esprit.tache.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employe")
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEmp;

    @Column(nullable = false, length = 100)
    private String nomEmp;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String role;  // ✅ Role stored directly in Employe

    private String position;

    @Temporal(TemporalType.DATE)
    private Date dateEmbauche;

    private String statutEmp;

    @ManyToOne
    @JoinColumn(name = "id_dep", nullable = false)
    private Departement departement;

    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Affectation> affectations;

    // ✅ Default constructor (Required by Hibernate)
    public Employe() {}

    // ✅ Constructor without ID (For creating new employees)
    public Employe(String nomEmp, String email, String role, String position, Date dateEmbauche, String statutEmp, Departement departement) {
        this.nomEmp = nomEmp;
        this.email = email;
        this.role = role;
        this.position = position;
        this.dateEmbauche = dateEmbauche;
        this.statutEmp = statutEmp;
        this.departement = departement;
    }

    // ✅ 🔥 NEW Constructor to Fix the Error (Includes `idEmp`)
    public Employe(int idEmp, String nomEmp, String email, String role, String position, Date dateEmbauche, String statutEmp, Departement departement) {
        this.idEmp = idEmp;
        this.nomEmp = nomEmp;
        this.email = email;
        this.role = role;
        this.position = position;
        this.dateEmbauche = dateEmbauche;
        this.statutEmp = statutEmp;
        this.departement = departement;
    }

    // ✅ Getters and Setters
    public int getIdEmp() { return idEmp; }
    public void setIdEmp(int idEmp) { this.idEmp = idEmp; }

    public String getNomEmp() { return nomEmp; }
    public void setNomEmp(String nomEmp) { this.nomEmp = nomEmp; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Date getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(Date dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getStatutEmp() { return statutEmp; }
    public void setStatutEmp(String statutEmp) { this.statutEmp = statutEmp; }

    public Departement getDepartement() { return departement; }
    public void setDepartement(Departement departement) { this.departement = departement; }
    public String getNomDepartement() {
        return (departement != null) ? departement.getNomDep() : "Aucun Département";
    }

}
