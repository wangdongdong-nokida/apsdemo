package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.Base.DataBase;
import com.example.apsdemo.dao.businessData.GearPackingOrderData;
import com.example.apsdemo.dao.camstarObject.WaferGearWarehouse;

import javax.persistence.*;

@Entity
@Table(name = "A_GEAR_PACKING_ORDER")
public class GearPackingOrder extends GearPackingOrderData {

    @ManyToOne(targetEntity = PackingOrder.class,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name = "PACKING_ORDERS_ID")
    private PackingOrder packingOrder;

    @ManyToOne(targetEntity = WaferGearWarehouse.class)
    @JoinColumn(name = "WAFER_GEAR_WAREHOUSES_ID")
    private WaferGearWarehouse waferGearWarehouse;

    public GearPackingOrder() {
    }

    public GearPackingOrder(PackingOrder packingOrder, WaferGearWarehouse waferGearWarehouse) {
        this.packingOrder = packingOrder;
        this.waferGearWarehouse = waferGearWarehouse;
    }

    public PackingOrder getPackingOrder() {
        return packingOrder;
    }

    public void setPackingOrder(PackingOrder packingOrder) {
        this.packingOrder = packingOrder;
    }

    public WaferGearWarehouse getWaferGearWarehouse() {
        return waferGearWarehouse;
    }

    public void setWaferGearWarehouse(WaferGearWarehouse waferGearWarehouse) {
        this.waferGearWarehouse = waferGearWarehouse;
    }

}
