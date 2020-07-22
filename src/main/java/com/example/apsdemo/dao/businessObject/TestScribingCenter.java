package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.Base.DataBase;
import com.example.apsdemo.dao.camstarObject.Wafer;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "A_TestScribingCenter")
public class TestScribingCenter extends DataBase {

    @OneToOne(targetEntity = WaferWarehouse.class)
    private WaferWarehouse waferWarehouse;

    @OneToMany(targetEntity = ScheduleTestItem.class,mappedBy = "testScribingCenter")
    private Set<ScheduleTestItem> scheduleTestItem;

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
    }
}
