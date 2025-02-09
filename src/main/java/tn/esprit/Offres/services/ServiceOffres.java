package tn.esprit.Offres.services;


import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.utils.Base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffres implements IService<Offre>{
    private Connection connection;
    public ServiceOffres(){
        connection = Base.getInstance().getConnection();
    }


    @Override
    public void ajouter(Offre offre) throws SQLException {
        String sql = "INSERT INTO `offre_emploi`(`titre_poste`, `description`, `date_publication`, `date_acceptation`, `time_to_hire`, `time_to_fill`, `statu_offre`, `departement`, `recruteur_responsable`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, offre.getTitre_poste());
        preparedStatement.setString(2, offre.getDescription());
        preparedStatement.setDate(3, offre.getDate_publication());  // Si c'est un Date (java.sql.Date)
        preparedStatement.setDate(4, offre.getDate_acceptation());
        preparedStatement.setInt(5, offre.getTime_to_hire());  // Si c'est un entier
        preparedStatement.setInt(6, offre.getTime_to_fill());
        preparedStatement.setString(7, offre.getStatu_offre());
        preparedStatement.setString(8, offre.getDepartement());
        preparedStatement.setString(9, offre.getRecruteur_responsable());

        preparedStatement.executeUpdate();  // Exécuter l'insertion
}

        @Override
    public void modifier(Offre offre) throws SQLException {
            String sql = "UPDATE `offre_emploi` SET `titre_poste` = ?, `description` = ?, `date_publication` = ?, `date_acceptation` = ?, `time_to_hire` = ?, `time_to_fill` = ?, `statu_offre` = ?, `departement` = ?, `recruteur_responsable` = ? "
                    + "WHERE `id_offre` = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, offre.getTitre_poste());
            preparedStatement.setString(2, offre.getDescription());
            preparedStatement.setDate(3, offre.getDate_publication());
            preparedStatement.setDate(4, offre.getDate_acceptation());
            preparedStatement.setInt(5, offre.getTime_to_hire());
            preparedStatement.setInt(6, offre.getTime_to_fill());
            preparedStatement.setString(7, offre.getStatu_offre());
            preparedStatement.setString(8, offre.getDepartement());
            preparedStatement.setString(9, offre.getRecruteur_responsable());
            preparedStatement.setInt(10, offre.getId_offre());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("L'offre a été mise à jour avec succès !");
            } else {
                System.out.println("Aucune offre trouvée avec cet ID.");
            }
        }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `offre_emploi` WHERE `id_offre` = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id); // ID de l'offre à supprimer

        int rowsDeleted = preparedStatement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("L'offre a été supprimée avec succès !");
        } else {
            System.out.println("Aucune offre trouvée avec cet ID.");
        }

    }

    @Override
    public List<Offre> afficher() throws SQLException {
        List<Offre> offres = new ArrayList<>();
        String sql = " SELECT * FROM `offre_emploi`";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            offres.add(new Offre(
                    rs.getInt("id_offre"),
                    rs.getString("titre_poste"),
                    rs.getString("description"),
                    rs.getDate("date_publication"),
                    rs.getDate("date_acceptation"),
                    rs.getInt("time_to_hire"),
                    rs.getInt("time_to_fill"),
                    rs.getString("statu_offre"),
                    rs.getString("departement"),
                    rs.getString("recruteur_responsable")
            ));
        }

        return offres ;
    }
}
