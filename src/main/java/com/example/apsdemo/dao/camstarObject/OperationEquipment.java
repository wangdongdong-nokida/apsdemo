package com.example.apsdemo.dao.camstarObject;

import javax.persistence.*;

@Entity
@Table(name = "A_Operaion_Equipment")
public class OperationEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.EAGER)
    private WorkStepName workStepName;

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public WorkStepName getWorkStepName() {
        return workStepName;
    }

    public void setWorkStepName(WorkStepName workStepName) {
        this.workStepName = workStepName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationEquipment)) return false;
        OperationEquipment that = (OperationEquipment) o;
        return getID() == that.getID();
    }
}
