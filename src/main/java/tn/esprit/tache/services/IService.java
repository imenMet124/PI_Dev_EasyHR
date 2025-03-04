package tn.esprit.tache.services;

import java.util.List;

public interface IService<T> {
    void ajouter(T entity);
    void modifier(T entity);
    void supprimer(int id);
    T getById(int id);
    List<T> getAll();
}
