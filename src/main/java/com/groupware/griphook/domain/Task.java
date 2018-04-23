package com.groupware.griphook.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.groupware.griphook.domain.enumeration.GW_SKU;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "estimated_hours")
    private Float estimatedHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_resource")
    private GW_SKU resource;

    @Column(name = "jhi_cost")
    private Float cost;

    @Column(name = "number_of_resources")
    private Integer numberOfResources;

    @Column(name = "sub_total")
    private Float subTotal;

    @ManyToOne
    private Phase phase;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Task name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getEstimatedHours() {
        return estimatedHours;
    }

    public Task estimatedHours(Float estimatedHours) {
        this.estimatedHours = estimatedHours;
        return this;
    }

    public void setEstimatedHours(Float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public GW_SKU getResource() {
        return resource;
    }

    public Task resource(GW_SKU resource) {
        this.resource = resource;
        return this;
    }

    public void setResource(GW_SKU resource) {
        this.resource = resource;
    }

    public Float getCost() {
        return cost;
    }

    public Task cost(Float cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Integer getNumberOfResources() {
        return numberOfResources;
    }

    public Task numberOfResources(Integer numberOfResources) {
        this.numberOfResources = numberOfResources;
        return this;
    }

    public void setNumberOfResources(Integer numberOfResources) {
        this.numberOfResources = numberOfResources;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public Task subTotal(Float subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Phase getPhase() {
        return phase;
    }

    public Task phase(Phase phase) {
        this.phase = phase;
        return this;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
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
        Task task = (Task) o;
        if (task.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", estimatedHours=" + getEstimatedHours() +
            ", resource='" + getResource() + "'" +
            ", cost=" + getCost() +
            ", numberOfResources=" + getNumberOfResources() +
            ", subTotal=" + getSubTotal() +
            "}";
    }
}
