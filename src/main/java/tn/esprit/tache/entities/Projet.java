package tn.esprit.tache.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projet")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProjet;

    @Column(nullable = false, length = 150)
    private String nomProjet;

    private String descProjet;
    private String statutProjet;

    @Temporal(TemporalType.DATE)
    private Date dateDebutProjet;

    @Temporal(TemporalType.DATE)
    private Date dateFinProjet;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tache> taches;

    // ✅ Default Constructor (Required for JPA)
    public Projet() {}

    // ✅ Constructor for Creating New Projects (Without ID)
    public Projet(String nomProjet, String descProjet, String statutProjet, Date dateDebutProjet, Date dateFinProjet) {
        this.nomProjet = nomProjet;
        this.descProjet = descProjet;
        this.statutProjet = statutProjet;
        this.dateDebutProjet = dateDebutProjet;
        this.dateFinProjet = dateFinProjet;
    }

    // ✅ Constructor to Fix the Error (Includes `idProjet` and `nomProjet`)
    public Projet(int idProjet, String nomProjet) {
        this.idProjet = idProjet;
        this.nomProjet = nomProjet;
    }

    // ✅ Full Constructor (All Attributes)
    public Projet(int idProjet, String nomProjet, String descProjet, String statutProjet, Date dateDebutProjet, Date dateFinProjet) {
        this.idProjet = idProjet;
        this.nomProjet = nomProjet;
        this.descProjet = descProjet;
        this.statutProjet = statutProjet;
        this.dateDebutProjet = dateDebutProjet;
        this.dateFinProjet = dateFinProjet;
    }

    // ✅ Getters and Setters
    public int getIdProjet() { return idProjet; }
    public void setIdProjet(int idProjet) { this.idProjet = idProjet; }

    public String getNomProjet() { return nomProjet; }
    public void setNomProjet(String nomProjet) { this.nomProjet = nomProjet; }

    public String getDescProjet() { return descProjet; }
    public void setDescProjet(String descProjet) { this.descProjet = descProjet; }

    public String getStatutProjet() { return statutProjet; }
    public void setStatutProjet(String statutProjet) { this.statutProjet = statutProjet; }

    public Date getDateDebutProjet() { return dateDebutProjet; }
    public void setDateDebutProjet(Date dateDebutProjet) { this.dateDebutProjet = dateDebutProjet; }

    public Date getDateFinProjet() { return dateFinProjet; }
    public void setDateFinProjet(Date dateFinProjet) { this.dateFinProjet = dateFinProjet; }
}
