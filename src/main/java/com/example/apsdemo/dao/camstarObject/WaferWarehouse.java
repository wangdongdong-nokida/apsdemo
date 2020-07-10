package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarData.WaferWarehouseData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@Entity
public class WaferWarehouse extends WaferWarehouseData {

    @JsonIgnore
    @OneToMany(targetEntity = WaferModelWarehouse.class, mappedBy = "waferWarehouse")
    private Set<WaferModelWarehouse> waferModelWarehouse;

    @JsonIgnore
    @OneToOne(targetEntity = TestScribingCenter.class,mappedBy = "waferWarehouse",cascade = CascadeType.ALL)
    private TestScribingCenter testScribingCenter;

    public Set<WaferModelWarehouse> getWaferModelWarehouse() {
        return waferModelWarehouse;
    }

    public void setWaferModelWarehouse(Set<WaferModelWarehouse> waferModelWarehouse) {
        this.waferModelWarehouse = waferModelWarehouse;
    }

    public TestScribingCenter getTestScribingCenter() {
        return testScribingCenter;
    }

    public void setTestScribingCenter(TestScribingCenter testScribingCenter) {
        this.testScribingCenter = testScribingCenter;
    }
}
