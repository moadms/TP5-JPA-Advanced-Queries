package ma.enset.tp5.repository;

import ma.enset.tp5.model.SalleReunion;
import ma.enset.tp5.util.PaginationResult;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SalleRepositoryImpl implements SalleRepository {
    private final EntityManagerFactory emf;

    public SalleRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(SalleReunion salle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(salle);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public SalleReunion findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(SalleReunion.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalleReunion> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM SalleReunion s", SalleReunion.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PaginationResult<SalleReunion> findSallesPaginees(int page, int size) {
        EntityManager em = emf.createEntityManager();
        try {
            Long total = em.createQuery("SELECT COUNT(s) FROM SalleReunion s", Long.class).getSingleResult();
            List<SalleReunion> data = em.createQuery("SELECT s FROM SalleReunion s ORDER BY s.id", SalleReunion.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
            return new PaginationResult<>(data, page, size, total);
        } finally {
            em.close();
        }
    }

    @Override
    public List<SalleReunion> findSallesMultiCriteres(String batiment, Integer minCapacite, Boolean avecEcran) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<SalleReunion> cq = cb.createQuery(SalleReunion.class);
            Root<SalleReunion> root = cq.from(SalleReunion.class);
            List<Predicate> predicates = new ArrayList<>();

            if (batiment != null && !batiment.isEmpty()) {
                predicates.add(cb.equal(root.get("batiment"), batiment));
            }
            if (minCapacite != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("capacitePlaces"), minCapacite));
            }
            if (avecEcran != null) {
                predicates.add(cb.equal(root.get("ecranGeant"), avecEcran));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
