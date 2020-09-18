package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.Base.DataBase;
import com.example.apsdemo.dao.businessData.GearPickingOrderData;
import com.example.apsdemo.dao.camstarObject.WaferGearWarehouse;

import javax.persistence.*;

@Entity
@Table(name = "A_GEAR_Packing_ORDER")
public class GearPickingOrder extends GearPickingOrderData {

    @ManyToOne(targetEntity = PickingOrder.class,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name = "PACKING_ORDERS_ID")
    private PickingOrder pickingOrder;

    @ManyToOne(targetEntity = WaferGearWarehouse.class)
    @JoinColumn(name = "WAFER_GEAR_WAREHOUSES_ID")
    private WaferGearWarehouse waferGearWarehouse;

    public GearPickingOrder() {
    }

    public GearPickingOrder(PickingOrder pickingOrder, WaferGearWarehouse waferGearWarehouse) {
        this.pickingOrder = pickingOrder;
        this.waferGearWarehouse = waferGearWarehouse;
    }

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public WaferGearWarehouse getWaferGearWarehouse() {
        return waferGearWarehouse;
    }

    public void setWaferGearWarehouse(WaferGearWarehouse waferGearWarehouse) {
        this.waferGearWarehouse = waferGearWarehouse;
    }

}
