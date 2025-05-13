package tn.esprit.tache.services;

import tn.esprit.tache.entities.Employe;
import tn.esprit.tache.entities.Departement;
import tn.esprit.tache.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeService implements IEmployeService {
    private Connection conn = DBConnection.getInstance().getConnection();

    @Override
    public void ajouterEmploye(Employe employe) {
        String sql = "INSERT INTO employe (nom_emp, email, role, position, date_embauche, statut_emp, id_dep) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, employe.getNomEmp());
            ps.setString(2, employe.getEmail());
            ps.setString(3, employe.getRole());
            ps.setString(4, employe.getPosition());
            ps.setDate(5, new Date(employe.getDateEmbauche().getTime()));
            ps.setString(6, employe.getStatutEmp());
            ps.setInt(7, employe.getDepartement().getIdDep());
            ps.executeUpdate();
            System.out.println("✅ Employé ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifierEmploye(Employe employe) {
        String sql = "UPDATE employe SET nom_emp=?, email=?, role=?, position=?, date_embauche=?, statut_emp=?, id_dep=? WHERE id_emp=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employe.getNomEmp());
            ps.setString(2, employe.getEmail());
            ps.setString(3, employe.getRole());
            ps.setString(4, employe.getPosition());
            ps.setDate(5, new Date(employe.getDateEmbauche().getTime()));
            ps.setString(6, employe.getStatutEmp());
            ps.setInt(7, employe.getDepartement().getIdDep());
            ps.setInt(8, employe.getIdEmp());
            ps.executeUpdate();
            System.out.println("✅ Employé modifié avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerEmploye(int idEmp) {
        String sql = "DELETE FROM employe WHERE id_emp=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);
            ps.executeUpdate();
            System.out.println("✅ Employé supprimé.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employe getEmployeById(int idEmp) {
        String sql = "SELECT e.*, d.nom_dep FROM employe e JOIN departement d ON e.id_dep = d.id_dep WHERE e.id_emp=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Departement dep = new Departement(rs.getInt("id_dep"), rs.getString("nom_dep"));
                return new Employe(rs.getInt("id_emp"), rs.getString("nom_emp"), rs.getString("email"),
                        rs.getString("role"), rs.getString("position"),
                        rs.getDate("date_embauche"), rs.getString("statut_emp"), dep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Employe> getAllEmployes() {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT e.*, d.nom_dep FROM employe e JOIN departement d ON e.id_dep = d.id_dep";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Departement dep = new Departement(rs.getInt("id_dep"), rs.getString("nom_dep"));
                Employe employe = new Employe(rs.getInt("id_emp"), rs.getString("nom_emp"), rs.getString("email"),
                        rs.getString("role"), rs.getString("position"),
                        rs.getDate("date_embauche"), rs.getString("statut_emp"), dep);
                employes.add(employe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    /**
     * ✅ Retrieves all employees from a specific department.
     */
    @Override
    public List<Employe> getEmployesByDepartement(int idDep) {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT e.* FROM employe e WHERE e.id_dep=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDep);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employe employe = new Employe(rs.getInt("id_emp"), rs.getString("nom_emp"), rs.getString("email"),
                        rs.getString("role"), rs.getString("position"),
                        rs.getDate("date_embauche"), rs.getString("statut_emp"), null);
                employes.add(employe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    public Map<Integer, String> getEmployeNames() {
        Map<Integer, String> employeMap = new HashMap<>();
        String sql = "SELECT id_emp, nom_emp FROM employe";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employeMap.put(rs.getInt("id_emp"), rs.getString("nom_emp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeMap;
    }


    public int getEmployeIdByName(String nomEmp) {
        String sql = "SELECT id_emp FROM employe WHERE nom_emp=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomEmp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_emp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retourne -1 si non trouvé
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM employe WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Email existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Email n'existe pas
    }


}
