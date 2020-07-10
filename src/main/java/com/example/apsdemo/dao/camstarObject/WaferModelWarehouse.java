package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.WaferModelWarehouseData;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class WaferModelWarehouse extends WaferModelWarehouseData {

    @ManyToOne(targetEntity = WaferWarehouse.class)
    private WaferWarehouse waferWarehouse;

    @OneToMany(targetEntity = WaferGearWarehouse.class,mappedBy = "waferModelWarehouse")
    private Set<WaferGearWarehouse> waferGearWarehouses;

    public Set<WaferGearWarehouse> getWaferGearWarehouses() {
        return waferGearWarehouses;
    }

    public void setWaferGearWarehouses(Set<WaferGearWarehouse> waferGearWarehouses) {
        this.waferGearWarehouses = waferGearWarehouses;
    }

    public WaferWarehouse getWaferWarehouse() {
        return waferWarehouse;
    }

    public void setWaferWarehouse(WaferWarehouse waferWarehouse) {
        this.waferWarehouse = waferWarehouse;
    }
}
