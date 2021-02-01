package com.example.apsdemo.dao.camstarData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@MappedSuperclass
@Data
@Table(name = "L_RJRW", catalog = "")
public class SecondOrderData {

    /*@Column(name = "CJRID")
    protected String cjrid;*/
    @Column(name = "CSBZ")
    protected String csbz;
//    @Column(name = "L_DLLXID")
//    protected String lDllxid;
//    @Column(name = "L_DLXHID")
//    protected String lDlxhid;
//    @Column(name = "SQRID")
//    protected String sqrid;
    @Column(name = "HPBZ")
    protected String scribingGroup;

    protected boolean testHidden;
    @Column(name = "BZ")
    protected String brief;
    @Column(name = "CETQTS")
    protected String testPlanDelay;
    @Column(name = "CSJHWCRQ")
    protected Date testPlanFinishedDate;
    @Column(name = "CSJSSJ")
    protected Date testEndDate;
    @Column(name = "CSKSSJ")
    protected Date testStartDate;
    @Column(name = "CSMARK")
    protected String testBrief;
    @Column(name = "DDSL")
    protected String quantity;
    @Column(name = "rwly")
    protected String rwly;
//    @Column(name = "GXRID")
//    protected String gxrid;
    @Column(name = "GXRQ")
    private Time gxrq;
    @Column(name = "HPJHWCRQ")
    protected Date scribingPlanFinishDate;
    @Column(name = "HPJSSJ")
    protected Date scribingEndDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    protected Date applyDate;
//    @Column(name = "YGHPL")
//    protected int planScribingQuantity;
    @Column(name = "YQCSWCRQ")
    protected Date testFinishedDateRequired;
    @Column(name = "YQHPWCSJ")
    protected Date scribingFinishedDateRequired;

}
