package tn.esprit.tache.services;

import tn.esprit.tache.entities.Affectation;
import java.util.List;

public interface IAffectationService {
    void affecterEmployeTache(int idEmp, int idTache);
    void retirerEmployeTache(int idEmp, int idTache);
    List<Affectation> getAffectationsByEmploye(int idEmp);
    List<Affectation> getAffectationsByTache(int idTache);

    List<Affectation> getAllAffectations();

}
