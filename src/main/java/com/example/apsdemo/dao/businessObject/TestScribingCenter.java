package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.TestScribingCenterData;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "A_TestScribing_Center")
public class TestScribingCenter extends TestScribingCenterData {

    @OneToOne(targetEntity = WaferWarehouse.class)
    private WaferWarehouse waferWarehouse;

    @JsonIgnore
    @OneToMany(targetEntity = ScheduleTestItem.class,mappedBy = "testScribingCenter",cascade = CascadeType.DETACH)
    private Set<ScheduleTestItem> scheduleTestItem;

    @JsonIgnore
    @OneToMany(targetEntity = ScheduleScribingItem.class,mappedBy = "testScribingCenter",cascade = CascadeType.DETACH)
    private Set<ScheduleScribingItem> scheduleScribingItems;

    @JsonIgnore
    @ManyToOne(targetEntity = SecondOrder.class)
    private SecondOrder secondOrder;

    public SecondOrder getSecondOrder() {
        return secondOrder;
    }

    public void setSecondOrder(SecondOrder secondOrder) {
        this.secondOrder = secondOrder;
        if(secondOrder!=null){
            secondOrder.getTestScribingCenters().add(this);
        }
    }

    public Set<ScheduleScribingItem> getScheduleScribingItems() {
        return scheduleScribingItems;
    }

    public void setScheduleScribingItems(Set<ScheduleScribingItem> scheduleScribingItems) {
        this.scheduleScribingItems = scheduleScribingItems;
    }

    public TestScribingCenter(String sliceNr, String waferNr) {
        this.setSliceNr(sliceNr);
        this.setWaferNr(waferNr);
    }

    public TestScribingCenter() {
    }

    public Set<ScheduleTestItem> getScheduleTestItem() {
        return scheduleTestItem;
    }

    public void setScheduleTestItem(Set<ScheduleTestItem> scheduleTestItem) {
        this.scheduleTestItem = scheduleTestItem;
    }

    public WaferWarehouse getWaferWarehouse() {
        return waferWarehouse;
    }

    public void setWaferWarehouse(WaferWarehouse waferWarehouse) {
        this.waferWarehouse = waferWarehouse;
        if(waferWarehouse!=null){
            this.setWaferNr(waferWarehouse.getWaferNr());
            this.setSliceNr(waferWarehouse.getSliceNr());
            waferWarehouse.setTestScribingCenter(this);
            for(ScheduleTestItem item:this.getScheduleTestItem()){
                item.setTestScribingCenter(this);
            }
            for(ScheduleScribingItem item:this.getScheduleScribingItems()){
                item.setTestScribingCenter(this);
            }
        }
    }
}
