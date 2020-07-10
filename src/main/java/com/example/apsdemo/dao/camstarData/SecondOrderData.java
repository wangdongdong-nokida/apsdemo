package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@MappedSuperclass
@Data
@Table(name = "L_RJRW", catalog = "")
public class SecondOrderData {

    @Column(name = "CJRID")
    protected String cjrid;
    @Column(name = "CSBZID")
    protected String csbzid;
    @Column(name = "HPBZID")
    protected String hpbzid;
    @Column(name = "L_DLLXID")
    protected String lDllxid;
    @Column(name = "L_DLXHID")
    protected String lDlxhid;
    @Column(name = "SQRID")
    protected String sqrid;


    protected boolean testHidden;
    @Column(name = "BZ")
    protected String brief;
    @Column(name = "CETQTS")
    protected String testPlanDelay;
    @Column(name = "CPLX")
    protected String productType;
    @Column(name = "CSJHWCRQ")
    protected Date testPlanFinishedDate;
    @Column(name = "CSJSSJ")
    protected Date testEndDate;
    @Column(name = "CSKSSJ")
    protected Time testStartDate;
    @Column(name = "CSMARK")
    protected String testBrief;
    @Column(name = "DDSL")
    protected Integer quantity;
//    @Column(name = "DLID")
//    protected String circuitId;
//    @Column(name = "GXRID")
//    protected String gxrid;
//    @Column(name = "GXRQ")
//    private Time gxrq;

    @Column(name = "HPJHWCRQ")
    protected Date scribingPlanFinishDate;
    @Column(name = "HPJSSJ")
    protected Time scribingEndDate;
    @Column(name = "HPKSSJ")
    protected Time scribingStartDate;
    @Column(name = "HPMARK")
    protected String scribingBrief;
    @Column(name = "JB")
    protected String urgency;
    @Column(name = "KCBJ")
    protected String stockMark;
 //    @Column(name = "KLCKRQ")
//    protected Time klckrq; 开流程卡日期
    @Column(name = "BH")
    protected String waferNr;
    @Id
    @Column(name = "L_RJRWID")
    protected String ID;
    @Column(name = "L_RJRWNAME")
    protected String name;
    @Column(name = "XH")
    protected String waferModelNr;
    @Column(name = "PCH")
    protected String batchNr;
    @Column(name = "RJRWH")
    protected String nr;
    @Column(name = "RWLX")
    protected String type;
    @Column(name = "RWZT")
    protected String status;


//    @Column(name = "SFTK")
//    protected String sftk; 是否投考
//    @Column(name = "TKRQ")
//    protected Time tkrq;  投考日期


    @Column(name = "SQBZ")
    protected String supplyBrief;
    @Column(name = "SQSJ")
    protected Date supplyDate;
    @Column(name = "YGHPL")
    protected int planScribingQuantity;
    @Column(name = "YQCSWCRQ")
    protected Date testFinishedDateRequired;
    @Column(name = "YQHPWCSJ")
    protected Date scribingFinishedDateRequired;

}
