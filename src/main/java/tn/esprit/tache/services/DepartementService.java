package tn.esprit.tache.services;

import tn.esprit.tache.entities.Departement;
import tn.esprit.tache.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartementService implements IDepartementService {
    private Connection conn = DBConnection.getInstance().getConnection();

    @Override
    public void ajouterDepartement(Departement departement) {
        String sql = "INSERT INTO departement (nom_dep) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, departement.getNomDep());
            ps.executeUpdate();
            System.out.println("✅ Département ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Departement> getAllDepartements() {
        List<Departement> departements = new ArrayList<>();
        String sql = "SELECT * FROM departement";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departements.add(new Departement(rs.getInt("id_dep"), rs.getString("nom_dep")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departements;
    }

    @Override
    public void modifierDepartement(Departement departement) {
        String sql = "UPDATE departement SET nom_dep=? WHERE id_dep=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, departement.getNomDep());
            ps.setInt(2, departement.getIdDep());
            ps.executeUpdate();
            System.out.println("✅ Département modifié avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerDepartement(int idDep) {
        String sql = "DELETE FROM departement WHERE id_dep=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDep);
            ps.executeUpdate();
            System.out.println("✅ Département supprimé.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Departement getDepartementById(int idDep) {
        String sql = "SELECT * FROM departement WHERE id_dep=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDep);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Departement(rs.getInt("id_dep"), rs.getString("nom_dep"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Departement getDepartementByName(String nomDep) {
        String sql = "SELECT * FROM departement WHERE nom_dep=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomDep);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Departement(rs.getInt("id_dep"), rs.getString("nom_dep"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found
    }

}
