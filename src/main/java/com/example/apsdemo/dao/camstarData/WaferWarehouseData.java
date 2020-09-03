package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Data
public class WaferWarehouseData {
    @Id
    @Column(name = "L_YPXXID")
    protected String ID;
    @Column(name = "BH")
    protected String waferNr;
    @Column(name = "YPH")
    protected String sliceNr;
    @Column(name = "LX")
    protected String type;
    @Column(name = "F_BH")
    protected String fatherWaferNr;
    @Column(name = "PCH")
    protected String batchNr;
    @Column(name = "JYSJ")
    protected Date testDate;
    @Column(name = "JYJG")
    protected String testResult;
    @Column(name = "JYMX")
    protected String testDetail;
    @Column(name = "YPBZ")
    protected String brief;
//    @Column(name = "")
//    protected String operator;
    @Column(name = "YPZT")
    protected String status;
}
