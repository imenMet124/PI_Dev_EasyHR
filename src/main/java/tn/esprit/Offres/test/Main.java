package tn.esprit.Offres.test;

import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.services.ServiceOffres;
import tn.esprit.Offres.utils.Base;
import tn.esprit.evenement.utils.MyDataBase;

import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Base db = Base.getInstance();
        ServiceOffres so = new ServiceOffres();
        try {
            so.ajouter(new Offre("dev java", "hello hello hello",
                    Date.valueOf("2025-12-12"), // Date de publication
                    Date.valueOf("2025-12-12"), // Date d'acceptation
                    3, 4, "yes", "yes", "yes"));
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        }
}
