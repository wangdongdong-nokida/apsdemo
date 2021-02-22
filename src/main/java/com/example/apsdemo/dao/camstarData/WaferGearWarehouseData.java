package com.example.apsdemo.dao.camstarData;

import com.example.apsdemo.annotation.Attribute;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
@Data
public class WaferGearWarehouseData {
    @Id
    @Column(name = "L_XPSLID")
    protected String ID;
    @Column(name = "DW")
    protected String GearNr;
    @Column(name = "KCSL")
    protected String quantity;
    @Attribute(name = "占料数量")
    protected String ZLSL;
    @Attribute(name = "质量等级")
    protected String ZLDJ;
    @Attribute(name = "筛选单号")
    protected String SXDH;
    @Attribute(name = "检验批次")
    protected String JYPC;
    @Attribute(name = "对应资料")
    protected String DYZL;
    @Attribute(name = "物料状态")
    protected String WLZT;
    @Attribute(name = "入库类型")
    protected String RKLX;
    @Attribute(name = "入库时间")
    protected String RKSJ;
    @Attribute(name = "入库状态")
    protected String RKZT;
    @Attribute(name = "物理形态")
    protected String WLXT;
    @Attribute(name = "备注")
    protected String YPBZ;
    @Column(name = "KFNAME")
    protected String stockName;
    @Column(name = "DWID")
    protected String DWID;
    @Column(name = "XH")
    protected String XH;

    @Transient
    protected String bindSalesOrder;
}
