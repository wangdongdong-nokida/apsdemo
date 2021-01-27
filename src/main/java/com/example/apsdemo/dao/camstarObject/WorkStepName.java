package com.example.apsdemo.dao.camstarObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SPECBASE")
public class WorkStepName {
    @Id
    @Column(name = "SPECBASEID")
    private String ID;

    @Column(name = "SPECNAME")
    private String stepName;

    @Column(name = "gjgx")
    private boolean createOperation;

    public boolean isCreateOperation() {
        return createOperation;
    }

    public void setCreateOperation(boolean createOperation) {
        this.createOperation = createOperation;
    }

    @JsonIgnore
    @OneToMany(targetEntity =WorkStep.class)
    @JoinColumn(name = "SPECBASEID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Set<WorkStep> workSteps;

    @JsonIgnore
    @OneToMany(mappedBy = "workStepName")
    private Set<OperationEquipment> operationEquipments=new HashSet<>();

    public Set<OperationEquipment> getOperationEquipments() {
        return operationEquipments;
    }

    public void setOperationEquipments(Set<OperationEquipment> operationEquipments) {
        this.operationEquipments = operationEquipments;
    }

    public Set<WorkStep> getWorkSteps() {
        return workSteps;
    }

    public void setWorkSteps(Set<WorkStep> workSteps) {
        this.workSteps = workSteps;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
