# TP 5 : Requêtes Avancées avec JPA - Salles Disponibles, Recherche Multi-Critères et Pagination

Ce projet met en pratique les techniques avancées d'interrogation de bases de données relationnelles avec **JPA 2.2** et **Hibernate ORM** :

---

## 1. Fonctionnalités Clés Implémentées

### 1. Recherche de Salles Disponibles par Créneau (JPQL avec paramètres temporels)
Détection dynamique des conflits d'horaires à l'aide d'une sous-requête de chevauchement :
```sql
SELECT s FROM SalleReunion s 
WHERE s.id NOT IN (
    SELECT r.salleReservee.id FROM ReservationCreneau r 
    WHERE r.debut < :creneauFin AND r.fin > :creneauDebut
)
```

### 2. Recherche Multi-critères Dynamique (JPA Criteria API)
Construction programmatique et type-safe de prédicats modulaires (`capaciteMin`, `etage`, `visioDisponible`) :
```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<SalleReunion> cq = cb.createQuery(SalleReunion.class);
Root<SalleReunion> root = cq.from(SalleReunion.class);

List<Predicate> predicats = new ArrayList<>();
if (capaciteMin != null) predicats.add(cb.ge(root.get("capacite"), capaciteMin));
if (besoinVisio != null) predicats.add(cb.equal(root.get("visioConferenceDisponible"), besoinVisio));

cq.where(cb.and(predicats.toArray(new Predicate[0])));
```

### 3. Pagination Efficace des Résultats
Contrôle du flux de pagination à l'aide de `setFirstResult(offset)` et `setMaxResults(pageSize)` générant les clauses SQL `LIMIT / OFFSET` :
```java
TypedQuery<SalleReunion> pageQuery = em.createQuery("SELECT s FROM SalleReunion s ORDER BY s.id", SalleReunion.class);
pageQuery.setFirstResult(page * pageSize);
pageQuery.setMaxResults(pageSize);
```

---

## 2. Compilation et Exécution

```bash
mvn clean compile exec:java "-Dexec.mainClass=ma.enset.tp5.App"
```
