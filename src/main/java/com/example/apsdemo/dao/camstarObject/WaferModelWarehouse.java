package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.WaferModelWarehouseData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "L_XP")
public class WaferModelWarehouse extends WaferModelWarehouseData {

    @JoinColumn(name = "YPH_ID")
    @ManyToOne(targetEntity = WaferWarehouse.class)
    private WaferWarehouse waferWarehouse;

    @JsonIgnore
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
