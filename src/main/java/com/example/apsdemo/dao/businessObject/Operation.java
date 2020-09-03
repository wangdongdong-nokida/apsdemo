package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.businessData.OperationData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name = "A_OPERATION")
@PrimaryKeyJoinColumn(name = "ID")
public class Operation extends OperationData {

    @JsonIgnore
    @ManyToOne(targetEntity = PackingOrder.class)
    private PackingOrder packingOrder;

    public Operation() {
    }

    public Operation(PackingOrder packingOrder) {
        this.packingOrder = packingOrder;
        if (packingOrder != null) {
            this.setWaferNr(packingOrder.getWaferNr());
            this.setSliceNr(packingOrder.getSliceNr());
            this.setModelNr(packingOrder.getModelNr());
            this.setCircuitNr(packingOrder.getCircuitNr());
            this.setBindSalesOrder(packingOrder.getBindSalesOrder());
            this.setSalesOrderQuantities(packingOrder.getSalesOrderQuantities());
        }
    }

    public PackingOrder getPackingOrder() {
        return packingOrder;
    }

    public void setPackingOrder(PackingOrder packingOrder) {
        this.packingOrder = packingOrder;
    }
}
