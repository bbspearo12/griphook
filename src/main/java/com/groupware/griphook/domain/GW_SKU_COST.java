package com.groupware.griphook.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.groupware.griphook.domain.enumeration.GW_SKU;

/**
 * A GW_SKU_COST.
 */
@Entity
@Table(name = "gw_sku_cost")
public class GW_SKU_COST implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sku")
    private GW_SKU sku;

    @Column(name = "jhi_cost")
    private Float cost;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GW_SKU getSku() {
        return sku;
    }

    public GW_SKU_COST sku(GW_SKU sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(GW_SKU sku) {
        this.sku = sku;
    }

    public Float getCost() {
        return cost;
    }

    public GW_SKU_COST cost(Float cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
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
        GW_SKU_COST gW_SKU_COST = (GW_SKU_COST) o;
        if (gW_SKU_COST.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gW_SKU_COST.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GW_SKU_COST{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
