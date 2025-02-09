package tn.esprit.Offres.entities;

import java.util.Date;

public class Offre {
    private int id_offre;
    private String titre_poste;
    private String description;
    private Date date_publication ;
    private Date date_acceptation;
    private int time_to_hire;
    private int time_to_fill;
    private String statu_offre;
    private String departement;
    private String recruteur_responsable;

    public Offre() {
    }

    public Offre(String titre_poste, String description, Date date_publication, Date date_acceptation, int time_to_hire, int time_to_fill, String statu_offre, String departement, String recruteur_responsable) {
        this.titre_poste = titre_poste;
        this.description = description;
        this.date_publication = date_publication;
        this.date_acceptation = date_acceptation;
        this.time_to_hire = time_to_hire;
        this.time_to_fill = time_to_fill;
        this.statu_offre = statu_offre;
        this.departement = departement;
        this.recruteur_responsable = recruteur_responsable;
    }

    public Offre(int id_offre, String titre_poste, String description, Date date_publication, Date date_acceptation, int time_to_hire, int time_to_fill, String statu_offre, String departement, String recruteur_responsable) {
        this.id_offre = id_offre;
        this.titre_poste = titre_poste;
        this.description = description;
        this.date_publication = date_publication;
        this.date_acceptation = date_acceptation;
        this.time_to_hire = time_to_hire;
        this.time_to_fill = time_to_fill;
        this.statu_offre = statu_offre;
        this.departement = departement;
        this.recruteur_responsable = recruteur_responsable;
    }

    public int getId_offre() {
        return id_offre;
    }

    public String getTitre_poste() {
        return titre_poste;
    }

    public java.sql.Date getDate_publication() {
        return (java.sql.Date) (java.util.Date) date_publication;
    }

    public String getDescription() {
        return description;
    }

    public java.sql.Date getDate_acceptation() {
        return (java.sql.Date) (java.util.Date) date_acceptation;
    }

    public int getTime_to_hire() {
        return time_to_hire;
    }

    public int getTime_to_fill() {
        return time_to_fill;
    }

    public String getStatu_offre() {
        return statu_offre;
    }

    public String getDepartement() {
        return departement;
    }

    public String getRecruteur_responsable() {
        return recruteur_responsable;
    }

    public void setId(int id_offre) {
        this.id_offre = id_offre;
    }

    public void setTitre_poste(String titre_poste) {
        this.titre_poste = titre_poste;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate_publication(Date date_publication) {
        this.date_publication = date_publication;
    }

    public void setDate_acceptation(Date date_acceptation) {
        this.date_acceptation = date_acceptation;
    }

    public void setTime_to_hire(int time_to_hire) {
        this.time_to_hire = time_to_hire;
    }

    public void setTime_to_fill(int time_to_fill) {
        this.time_to_fill = time_to_fill;
    }

    public void setStatu_offre(String statu_offre) {
        this.statu_offre = statu_offre;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public void setRecruteur_responsable(String recruteur_responsable) {
        this.recruteur_responsable = recruteur_responsable;
    }

    @Override
    public String toString() {
        return "Offres{" +
                "id=" + id_offre +
                ", titre_poste='" + titre_poste + '\'' +
                ", description='" + description + '\'' +
                ", date_publication=" + date_publication +
                ", date_acceptation=" + date_acceptation +
                ", time_to_hire=" + time_to_hire +
                ", time_to_fill=" + time_to_fill +
                ", statu_offre='" + statu_offre + '\'' +
                ", departement='" + departement + '\'' +
                ", recruteur_responsable='" + recruteur_responsable + '\'' +
                '}';
    }
}
