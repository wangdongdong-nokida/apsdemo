package com.example.apsdemo.dao.camstarObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SPECBASE")
public class WorkStepName {
    @Id
    @Column(name = "SPECBASEID")
    private String ID;

    @Column(name = "SPECNAME")
    private String stepName;

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
