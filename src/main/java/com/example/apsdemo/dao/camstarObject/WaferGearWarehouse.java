package com.example.apsdemo.dao.camstarObject;


import com.example.apsdemo.dao.businessObject.GearPackingOrder;
import com.example.apsdemo.dao.businessObject.PackingOrder;
import com.example.apsdemo.dao.camstarData.WaferGearWarehouseData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "L_XPSL")
public class WaferGearWarehouse extends WaferGearWarehouseData {
    @JoinColumn(name = "XPXH_ID")
    @ManyToOne(targetEntity = WaferModelWarehouse.class)
    private WaferModelWarehouse waferModelWarehouse;

    @JsonIgnore
    @OneToMany(targetEntity = GearPackingOrder.class,fetch = FetchType.LAZY,mappedBy ="waferGearWarehouse" )
    private Set<GearPackingOrder> gearPackingOrders=new HashSet<>();

    @OneToMany(targetEntity = Occupy.class,fetch = FetchType.LAZY,mappedBy = "waferGearWarehouse")
    private Set<Occupy> occupies=new HashSet<>();

    public Set<GearPackingOrder> getGearPackingOrders() {
        return gearPackingOrders;
    }

    public void setGearPackingOrders(Set<GearPackingOrder> gearPackingOrders) {
        this.gearPackingOrders = gearPackingOrders;
    }

    public WaferModelWarehouse getWaferModelWarehouse() {
        return waferModelWarehouse;
    }

    public void setWaferModelWarehouse(WaferModelWarehouse waferModelWarehouse) {
        this.waferModelWarehouse = waferModelWarehouse;
    }

    public Set<Occupy> getOccupies() {
        return occupies;
    }

    public void setOccupies(Set<Occupy> occupies) {
        this.occupies = occupies;
    }
}
