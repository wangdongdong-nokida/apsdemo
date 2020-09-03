package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.PackingOrderData;
import com.example.apsdemo.dao.camstarObject.SalesOrder;
import com.example.apsdemo.dao.camstarObject.WaferGearWarehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "A_PACKING_ORDER")
@PrimaryKeyJoinColumn(name = "ID")
public class PackingOrder extends PackingOrderData {

    public PackingOrder() {
    }

    public PackingOrder(boolean salesOrder) {
        this.setSalesOrder(salesOrder);
    }

    @JsonIgnore
    @OneToMany(targetEntity = GearPackingOrder.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "packingOrder")
    private Set<GearPackingOrder> gearPackingOrders = new HashSet<>();

    @JsonIgnore
    @OneToMany(targetEntity = Operation.class,mappedBy = "packingOrder")
    private Set<Operation> operation=new HashSet<>();

    public Set<Operation> getOperation() {
        return operation;
    }

    public void setOperation(Set<Operation> operation) {
        this.operation = operation;
    }

    public Set<GearPackingOrder> getGearPackingOrders() {
        return gearPackingOrders;
    }

    public void setGearPackingOrders(Set<GearPackingOrder> gearPackingOrders) {
        this.gearPackingOrders = gearPackingOrders;
    }

}
