package tn.esprit.evenement.test;

import tn.esprit.evenement.entities.Evenement;
import tn.esprit.evenement.entities.Participation;
import tn.esprit.evenement.services.ServiceEvenement;
import tn.esprit.evenement.services.ServiceParticipation;
import tn.esprit.evenement.utils.MyDataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // Obtenir une instance de la base de données
        MyDataBase db = MyDataBase.getInstance();

        // Vérifier la connexion
//        Connection connection = db.getConnection();
//        if (connection != null) {
//            System.out.println("Connexion à la base de données réussie !");
//
//        } else {
//            System.out.println("Échec de la connexion !");
//        }



                // Créer une instance du service des événements
                ServiceEvenement serviceEvenement = new ServiceEvenement();

                // Ajouter un événement
//                Evenement evenement1 = new Evenement();
//                 Timestamp customDate = Timestamp.valueOf("2025-02-15 10:00:00");
//
//                 evenement1.setTitre(" workshop IA");
//                evenement1.setDescription("Une conférence sur les nouveautés IA.");
//                evenement1.setDate(customDate);
//                evenement1.setLieu(" ESPRIT");
//                evenement1.setCapacite(100);
//
//                try {
//                    serviceEvenement.ajouter(evenement1);
//                    System.out.println("Événement ajouté : " + evenement1);
//                } catch (SQLException e) {
//                    System.out.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
//                }

                // Modifier un événement
//        Timestamp newDate = Timestamp.valueOf("2025-02-20 14:00:00");
//
//        Evenement evenement2 = new Evenement();
//                evenement2.setId(1); // L'ID de l'événement à modifier
//                evenement2.setTitre("hackathon chess");
//                evenement2.setDescription("hgggfdsq");
//                evenement2.setDate(newDate); // Modifie la date
//                evenement2.setLieu("OPEN space");
//                evenement2.setCapacite(150);
//
//                try {
//                    serviceEvenement.modifier(evenement2);
//                    System.out.println("Événement modifié : " + evenement2);
//                } catch (SQLException e) {
//                    System.out.println("Erreur lors de la modification de l'événement : " + e.getMessage());
//                }

//                // Supprimer un événement
//                try {
//                    serviceEvenement.supprimer(3);  // ID de l'événement à supprimer
//                    System.out.println("Événement supprimé.");
//                } catch (SQLException e) {
//                    System.out.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
//                }
////
//                // Afficher les événements futurs
//                try {
//                    List<Evenement> evenements = serviceEvenement.afficher();
//                    System.out.println("Liste des événements futurs :");
//                    for (Evenement e : evenements) {
//                        System.out.println(e);
//                    }
//                } catch (SQLException e) {
//                    System.out.println("Erreur lors de l'affichage des événements : " + e.getMessage());
//                }
        ServiceParticipation service = new ServiceParticipation();

        // Ajouter une participation
//        Participation p1 = new Participation(1, 1, LocalDate.now(), "En attente");
//        service.ajouterParticipation(p1);

//        // Modifier le statut d'une participation
        //service.modifierStatut(2, "Confirmé");
//
//        // Récupérer les participations d'un utilisateur
        //System.out.println("Participations de l'utilisateur 3 : " + service.getParticipationsParUtilisateur(1));
//
//         Supprimer une participation
             service.supprimerParticipation(2);
            }
    }

