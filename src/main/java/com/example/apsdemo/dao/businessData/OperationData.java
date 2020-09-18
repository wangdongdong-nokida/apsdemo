package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import com.example.apsdemo.dao.businessObject.ScheduleTask;
import com.example.apsdemo.dao.camstarObject.Equipment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

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

    protected String equipmentName;
    protected boolean salesOrder;
    protected String brief;

    @Transient
    Set<Equipment> equipments=new HashSet<>();

}
