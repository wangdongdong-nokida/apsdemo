package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.ScheduleTaskData;
import com.example.apsdemo.dao.camstarObject.Container;
import com.example.apsdemo.logicSchedule.EquipmentCalendarBitSet;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "A_ScheduleTask")
@Inheritance(strategy = InheritanceType.JOINED)
public class ScheduleTask extends ScheduleTaskData {

    public ScheduleTask() {
    }

    public ScheduleTask(ScheduleTaskLine scheduleTaskLine, ScheduleTestItem scheduleTestItem, int durationTime) {
        this.scheduleTestItem = scheduleTestItem;
        setParmas(scheduleTaskLine, durationTime);
    }

    public ScheduleTask(ScheduleTaskLine scheduleTaskLine, ScheduleScribingItem scheduleScribing, int durationTime) {
        this.scheduleScribingItem = scheduleScribing;
        setParmas(scheduleTaskLine, durationTime);
    }

    private void setParmas(ScheduleTaskLine scheduleTaskLine, int durationTime) {
        this.scheduleTaskLine = scheduleTaskLine;
        this.setDurationTime(durationTime);
    }

    @JsonIgnore
    @OneToOne(targetEntity = Container.class,mappedBy = "scheduleTask",cascade = CascadeType.REMOVE)
    private Container container;

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private ScheduleTask son;

    @JsonIgnore
    @ManyToOne(targetEntity = ScheduleTaskLine.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private ScheduleTaskLine scheduleTaskLine;

    @OneToOne(targetEntity = ScheduleTestItem.class, cascade = CascadeType.ALL,mappedBy = "scheduleTask")
    private ScheduleTestItem scheduleTestItem;

    @OneToOne(targetEntity = ScheduleScribingItem.class, cascade = CascadeType.ALL,mappedBy = "scheduleTask")
    private ScheduleScribingItem scheduleScribingItem;


    public ScheduleScribingItem getScheduleScribingItem() {
        return scheduleScribingItem;
    }

    public void setScheduleScribingItem(ScheduleScribingItem scheduleScribing) {
        this.scheduleScribingItem = scheduleScribing;
    }




    //    public ScheduleTask getFather() {
//        return father;
//    }
//
//    public void setFather(ScheduleTask father) {
//        this.father = father;
//    }

    public ScheduleTaskLine getScheduleTaskLine() {
        return scheduleTaskLine;
    }

    public void setScheduleTaskLine(ScheduleTaskLine scheduleTaskLine) {
        this.scheduleTaskLine = scheduleTaskLine;
    }

    public ScheduleTestItem getScheduleTestItem() {
        return scheduleTestItem;
    }

    public void setScheduleTestItem(ScheduleTestItem scheduleTestItem) {
        this.scheduleTestItem = scheduleTestItem;
    }

    public static void setFatherSonRelation(ScheduleTask father, ScheduleTask son) {
        father.setSon(son);
//        son.setFather(father);
    }

    public ScheduleTask getSon() {
        return son;
    }

    public void setSon(ScheduleTask son) {
        this.son = son;
    }

    public void calcDate(EquipmentCalendarBitSet.BitSetWrapper wrapper) {
        if (getStartDate() != null) {
            int startAvailable = wrapper.getStartAvailable(getStartDate());
            int endRange = wrapper.getEndRange(startAvailable, getDurationTime());
            Calendar calendar = wrapper.getFromStart(startAvailable + endRange);
            setEndDate(calendar.getTime());
        }
        if (getSon() != null) {
            getSon().setStartDate(this.getEndDate());
        }
    }

    public void scheduleDate(EquipmentCalendarBitSet.BitSetWrapper wrapper) {
        this.calcDate(wrapper);
        if (getSon() != null) {
            getSon().scheduleDate(wrapper);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
