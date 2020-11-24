package com.example.apsdemo.dao.camstarObject;

import javax.persistence.*;


@Entity
@Table(name = "WORKFLOWSTEP")
public class WorkStep {
    @Id
    @Column(name = "WORKFLOWSTEPID")
    private String ID;

    @Column(name = "SEQUENCE")
    private String SEQUENCE;

    @ManyToOne(targetEntity = WorkStepName.class)
    @JoinColumn(name = "SPECBASEID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WorkStepName workStepName;

    @JoinColumn(name = "WORKFLOWID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(targetEntity = WorkFlow.class)
    private WorkFlow workFlow;

    public String getSEQUENCE() {
        return SEQUENCE;
    }

    public void setSEQUENCE(String SEQUENCE) {
        this.SEQUENCE = SEQUENCE;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }

    public WorkStepName getWorkStepName() {
        return workStepName;
    }

    public void setWorkStepName(WorkStepName workStepName) {
        this.workStepName = workStepName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
