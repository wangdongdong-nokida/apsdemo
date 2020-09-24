package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.ScheduleTask;
import com.example.apsdemo.dao.camstarData.ContainerData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CONTAINER")
public class Container extends ContainerData {

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class)
    private ScheduleTask scheduleTask;

    @JsonIgnore
    @OneToMany(targetEntity = HistoryMainLine.class,fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "containerid",foreignKey = @ForeignKey(name = "null",value = ConstraintMode.NO_CONSTRAINT))
    private Set<HistoryMainLine> historyMainLines=new HashSet<>();

    @JsonIgnore
    @ManyToOne(targetEntity = CurrentStatus.class,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "currentstatusid",foreignKey = @ForeignKey(name = "null",value = ConstraintMode.NO_CONSTRAINT))
    private CurrentStatus currentStatus;

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Set<HistoryMainLine> getHistoryMainLines() {
        return historyMainLines;
    }

    public void setHistoryMainLines(Set<HistoryMainLine> historyMainLines) {
        this.historyMainLines = historyMainLines;
    }

    public ScheduleTask getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
    }
}
