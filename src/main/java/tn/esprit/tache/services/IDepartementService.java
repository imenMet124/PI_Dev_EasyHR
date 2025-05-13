package tn.esprit.tache.services;

import tn.esprit.tache.entities.Departement;
import java.util.List;

public interface IDepartementService {
    void ajouterDepartement(Departement departement);
    void modifierDepartement(Departement departement);
    void supprimerDepartement(int idDep);
    Departement getDepartementById(int idDep);
    List<Departement> getAllDepartements();
}
