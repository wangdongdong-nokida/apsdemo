package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.dao.businessObject.ScheduleTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class PickingOrderData extends ScheduleTask {

    protected String waferNr;
    protected String sliceNr;
    protected String modelNr;
    protected String sliceState;
    protected String bindSalesOrder;
    protected String bindContract;
    protected String bindCustomer;
    protected String bindSalesOrderID;
    protected String salesOrderQuantities;
    protected String salesOrderTestDate;

    protected String circuitNr;
    protected String quantity;
    protected boolean salesOrder;

    protected String brief;

}
