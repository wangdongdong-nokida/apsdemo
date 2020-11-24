package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
@Data
public class WaferModelWarehouseData {
    @Id
    @Column(name = "L_XPID")
    protected String ID;
    @Column(name = "CPXH")
    protected String modelNr;
    @Column(name = "LX")
    protected String type;
    @Column(name = "DLXH")
    protected String circuitNr;
    @Column(name = "CPMC")
    protected String name;
    @Column(name = "YPBZ")
    protected String breif;

    @Transient
    protected String bindSalesOrder;

    @Transient
    protected Object[] modelNrs;

    @Transient
    protected String quantity;
}
