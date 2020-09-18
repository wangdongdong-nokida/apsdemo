package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.ScheduleScribingItemData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name ="A_ScheduleScribingItem")
public class ScheduleScribingItem extends ScheduleScribingItemData {

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class)
    private ScheduleTask scheduleTask;

    @JsonIgnore
    @ManyToOne(targetEntity = TestScribingCenter.class)
    private TestScribingCenter testScribingCenter;

    public TestScribingCenter getTestScribingCenter() {
        return testScribingCenter;
    }

    public void setTestScribingCenter(TestScribingCenter testScribingCenter) {
        this.testScribingCenter = testScribingCenter;
        if(testScribingCenter!=null){
            testScribingCenter.getScheduleScribingItems().add(this);
            this.setWaferNr(testScribingCenter.getWaferNr());
            this.setSliceNr(testScribingCenter.getSliceNr());
        }
    }

    public ScheduleTask getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
    }

    public ScheduleScribingItem() {
    }

    public ScheduleScribingItem(String brief,double durationTime,TestScribingCenter center,ScheduleTaskLine line) {
        this.setScheduleTask(new ScheduleTask(line,this,(int)(durationTime*60)));
        this.setSliceNr(center.getSliceNr());
        this.setWaferNr(center.getWaferNr());
        this.setTestScribingCenter(center);
        this.setBrief(brief);
    }

    public ScheduleScribingItem(String brief,double durationTime,TestScribingCenter center,
                                ScheduleTaskLine line,String responsiblePerson,
                                String applyPerson,String applyDate,String operationNr){
        this(brief, durationTime, center, line);
        this.setResponsiblePerson(responsiblePerson);
        this.setApplyPerson(applyPerson);
        this.setApplyDate(applyDate);
        this.setOperationNr(operationNr);
    }

}
