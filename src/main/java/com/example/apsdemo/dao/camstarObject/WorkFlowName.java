package com.example.apsdemo.dao.camstarObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workflowbase")
public class WorkFlowName {
    @Id
    @Column(name = "WORKFLOWBASEID")
    private String ID;

    @Column(name = "WORKFLOWNAME")
    private String workFlowName;

    @JsonIgnore
    @JoinColumn(name = "WORKFLOWBASEID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OneToMany(targetEntity = WorkFlow.class)
    private Set<WorkFlow> workFlows;


    public Set<WorkFlow> getWorkFlows() {
        return workFlows;
    }

    public void setWorkFlows(Set<WorkFlow> workFlows) {
        this.workFlows = workFlows;
    }

    public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
