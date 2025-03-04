package tn.esprit.tache.services;

import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetService implements IProjetService {
    private Connection conn = DBConnection.getInstance().getConnection();

    @Override
    public void ajouterProjet(Projet projet) {
        String sql = "INSERT INTO projet (nom_projet, desc_projet, statut_projet, date_debut_projet, date_fin_projet) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, projet.getNomProjet());
            ps.setString(2, projet.getDescProjet());
            ps.setString(3, projet.getStatutProjet());
            ps.setDate(4, new Date(projet.getDateDebutProjet().getTime()));
            ps.setDate(5, new Date(projet.getDateFinProjet().getTime()));
            ps.executeUpdate();
            System.out.println("✅ Projet ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Projet> getAllProjets() {
        List<Projet> projets = new ArrayList<>();
        String sql = "SELECT * FROM projet";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projets.add(new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"), rs.getString("desc_projet"),
                        rs.getString("statut_projet"), rs.getDate("date_debut_projet"), rs.getDate("date_fin_projet")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projets;
    }

    @Override
    public void modifierProjet(Projet projet) {
        String sql = "UPDATE projet SET nom_projet=?, desc_projet=?, statut_projet=?, date_debut_projet=?, date_fin_projet=? WHERE id_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, projet.getNomProjet());
            ps.setString(2, projet.getDescProjet());
            ps.setString(3, projet.getStatutProjet());
            ps.setDate(4, new Date(projet.getDateDebutProjet().getTime()));
            ps.setDate(5, new Date(projet.getDateFinProjet().getTime()));
            ps.setInt(6, projet.getIdProjet());
            ps.executeUpdate();
            System.out.println("✅ Projet modifié avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerProjet(int idProjet) {
        String sql = "DELETE FROM projet WHERE id_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProjet);
            ps.executeUpdate();
            System.out.println("✅ Projet supprimé.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Projet getProjetById(int idProjet) {
        String sql = "SELECT * FROM projet WHERE id_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProjet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"), rs.getString("desc_projet"),
                        rs.getString("statut_projet"), rs.getDate("date_debut_projet"), rs.getDate("date_fin_projet"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Projet getProjetByName(String nomProjet) {
        String sql = "SELECT * FROM projet WHERE nom_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomProjet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"), rs.getString("desc_projet"),
                        rs.getString("statut_projet"), rs.getDate("date_debut_projet"), rs.getDate("date_fin_projet"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
