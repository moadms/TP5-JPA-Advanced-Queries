package ma.enset.tp5.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_reservations_creneaux")
public class ReservationCreneau implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long id;

    @Column(name = "titre_session", nullable = false)
    private String titre;

    @Column(name = "debut_horaire", nullable = false)
    private LocalDateTime debut;

    @Column(name = "fin_horaire", nullable = false)
    private LocalDateTime fin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_salle")
    private SalleReunion salleReservee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_organisateur")
    private Participant organisateur;

    public ReservationCreneau() {
    }

    public ReservationCreneau(String titre, LocalDateTime debut, LocalDateTime fin, SalleReunion salleReservee, Participant organisateur) {
        this.titre = titre;
        this.debut = debut;
        this.fin = fin;
        this.salleReservee = salleReservee;
        this.organisateur = organisateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public SalleReunion getSalleReservee() {
        return salleReservee;
    }

    public void setSalleReservee(SalleReunion salleReservee) {
        this.salleReservee = salleReservee;
    }

    public Participant getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(Participant organisateur) {
        this.organisateur = organisateur;
    }

    @Override
    public String toString() {
        return "ReservationCreneau{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", debut=" + debut +
                ", fin=" + fin +
                '}';
    }
}
