package ma.enset.tp5.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_salles_reunion")
public class SalleReunion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salle")
    private Long id;

    @Column(name = "code_espace", unique = true, nullable = false, length = 30)
    private String codeEspace;

    @Column(name = "libelle_espace", nullable = false, length = 100)
    private String nomEspace;

    @Column(name = "capacite_assise")
    private Integer capacite;

    @Column(name = "niveau_etage")
    private Integer etage;

    @Column(name = "possede_visio")
    private Boolean visioConferenceDisponible;

    @OneToMany(mappedBy = "salleReservee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservationCreneau> reservations = new ArrayList<>();

    public SalleReunion() {
    }

    public SalleReunion(String codeEspace, String nomEspace, Integer capacite, Integer etage, Boolean visioConferenceDisponible) {
        this.codeEspace = codeEspace;
        this.nomEspace = nomEspace;
        this.capacite = capacite;
        this.etage = etage;
        this.visioConferenceDisponible = visioConferenceDisponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeEspace() {
        return codeEspace;
    }

    public void setCodeEspace(String codeEspace) {
        this.codeEspace = codeEspace;
    }

    public String getNomEspace() {
        return nomEspace;
    }

    public void setNomEspace(String nomEspace) {
        this.nomEspace = nomEspace;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Integer getEtage() {
        return etage;
    }

    public void setEtage(Integer etage) {
        this.etage = etage;
    }

    public Boolean getVisioConferenceDisponible() {
        return visioConferenceDisponible;
    }

    public void setVisioConferenceDisponible(Boolean visioConferenceDisponible) {
        this.visioConferenceDisponible = visioConferenceDisponible;
    }

    public List<ReservationCreneau> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationCreneau> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "SalleReunion{" +
                "id=" + id +
                ", code='" + codeEspace + '\'' +
                ", nom='" + nomEspace + '\'' +
                ", capacite=" + capacite +
                ", etage=" + etage +
                ", visio=" + visioConferenceDisponible +
                '}';
    }
}
