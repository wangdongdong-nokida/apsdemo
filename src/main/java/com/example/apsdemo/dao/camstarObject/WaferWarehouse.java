package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarData.WaferWarehouseData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "L_YPXX")
public class WaferWarehouse extends WaferWarehouseData {

    @JsonIgnore
    @OneToMany(targetEntity = WaferModelWarehouse.class, mappedBy = "waferWarehouse")
    private Set<WaferModelWarehouse> waferModelWarehouse;

    @JsonIgnore
    @OneToOne(targetEntity = TestScribingCenter.class,mappedBy = "waferWarehouse",cascade = CascadeType.ALL)
    private TestScribingCenter testScribingCenter;

    @OneToOne(targetEntity = LLpjd.class)
    @JoinColumn(name = "LPJD_ID",foreignKey = @ForeignKey(name = "",value = ConstraintMode.NO_CONSTRAINT))
    private LLpjd lLpjd;

    public LLpjd getlLpjd() {
        return lLpjd;
    }

    public void setlLpjd(LLpjd lLpjd) {
        this.lLpjd = lLpjd;
    }

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
