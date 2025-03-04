package tn.esprit.tache.services;

import tn.esprit.tache.entities.Affectation;
import tn.esprit.tache.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AffectationService implements IAffectationService {
    private Connection conn = DBConnection.getInstance().getConnection();

    @Override
    public void affecterEmployeTache(int idEmp, int idTache) {
        String sql = "INSERT INTO affectation (id_emp, id_tache, date_affectation) VALUES (?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);
            ps.setInt(2, idTache);
            ps.executeUpdate();
            System.out.println("✅ Employé affecté à la tâche avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Affectation> getAllAffectations() {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT a.*, e.nom_emp, t.titre_tache FROM affectation a " +
                "JOIN employe e ON a.id_emp = e.id_emp " +
                "JOIN tache t ON a.id_tache = t.id_tache";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Affectation affectation = new Affectation(
                        rs.getInt("id_affectation"),
                        rs.getInt("id_emp"),
                        rs.getInt("id_tache"),
                        rs.getDate("date_affectation")
                );
                affectations.add(affectation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }


    @Override
    public void retirerEmployeTache(int idEmp, int idTache) {
        String sql = "DELETE FROM affectation WHERE id_emp=? AND id_tache=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);
            ps.setInt(2, idTache);
            ps.executeUpdate();
            System.out.println("✅ Employé retiré de la tâche.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Affectation> getAffectationsByEmploye(int idEmp) {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM affectation WHERE id_emp=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                affectations.add(new Affectation(rs.getInt("id_affectation"), rs.getInt("id_emp"), rs.getInt("id_tache"), rs.getDate("date_affectation")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

    @Override
    public List<Affectation> getAffectationsByTache(int idTache) {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM affectation WHERE id_tache=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTache);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                affectations.add(new Affectation(rs.getInt("id_affectation"), rs.getInt("id_emp"), rs.getInt("id_tache"), rs.getDate("date_affectation")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectations;
    }

}
