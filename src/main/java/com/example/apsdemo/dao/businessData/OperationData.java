package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import com.example.apsdemo.dao.businessObject.ScheduleTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class OperationData extends ScheduleTask {
    protected String waferNr;
    protected String sliceNr;
    protected String modelNr;
    protected String sliceState;
    protected String bindSalesOrder;
    protected String salesOrderQuantities;
    protected String circuitNr;
    protected int quantity;

    protected String workStepName;
    protected String workFlowName;

    protected boolean salesOrder;
    protected String brief;
}
