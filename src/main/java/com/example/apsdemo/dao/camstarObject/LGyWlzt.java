package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.LGyWlztData;

import javax.persistence.*;

@Entity
@Table(name = "L_GY_WLZT", schema = "INSITEDB", catalog = "")
public class LGyWlzt extends LGyWlztData {

    private WorkFlow workFlow;

    @ManyToOne(targetEntity = WorkFlow.class)
    @JoinColumn(name = "workflowid")
    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }
}
