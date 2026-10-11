package ma.enset.tp5;

import ma.enset.tp5.model.Participant;
import ma.enset.tp5.model.ReservationCreneau;
import ma.enset.tp5.model.SalleReunion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println(">>> DEMARRAGE TP5 - REQUETES AVANCEES JPA, MULTI-CRITERES & PAGINATION <<<");
        System.out.println("==========================================================================");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tp5_requetes_avancees");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Création d'un jeu de données de test riche
            Participant p1 = new Participant("Moad Moussaoui", "moussaoui9881@gmail.com", "IT / Cloud");
            Participant p2 = new Participant("Karima Bennani", "k.bennani@entreprise.ma", "Ressources Humaines");
            Participant p3 = new Participant("Tariq Mansouri", "t.mansouri@entreprise.ma", "Direction Générale");
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            SalleReunion s1 = new SalleReunion("SALLE-101", "Espace Turing", 15, 1, true);
            SalleReunion s2 = new SalleReunion("SALLE-102", "Espace Lovelace", 25, 1, true);
            SalleReunion s3 = new SalleReunion("SALLE-201", "Espace Dijkstra", 8, 2, false);
            SalleReunion s4 = new SalleReunion("SALLE-202", "Espace Shannon", 50, 2, true);
            SalleReunion s5 = new SalleReunion("SALLE-301", "Espace Hopper", 12, 3, false);
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(s4);
            em.persist(s5);

            LocalDateTime maintenant = LocalDateTime.now();

            // Réservation sur Salle 101 aujourd'hui entre 10h et 12h
            ReservationCreneau res1 = new ReservationCreneau(
                    "Revue Technique Sprint",
                    maintenant.withHour(10).withMinute(0),
                    maintenant.withHour(12).withMinute(0),
                    s1, p1
            );
            // Réservation sur Salle 102 aujourd'hui entre 14h et 16h
            ReservationCreneau res2 = new ReservationCreneau(
                    "Entretien Recrutement",
                    maintenant.withHour(14).withMinute(0),
                    maintenant.withHour(16).withMinute(0),
                    s2, p2
            );
            em.persist(res1);
            em.persist(res2);

            em.getTransaction().commit();
            System.out.println("✓ Jeu de données initialisé avec 5 salles et 2 réservations.");

            // -------------------------------------------------------------
            // [1] REQUETE JPQL : Salles disponibles sur un créneau horaire
            // -------------------------------------------------------------
            System.out.println("\n--- [1] RECHERCHE DES SALLES DISPONIBLES (JPQL AVEC PARAMETRES) ---");
            LocalDateTime testDebut = maintenant.withHour(10).withMinute(30);
            LocalDateTime testFin = maintenant.withHour(11).withMinute(30);

            String jpqlDispo = "SELECT s FROM SalleReunion s WHERE s.id NOT IN (" +
                    "   SELECT r.salleReservee.id FROM ReservationCreneau r " +
                    "   WHERE r.debut < :creneauFin AND r.fin > :creneauDebut" +
                    ")";

            List<SalleReunion> sallesDispos = em.createQuery(jpqlDispo, SalleReunion.class)
                    .setParameter("creneauDebut", testDebut)
                    .setParameter("creneauFin", testFin)
                    .getResultList();

            System.out.println("Salles disponibles entre " + testDebut.toLocalTime() + " et " + testFin.toLocalTime() + " :");
            for (SalleReunion s : sallesDispos) {
                System.out.println("  -> " + s.getCodeEspace() + " (" + s.getNomEspace() + ") - Capacité : " + s.getCapacite());
            }

            // -------------------------------------------------------------
            // [2] RECHERCHE DYNAMIQUE MULTI-CRITERES (CRITERIA API)
            // -------------------------------------------------------------
            System.out.println("\n--- [2] RECHERCHE DYNAMIQUE MULTI-CRITERES (CRITERIA API) ---");
            Integer capaciteMin = 10;
            Integer etageFiltre = null; // Optionnel
            Boolean besoinVisio = true;

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<SalleReunion> cq = cb.createQuery(SalleReunion.class);
            Root<SalleReunion> root = cq.from(SalleReunion.class);

            List<Predicate> predicats = new ArrayList<>();
            if (capaciteMin != null) {
                predicats.add(cb.ge(root.get("capacite"), capaciteMin));
            }
            if (etageFiltre != null) {
                predicats.add(cb.equal(root.get("etage"), etageFiltre));
            }
            if (besoinVisio != null) {
                predicats.add(cb.equal(root.get("visioConferenceDisponible"), besoinVisio));
            }

            cq.where(cb.and(predicats.toArray(new Predicate[0])));
            cq.orderBy(cb.asc(root.get("capacite")));

            List<SalleReunion> resultatsCriteria = em.createQuery(cq).getResultList();
            System.out.println("Critères : Capacité >= " + capaciteMin + ", Visio = " + besoinVisio);
            for (SalleReunion s : resultatsCriteria) {
                System.out.println("  -> " + s.getNomEspace() + " (Capacité : " + s.getCapacite() + ", Étage : " + s.getEtage() + ")");
            }

            // -------------------------------------------------------------
            // [3] PAGINATION AVEC SETFIRSTRESULT & SETMAXRESULTS
            // -------------------------------------------------------------
            System.out.println("\n--- [3] DEMONSTRATION DE LA PAGINATION ---");
            int pageSize = 2;
            int totalSalles = em.createQuery("SELECT COUNT(s) FROM SalleReunion s", Long.class).getSingleResult().intValue();
            int totalPages = (int) Math.ceil((double) totalSalles / pageSize);

            System.out.println("Total salles : " + totalSalles + " | Taille page : " + pageSize + " | Pages : " + totalPages);

            for (int page = 0; page < totalPages; page++) {
                System.out.println(" Page " + (page + 1) + " :");
                TypedQuery<SalleReunion> pageQuery = em.createQuery("SELECT s FROM SalleReunion s ORDER BY s.id", SalleReunion.class);
                pageQuery.setFirstResult(page * pageSize);
                pageQuery.setMaxResults(pageSize);

                List<SalleReunion> pageItems = pageQuery.getResultList();
                for (SalleReunion s : pageItems) {
                    System.out.println("    [" + s.getId() + "] " + s.getCodeEspace() + " - " + s.getNomEspace());
                }
            }

            System.out.println("\n==========================================================================");
            System.out.println(">>> EXECUTION DU TP5 TERMINEE AVEC SUCCES <<<");
            System.out.println("==========================================================================");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
