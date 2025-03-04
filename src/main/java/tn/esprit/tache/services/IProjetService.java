package tn.esprit.tache.services;

import tn.esprit.tache.entities.Projet;
import java.util.List;

public interface IProjetService {
    void ajouterProjet(Projet projet);
    void modifierProjet(Projet projet);
    void supprimerProjet(int idProjet);
    Projet getProjetById(int idProjet);
    List<Projet> getAllProjets();

    Projet getProjetByName(String nomProjet);

}
