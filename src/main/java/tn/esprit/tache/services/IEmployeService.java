package tn.esprit.tache.services;

import tn.esprit.tache.entities.Employe;
import java.util.List;

public interface IEmployeService {
    void ajouterEmploye(Employe employe);
    void modifierEmploye(Employe employe);
    void supprimerEmploye(int idEmp);
    Employe getEmployeById(int idEmp);
    List<Employe> getAllEmployes();
    List<Employe> getEmployesByDepartement(int idDep);
}
