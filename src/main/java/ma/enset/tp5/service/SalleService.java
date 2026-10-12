package ma.enset.tp5.service;

import ma.enset.tp5.model.SalleReunion;
import ma.enset.tp5.util.PaginationResult;

import java.util.List;

public interface SalleService {
    void ajouterSalle(SalleReunion salle);
    SalleReunion obtenirSalle(Long id);
    List<SalleReunion> obtenirToutesLesSalles();
    PaginationResult<SalleReunion> rechercherAvecPagination(int page, int size);
    List<SalleReunion> rechercherMultiCriteres(String batiment, Integer minCapacite, Boolean avecEcran);
}
