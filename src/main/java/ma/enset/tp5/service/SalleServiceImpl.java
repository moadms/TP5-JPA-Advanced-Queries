package ma.enset.tp5.service;

import ma.enset.tp5.model.SalleReunion;
import ma.enset.tp5.repository.SalleRepository;
import ma.enset.tp5.repository.SalleRepositoryImpl;
import ma.enset.tp5.util.PaginationResult;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class SalleServiceImpl implements SalleService {
    private final SalleRepository repository;

    public SalleServiceImpl(EntityManagerFactory emf) {
        this.repository = new SalleRepositoryImpl(emf);
    }

    @Override
    public void ajouterSalle(SalleReunion salle) {
        repository.save(salle);
    }

    @Override
    public SalleReunion obtenirSalle(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<SalleReunion> obtenirToutesLesSalles() {
        return repository.findAll();
    }

    @Override
    public PaginationResult<SalleReunion> rechercherAvecPagination(int page, int size) {
        return repository.findSallesPaginees(page, size);
    }

    @Override
    public List<SalleReunion> rechercherMultiCriteres(String batiment, Integer minCapacite, Boolean avecEcran) {
        return repository.findSallesMultiCriteres(batiment, minCapacite, avecEcran);
    }
}
