package com.example.apsdemo.dao.camstarObject;

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
