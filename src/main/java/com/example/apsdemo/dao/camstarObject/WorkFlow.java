package com.example.apsdemo.dao.camstarObject;

import javax.persistence.*;

@Entity
@Table(name = "workflow")
public class WorkFlow {
    @Id
    @Column(name = "WORKFLOWID")
    private String ID;

    @JoinColumn(name = "WORKFLOWBASEID",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = WorkFlowName.class)
    private WorkFlowName workFlowName;


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
