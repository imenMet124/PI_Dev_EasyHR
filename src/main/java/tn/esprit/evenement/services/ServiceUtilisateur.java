package tn.esprit.evenement.services;

import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.evenement.utils.MyDataBase;
import tn.esprit.evenement.entities.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur {
    private final Connection connection;

    public ServiceUtilisateur() {
        this.connection = MyDataBase.getInstance().getConnection();
    }


    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String req = "INSERT INTO utilisateur (Nom, Prenom, Email, Role) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, utilisateur.getNom());
        ps.setString(2, utilisateur.getPrenom());
        ps.setString(3, utilisateur.getEmail());
        ps.setString(4, utilisateur.getRole().name());

        ps.executeUpdate();
        System.out.println("✅ Utilisateur ajouté avec succès !");
    }


    public void modifierUtilisateur(Utilisateur utilisateur) throws SQLException {
        String req = "UPDATE utilisateur SET Nom=?, Prenom=?, Email=?, Role=? WHERE Id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setString(1, utilisateur.getNom());
        ps.setString(2, utilisateur.getPrenom());
        ps.setString(3, utilisateur.getEmail());
        ps.setString(4, utilisateur.getRole().name());
        ps.setInt(5, utilisateur.getId());

        ps.executeUpdate();
        System.out.println("✅ Utilisateur modifié avec succès !");
    }


    public void supprimerUtilisateur(int idUser) throws SQLException {
        String req = "DELETE FROM utilisateur WHERE Id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, idUser);

        ps.executeUpdate();
        System.out.println("✅ Utilisateur supprimé avec succès !");
    }


    public Utilisateur getUtilisateurById(int idUser) throws SQLException {
        String req = "SELECT * FROM utilisateur WHERE Id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, idUser);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Utilisateur(
                    rs.getInt("Id"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Email"),
                    Utilisateur.Role.valueOf(rs.getString("Role")) // Conversion en Enum
            );
        }
        return null;
    }

//
//    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
//        List<Utilisateur> utilisateurs = new ArrayList<>();
//        String req = "SELECT * FROM utilisateur";
//        Statement stmt = connection.createStatement();
//        ResultSet rs = stmt.executeQuery(req);
//
//        while (rs.next()) {
//            Utilisateur utilisateur = new Utilisateur(
//                    rs.getInt("Id"),
//                    rs.getString("Nom"),
//                    rs.getString("Prenom"),
//                    rs.getString("Email"),
//                    Utilisateur.Role.valueOf(rs.getString("Role")) // Conversion en Enum
//            );
//            utilisateurs.add(utilisateur);
//        }
//        return utilisateurs;
//    }
public List<User> getAllUtilisateurs() throws SQLException {
    List<User> utilisateurs = new ArrayList<>();
    String req = "SELECT * FROM user"; // Use the correct table name
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(req);

    while (rs.next()) {
        User user = new User(
                rs.getInt("iyedIdUser"), // Map the correct column names
                rs.getString("iyedNomUser"),
                rs.getString("iyedEmailUser"),
                rs.getString("iyedPhoneUser"),
                rs.getString("iyedPasswordUser"), // Include the password
                UserRole.valueOf(rs.getString("iyedRoleUser")), // Convert to enum
                rs.getString("iyedPositionUser"),
                rs.getDouble("iyedSalaireUser"),
                rs.getDate("iyedDateEmbaucheUser"),
                UserStatus.valueOf(rs.getString("iyedStatutUser")), // Convert to enum
                null // Department is not included in the query, set to null or fetch it separately
        );
        utilisateurs.add(user);
    }
    return utilisateurs;
}


    public Utilisateur getUtilisateurParId(int id) {
        String req = "SELECT * FROM utilisateur WHERE Id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Email"),
                        Utilisateur.Role.valueOf(rs.getString("Role")) // Convertit String en Enum Role
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

}
