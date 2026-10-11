package ma.enset.tp5.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_participants")
public class Participant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participant")
    private Long id;

    @Column(name = "nom_prenom", nullable = false, length = 120)
    private String nomComplet;

    @Column(name = "email_contact", unique = true, nullable = false, length = 120)
    private String email;

    @Column(name = "departement", length = 80)
    private String departement;

    public Participant() {
    }

    public Participant(String nomComplet, String email, String departement) {
        this.nomComplet = nomComplet;
        this.email = email;
        this.departement = departement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nomComplet='" + nomComplet + '\'' +
                ", email='" + email + '\'' +
                ", departement='" + departement + '\'' +
                '}';
    }
}
