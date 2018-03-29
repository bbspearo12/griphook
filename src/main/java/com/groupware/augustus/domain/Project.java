package com.groupware.augustus.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "accout_manager")
    private String accoutManager;

    @Column(name = "presales_architect")
    private String presalesArchitect;

    @Column(name = "default_project_margin")
    private Float defaultProjectMargin;

    @Column(name = "subcontract_project_margin")
    private Float subcontractProjectMargin;

    @Column(name = "pmpercentage")
    private Float pmpercentage;

    @Column(name = "risk")
    private Float risk;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Phase> phases = new HashSet<>();

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

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccoutManager() {
        return accoutManager;
    }

    public Project accoutManager(String accoutManager) {
        this.accoutManager = accoutManager;
        return this;
    }

    public void setAccoutManager(String accoutManager) {
        this.accoutManager = accoutManager;
    }

    public String getPresalesArchitect() {
        return presalesArchitect;
    }

    public Project presalesArchitect(String presalesArchitect) {
        this.presalesArchitect = presalesArchitect;
        return this;
    }

    public void setPresalesArchitect(String presalesArchitect) {
        this.presalesArchitect = presalesArchitect;
    }

    public Float getDefaultProjectMargin() {
        return defaultProjectMargin;
    }

    public Project defaultProjectMargin(Float defaultProjectMargin) {
        this.defaultProjectMargin = defaultProjectMargin;
        return this;
    }

    public void setDefaultProjectMargin(Float defaultProjectMargin) {
        this.defaultProjectMargin = defaultProjectMargin;
    }

    public Float getSubcontractProjectMargin() {
        return subcontractProjectMargin;
    }

    public Project subcontractProjectMargin(Float subcontractProjectMargin) {
        this.subcontractProjectMargin = subcontractProjectMargin;
        return this;
    }

    public void setSubcontractProjectMargin(Float subcontractProjectMargin) {
        this.subcontractProjectMargin = subcontractProjectMargin;
    }

    public Float getPmpercentage() {
        return pmpercentage;
    }

    public Project pmpercentage(Float pmpercentage) {
        this.pmpercentage = pmpercentage;
        return this;
    }

    public void setPmpercentage(Float pmpercentage) {
        this.pmpercentage = pmpercentage;
    }

    public Float getRisk() {
        return risk;
    }

    public Project risk(Float risk) {
        this.risk = risk;
        return this;
    }

    public void setRisk(Float risk) {
        this.risk = risk;
    }

    public Set<Phase> getPhases() {
        return phases;
    }

    public Project phases(Set<Phase> phases) {
        this.phases = phases;
        return this;
    }

    public Project addPhase(Phase phase) {
        this.phases.add(phase);
        phase.setProject(this);
        return this;
    }

    public Project removePhase(Phase phase) {
        this.phases.remove(phase);
        phase.setProject(null);
        return this;
    }

    public void setPhases(Set<Phase> phases) {
        this.phases = phases;
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
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", accoutManager='" + getAccoutManager() + "'" +
            ", presalesArchitect='" + getPresalesArchitect() + "'" +
            ", defaultProjectMargin=" + getDefaultProjectMargin() +
            ", subcontractProjectMargin=" + getSubcontractProjectMargin() +
            ", pmpercentage=" + getPmpercentage() +
            ", risk=" + getRisk() +
            "}";
    }
}
