package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.ScheduleTestItemData;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.schedule.ScheduleTask;
import com.example.apsdemo.schedule.ScheduleTaskLine;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "scheduleTestItem")
public class ScheduleTestItem extends ScheduleTestItemData {
    @JsonIgnore
    @ManyToOne(targetEntity = TestScribingCenter.class,fetch = FetchType.LAZY)
    private TestScribingCenter testScribingCenter;

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class,mappedBy ="scheduleTestItem" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private ScheduleTask scheduleTask;

    public ScheduleTask getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
        if(scheduleTask!=null){
            scheduleTask.setScheduleTestItem(this);
        }
    }

    public ScheduleTestItem() {
    }

    public TestScribingCenter getTestScribingCenter() {
        return testScribingCenter;
    }

    public void setTestScribingCenter(TestScribingCenter testScribingCenter) {
        this.testScribingCenter = testScribingCenter;
    }

    public ScheduleTestItem(ScheduleTaskLine line, TestScribingCenter center, String product, String waferNr, String sliceNr, String screen, String testType, int durationTime){
        this.setWaferNr(waferNr);
        this.setScheduleTask(new ScheduleTask(line,this,durationTime));
        this.setTestScribingCenter(center);
        this.setProductNr(product);
        this.setSliceNr(sliceNr);
        this.setTestParameter(screen);
        this.setTestType(testType);
    }

}
