package tn.esprit.tache.services;

import tn.esprit.tache.entities.Tache;
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TacheService implements ITacheService {
    private Connection conn = DBConnection.getInstance().getConnection();

    @Override
    public void ajouterTache(Tache tache) {
        String sql = "INSERT INTO tache (titre_tache, desc_tache, priorite, statut_tache, deadline, progression, id_projet) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tache.getTitreTache());
            ps.setString(2, tache.getDescTache());
            ps.setString(3, tache.getPriorite());
            ps.setString(4, tache.getStatutTache());
            ps.setDate(5, new Date(tache.getDeadline().getTime()));
            ps.setDouble(6, tache.getProgression());
            ps.setInt(7, tache.getProjet().getIdProjet());
            ps.executeUpdate();
            System.out.println("✅ Tâche ajoutée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierTache(Tache tache) {
        String sql = "UPDATE tache SET titre_tache=?, desc_tache=?, priorite=?, statut_tache=?, deadline=?, progression=?, id_projet=? " +
                "WHERE id_tache=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tache.getTitreTache());
            ps.setString(2, tache.getDescTache());
            ps.setString(3, tache.getPriorite());
            ps.setString(4, tache.getStatutTache());
            ps.setDate(5, new Date(tache.getDeadline().getTime()));
            ps.setDouble(6, tache.getProgression());
            ps.setInt(7, tache.getProjet().getIdProjet());
            ps.setInt(8, tache.getIdTache());
            ps.executeUpdate();
            System.out.println("✅ Tâche modifiée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerTache(int idTache) {
        String sql = "DELETE FROM tache WHERE id_tache=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTache);
            ps.executeUpdate();
            System.out.println("✅ Tâche supprimée.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tache getTacheById(int idTache) {
        String sql = "SELECT t.*, p.nom_projet FROM tache t JOIN projet p ON t.id_projet = p.id_projet WHERE t.id_tache=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTache);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Projet projet = new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"));
                return new Tache(rs.getInt("id_tache"), rs.getString("titre_tache"), rs.getString("desc_tache"),
                        rs.getString("priorite"), rs.getString("statut_tache"), rs.getDate("deadline"),
                        rs.getDouble("progression"), projet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Tache> getAllTaches() {
        List<Tache> taches = new ArrayList<>();
        String sql = "SELECT t.*, p.nom_projet FROM tache t JOIN projet p ON t.id_projet = p.id_projet";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Projet projet = new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"));
                Tache tache = new Tache(rs.getInt("id_tache"), rs.getString("titre_tache"), rs.getString("desc_tache"),
                        rs.getString("priorite"), rs.getString("statut_tache"), rs.getDate("deadline"),
                        rs.getDouble("progression"), projet);
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }

    @Override
    public List<Tache> getTachesByProjet(int idProjet) {
        List<Tache> taches = new ArrayList<>();
        String sql = "SELECT t.*, p.nom_projet FROM tache t JOIN projet p ON t.id_projet = p.id_projet WHERE t.id_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProjet);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Projet projet = new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"));
                Tache tache = new Tache(rs.getInt("id_tache"), rs.getString("titre_tache"), rs.getString("desc_tache"),
                        rs.getString("priorite"), rs.getString("statut_tache"), rs.getDate("deadline"),
                        rs.getDouble("progression"), projet);
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }


    public Map<Integer, String> getTacheNames() {
        Map<Integer, String> tacheMap = new HashMap<>();
        String sql = "SELECT id_tache, titre_tache FROM tache WHERE id_tache NOT IN (SELECT id_tache FROM affectation)";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tacheMap.put(rs.getInt("id_tache"), rs.getString("titre_tache"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tacheMap;
    }




}
