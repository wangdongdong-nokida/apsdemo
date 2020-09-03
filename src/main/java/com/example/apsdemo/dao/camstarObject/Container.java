package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.ScheduleTask;
import com.example.apsdemo.dao.camstarData.ContainerData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.ibatis.annotations.One;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTAINER")
public class Container extends ContainerData {

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class)
    private ScheduleTask scheduleTask;

    public ScheduleTask getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
    }
}
