package com.groupware.griphook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Phase.
 */
@Entity
@Table(name = "phase")
public class Phase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sub_total")
    private Float subTotal;

    @Column(name = "sub_total_with_margin")
    private Float subTotalWithMargin;

    @ManyToOne
    private Project project;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "phase")
    @JsonIgnore
    private Set<Task> tasks = new HashSet<>();

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

    public Phase name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public Phase subTotal(Float subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Float getSubTotalWithMargin() {
        return subTotalWithMargin;
    }

    public Phase subTotalWithMargin(Float subTotalWithMargin) {
        this.subTotalWithMargin = subTotalWithMargin;
        return this;
    }

    public void setSubTotalWithMargin(Float subTotalWithMargin) {
        this.subTotalWithMargin = subTotalWithMargin;
    }

    public Project getProject() {
        return project;
    }

    public Phase project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Phase tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Phase addTask(Task task) {
        this.tasks.add(task);
        task.setPhase(this);
        return this;
    }

    public Phase removeTask(Task task) {
        this.tasks.remove(task);
        task.setPhase(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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
        Phase phase = (Phase) o;
        if (phase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), phase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Phase{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", subTotal=" + getSubTotal() +
            ", subTotalWithMargin=" + getSubTotalWithMargin() +
            "}";
    }
}
