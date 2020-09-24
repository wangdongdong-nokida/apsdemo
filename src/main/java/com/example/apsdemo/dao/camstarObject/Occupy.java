package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.camstarData.OccupyData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "L_ZL", catalog = "")
public class Occupy extends OccupyData {

    @JsonIgnore
    @ManyToOne(targetEntity = WaferGearWarehouse.class)
    @JoinColumn(name = "XSL_ID")
    private WaferGearWarehouse waferGearWarehouse;

    @ManyToOne(targetEntity = SalesOrder.class)
    @JoinColumn(name = "DD_ID")
    private SalesOrder salesOrder;


    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }


    public WaferGearWarehouse getWaferGearWarehouse() {
        return waferGearWarehouse;
    }

    public void setWaferGearWarehouse(WaferGearWarehouse waferGearWarehouse) {
        this.waferGearWarehouse = waferGearWarehouse;
    }

}
