package com.example.apsdemo.dao.camstarObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "workflow")
public class WorkFlow {
    @Id
    @Column(name = "WORKFLOWID")
    private String ID;

    @JoinColumn(name = "WORKFLOWBASEID",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = WorkFlowName.class)
    private WorkFlowName workFlowName;

    @JsonIgnore
    @JoinColumn(name = "WORKFLOWID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OneToMany(targetEntity =  WorkStep.class,fetch = FetchType.LAZY)
    private Set<WorkStep> workSteps;

    public Set<WorkStep> getWorkSteps() {
        return workSteps;
    }

    public void setWorkSteps(Set<WorkStep> workSteps) {
        this.workSteps = workSteps;
    }

    public WorkFlowName getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(WorkFlowName workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
