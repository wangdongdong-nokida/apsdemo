package com.example.apsdemo.dao.camstarObject;


import javax.persistence.*;

@Entity
@Table(name = "WORKFLOWSTEP")
public class WorkStep {
    @Id
    @Column(name = "WORKFLOWSTEPID")
    private String ID;

    @JoinColumn(name = "WORKFLOWID",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity =  WorkFlow.class)
    private WorkFlow workFlow;

    @ManyToOne
    @JoinColumn(name = "SPECBASEID",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private WorkStepName workStepName;

    public WorkStepName getWorkStepName() {
        return workStepName;
    }

    public void setWorkStepName(WorkStepName workStepName) {
        this.workStepName = workStepName;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
