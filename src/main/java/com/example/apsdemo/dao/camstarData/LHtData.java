package com.example.apsdemo.dao.camstarData;

import javax.persistence.*;

@MappedSuperclass
public class LHtData {

    @Column(name = "CJR")
    private String cjr;
    //    工程号
    @Column(name = "GCH")
    private String gch;
    @Column(name = "L_HTNAME")
    private String lHtname;
    //    合同号
    @Column(name = "HTH")
    private String hth;

    @Id
    @Column(name = "L_HTID")
    private String lHtid;
    //    销售类型
    @Column(name = "XSLX")
    private String xslx;
    //    备注
    @Column(name = "BZ")
    private String bz;
    //    链接地址x
    @Column(name = "LJDZ")
    private String ljdz;
    //    元器件质保单位x
    @Column(name = "YQJZBDW")
    private String yqjzbdw;
    //    订单类型
    @Column(name = "DDLX")
    private String ddlx;
    //    客户
    @Column(name = "KH")
    private String kh;
    //    销售片区
    @Column(name = "XSPQ")
    private String xspq;


    public String getCjr() {
        return cjr;
    }

    public void setCjr(String cjr) {
        this.cjr = cjr;
    }


    public String getGch() {
        return gch;
    }

    public void setGch(String gch) {
        this.gch = gch;
    }


    public String getHth() {
        return hth;
    }

    public void setHth(String hth) {
        this.hth = hth;
    }


    public String getlHtid() {
        return lHtid;
    }

    public void setlHtid(String lHtid) {
        this.lHtid = lHtid;
    }


    public String getlHtname() {
        return lHtname;
    }

    public void setlHtname(String lHtname) {
        this.lHtname = lHtname;
    }


    public String getXslx() {
        return xslx;
    }

    public void setXslx(String xslx) {
        this.xslx = xslx;
    }


    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }


    public String getLjdz() {
        return ljdz;
    }

    public void setLjdz(String ljdz) {
        this.ljdz = ljdz;
    }


    public String getYqjzbdw() {
        return yqjzbdw;
    }

    public void setYqjzbdw(String yqjzbdw) {
        this.yqjzbdw = yqjzbdw;
    }


    public String getDdlx() {
        return ddlx;
    }

    public void setDdlx(String ddlx) {
        this.ddlx = ddlx;
    }


    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }


    public String getXspq() {
        return xspq;
    }

    public void setXspq(String xspq) {
        this.xspq = xspq;
    }


}
