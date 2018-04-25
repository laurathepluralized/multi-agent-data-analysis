package edu.gatech.hpan.expmanager.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Experiment1.
 */
@Entity
@Table(name = "experiment1")
public class Experiment1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_index")
    private String index;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public Experiment1 index(String index) {
        this.index = index;
        return this;
    }

    public void setIndex(String index) {
        this.index = index;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Experiment1 experiment1 = (Experiment1) o;
        if (experiment1.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), experiment1.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Experiment1{" +
            "id=" + getId() +
            ", index='" + getIndex() + "'" +
            "}";
    }
}
