package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.ScheduleTestItemData;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "A_scheduleTestItem")
public class ScheduleTestItem extends ScheduleTestItemData {

    @JsonIgnore
    @ManyToOne(targetEntity = TestScribingCenter.class, fetch = FetchType.LAZY)
    private TestScribingCenter testScribingCenter;

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTask.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ScheduleTask scheduleTask;

    @ManyToOne(targetEntity = SecondOrder.class)
    private SecondOrder secondOrder;

    public SecondOrder getSecondOrder() {
        return secondOrder;
    }

    public void setSecondOrder(SecondOrder secondOrder) {
        this.secondOrder = secondOrder;
    }


    public ScheduleTask getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
        if (scheduleTask != null) {
            scheduleTask.setScheduleTestItem(this);
        }
    }

    public ScheduleTestItem() {
    }

    public TestScribingCenter getTestScribingCenter() {
        return testScribingCenter;
    }

    public  void setTestScribingCenter(TestScribingCenter testScribingCenter) {
        this.testScribingCenter = testScribingCenter;
        if (testScribingCenter != null) {
            testScribingCenter.getScheduleTestItem().add(this);
            this.setWaferNr(testScribingCenter.getWaferNr());
            this.setSliceNr(testScribingCenter.getSliceNr());
        }
    }

    public ScheduleTestItem(SecondOrder secondOrder, ScheduleTaskLine line, TestScribingCenter center, String product, String waferNr, String sliceNr, String screen, String testType, int durationTime, int quantity, String circuitNr) {
        this.setSecondOrder(secondOrder);
        this.setWaferNr(waferNr);
        this.setScheduleTask(new ScheduleTask(line, this, durationTime));
        this.setTestScribingCenter(center);
        this.setProductNr(product);
        this.setSliceNr(sliceNr);
        this.setTestParameter(screen);
        this.setTestType(testType);
        this.setQuantity(quantity);
        this.setCircuitNr(circuitNr);
    }

}
