package tn.esprit.Offres.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.Offres.entities.Offre;
import tn.esprit.Offres.services.ServiceOffres;

import java.sql.SQLException;
import java.util.List;

public class ViewOfferController {

    @FXML
    private ListView<Offre> offerListView;

    private final ServiceOffres serviceOffres = new ServiceOffres();

    @FXML
    public void initialize() {
        loadOffers();
    }

    public void loadOffers() {
        try {
            // Récupérer les offres à partir du service
            List<Offre> offres = serviceOffres.afficher(); // Correction : appel via instance

            // Créer une ObservableList pour lier avec la ListView
            ObservableList<Offre> observableOffres = FXCollections.observableArrayList(offres);

            // Définir les éléments de la ListView
            offerListView.setItems(observableOffres);

            // Définir le type de cellule personnalisée
            offerListView.setCellFactory(param -> new OffreCell());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cellule personnalisée pour afficher les informations des offres dans la ListView
    private static class OffreCell extends javafx.scene.control.ListCell<Offre> {
        @Override
        protected void updateItem(Offre item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getTitrePoste() + " - " + item.getDepartement());
            }
        }
    }
}
