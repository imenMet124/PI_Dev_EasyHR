package tn.esprit.tache.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "tache")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTache;

    @Column(nullable = false, length = 150)
    private String titreTache;

    private String descTache;
    private String priorite;
    private String statutTache;

    @Column(name = "deadline")
    private Date deadline; // ✅ Utilisation de java.sql.Date

    private double progression;

    @ManyToOne
    @JoinColumn(name = "id_projet", nullable = false)
    private Projet projet;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Affectation> affectations;

    // ✅ Constructeur par défaut (Obligatoire pour JPA)
    public Tache() {}

    // ✅ Constructeur pour création d'une nouvelle tâche
    public Tache(String titreTache, String descTache, String priorite, String statutTache, Date deadline, double progression, Projet projet) {
        this.titreTache = titreTache;
        this.descTache = descTache;
        this.priorite = priorite;
        this.statutTache = statutTache;
        this.deadline = deadline;
        this.progression = progression;
        this.projet = projet;
    }

    // ✅ Constructeur avec ID
    public Tache(int idTache, String titreTache, String descTache, String priorite, String statutTache, Date deadline, double progression, Projet projet) {
        this.idTache = idTache;
        this.titreTache = titreTache;
        this.descTache = descTache;
        this.priorite = priorite;
        this.statutTache = statutTache;
        this.deadline = deadline;
        this.progression = progression;
        this.projet = projet;
    }

    // ✅ Getters et Setters
    public int getIdTache() { return idTache; }
    public void setIdTache(int idTache) { this.idTache = idTache; }

    public String getTitreTache() { return titreTache; }
    public void setTitreTache(String titreTache) { this.titreTache = titreTache; }

    public String getDescTache() { return descTache; }
    public void setDescTache(String descTache) { this.descTache = descTache; }

    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }

    public String getStatutTache() { return statutTache; }
    public void setStatutTache(String statutTache) { this.statutTache = statutTache; }

    public Date getDeadline() { return deadline; }

    // ✅ Correction du setter pour convertir `java.util.Date` en `java.sql.Date`
    public void setDeadline(java.util.Date deadline) {
        if (deadline != null) {
            this.deadline = new Date(deadline.getTime());
        } else {
            this.deadline = null;
        }
    }

    public double getProgression() { return progression; }
    public void setProgression(double progression) { this.progression = progression; }

    public Projet getProjet() { return projet; }
    public void setProjet(Projet projet) { this.projet = projet; }
}