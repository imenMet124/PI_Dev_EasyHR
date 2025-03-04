package tn.esprit.tache.services;

import tn.esprit.tache.entities.Tache;
import java.util.List;

public interface ITacheService {
    void ajouterTache(Tache tache);
    void modifierTache(Tache tache);
    void supprimerTache(int idTache);
    Tache getTacheById(int idTache);
    List<Tache> getAllTaches();
    List<Tache> getTachesByProjet(int idProjet);
}
