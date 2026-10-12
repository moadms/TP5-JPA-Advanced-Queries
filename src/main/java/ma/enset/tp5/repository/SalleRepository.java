package ma.enset.tp5.repository;

import ma.enset.tp5.model.SalleReunion;
import ma.enset.tp5.util.PaginationResult;

import java.util.List;

public interface SalleRepository {
    void save(SalleReunion salle);
    SalleReunion findById(Long id);
    List<SalleReunion> findAll();
    PaginationResult<SalleReunion> findSallesPaginees(int page, int size);
    List<SalleReunion> findSallesMultiCriteres(String batiment, Integer minCapacite, Boolean avecEcran);
}
