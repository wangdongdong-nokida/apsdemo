package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.LGyWlztData;

import javax.persistence.*;

@Entity
@Table(name = "L_GY_WLZT")
public class LGyWlzt extends LGyWlztData {

    private WorkFlow workFlow;


    private LWlzt lWlzt;

    @ManyToOne(targetEntity = LWlzt.class)
    @JoinColumn(name = "lWlztid")
    public LWlzt getlWlzt() {
        return lWlzt;
    }

    public void setlWlzt(LWlzt lWlzt) {
        this.lWlzt = lWlzt;
    }

    @ManyToOne(targetEntity = WorkFlow.class)
    @JoinColumn(name = "workflowid")
    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }
}
