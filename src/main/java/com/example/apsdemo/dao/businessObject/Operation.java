package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.OperationData;
import com.example.apsdemo.dao.camstarObject.WorkStepName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name = "A_OPERATION")
@PrimaryKeyJoinColumn(name = "ID")
public class Operation extends OperationData {

    @JsonIgnore
    @ManyToOne(targetEntity = PickingOrder.class)
    @JoinColumn(name = "PACKING_ORDER_ID")
    private PickingOrder pickingOrder;


    @OneToOne(fetch = FetchType.EAGER)
    private WorkStepName R_workStepName;


    public WorkStepName getR_workStepName() {
        return R_workStepName;
    }

    public void setR_workStepName(WorkStepName r_workStepName) {
        R_workStepName = r_workStepName;
    }

    public Operation() {
    }

    public Operation(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
        if (pickingOrder != null) {
            this.setWaferNr(pickingOrder.getWaferNr());
            this.setSliceNr(pickingOrder.getSliceNr());
            this.setModelNr(pickingOrder.getModelNr());
            this.setCircuitNr(pickingOrder.getCircuitNr());
            this.setBindSalesOrder(pickingOrder.getBindSalesOrder());
            this.setSalesOrderQuantities(pickingOrder.getSalesOrderQuantities());
        }
    }

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }
}
