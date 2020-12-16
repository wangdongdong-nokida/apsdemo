package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.PickingOrderData;
import com.example.apsdemo.dao.camstarObject.WorkFlowName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "A_Packing_ORDER")
@PrimaryKeyJoinColumn(name = "ID")
public class PickingOrder extends PickingOrderData {

    public PickingOrder() {
    }

    public PickingOrder(boolean salesOrder) {
        this.setSalesOrder(salesOrder);
    }

    @JsonIgnore
    @OneToMany(targetEntity = GearPickingOrder.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "pickingOrder")
    private Set<GearPickingOrder> gearPickingOrders = new HashSet<>();

    @JsonIgnore
    @OneToMany(targetEntity = Operation.class,cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE},mappedBy = "pickingOrder")
    private Set<Operation> operation=new HashSet<>();

    @JsonIgnore
    @OneToOne(targetEntity =WorkFlowName.class)
    private WorkFlowName workFlowName;


    public WorkFlowName getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(WorkFlowName workFlowName) {
        this.workFlowName = workFlowName;
    }

    public Set<Operation> getOperation() {
        return operation;
    }

    public void setOperation(Set<Operation> operation) {
        this.operation = operation;
    }

    public Set<GearPickingOrder> getGearPickingOrders() {
        return gearPickingOrders;
    }

    public void setGearPickingOrders(Set<GearPickingOrder> gearPickingOrders) {
        this.gearPickingOrders = gearPickingOrders;
    }

}
