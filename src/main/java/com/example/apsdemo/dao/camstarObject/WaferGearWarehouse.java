package com.example.apsdemo.dao.camstarObject;


import com.example.apsdemo.dao.camstarData.WaferGearWarehouseData;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class WaferGearWarehouse extends WaferGearWarehouseData {
    @ManyToOne(targetEntity = WaferModelWarehouse.class)
    private WaferModelWarehouse waferModelWarehouse;

    public WaferModelWarehouse getWaferModelWarehouse() {
        return waferModelWarehouse;
    }

    public void setWaferModelWarehouse(WaferModelWarehouse waferModelWarehouse) {
        this.waferModelWarehouse = waferModelWarehouse;
    }
}
