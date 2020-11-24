package com.example.apsdemo.dao.camstarData;

import com.example.apsdemo.annotation.Attribute;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class SalesOrderData {
    @Attribute(name = "版号")
    private String bh;
    @Attribute(name = "备注")
    private String bz;
//    @Attribute(name = "订单号")
//    private String ddh;
    @Attribute(name = "订单数量")
    private Integer ddsl;
    @Attribute(name = "订单状态")
    private String ddzt;
    @Attribute(name = "客户")
    private String khjc;
    @Attribute(name = "是否军检")
    private String sfjj;
    @Attribute(name = "是否监制")
    private String sfjz;
    @Attribute(name = "型号")
    private String xh;
    @Attribute(name = "预发货日期")
    private Date yfhrq;
    @Attribute(name = "检验完成时间")
    private Date jywcsj;


    private String cqfj;
    private Date ddjsrq;
    private Integer dgsl;
    private Integer fhsl;
    private String filtertags;
    private String fzbmid;
    private String htsm;
    private Date jfqx;
    private String jjbz;
    private Date jjsj;
    private String jldwid;
    private Date jsrq;
    private String jzdw;
    private Date jzsj;
    private String ID;
    private String lDdname;
    private String qrbz;
    private String sfdpa;
    private String sfecsx;
    private String sffkzdd;
    private String sfxp;
    private String sfxy;
    private String sfys;
    private String sfzdgc;
    private String tcqps;
    private String tgxgcl;
    private Integer xqsl;
    private Integer ycdhs;
    private Integer yhdcs;
    private Integer yhdjs;
    private String yjrwId;
    private Integer ypygs;
    private String ysdw;
    private Date yssj;
    private Integer yxj;
    private String zbdw;
    private String zldj;
    private String zxbz;
    private String lYjrwid;
    private String cpbm;
    private String cplx;
    private String cpmc;
    private String ddbh;
    private String hsjg;
    private String jjyq;
    private String jzyq;
    private String ljdz;
    private String ly;
    private Date qdrq;
    private String scjl;
    private String xmgzd;
    private String yhddh;
    private String yhdw;
    private String yhfl;
    private String yqjzbdw;
    private String ysyq;
    private String yzbm;
    private String yzbmid;
    private String zzjyxs;
    private Date xmjssj;
    private Date xmkssj;
    private String xmlx;
    private String xmmc;
    private String ckbh;
    private String csddfk;
    private String ddlb;
    private Date yqfhwcrq;
    private Date yqjjwcrq;
    private String ckbz;
    private Date cksj;


    @Basic
    @Column(name = "BH")
    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    @Basic
    @Column(name = "BZ")
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Basic
    @Column(name = "CQFJ")
    public String getCqfj() {
        return cqfj;
    }

    public void setCqfj(String cqfj) {
        this.cqfj = cqfj;
    }

//    @Basic
//    @Column(name = "DDH")
//    public String getDdh() {
//        return ddh;
//    }

//    public void setDdh(String ddh) {
//        this.ddh = ddh;
//    }

    @Basic
    @Column(name = "DDJSRQ")
    public Date getDdjsrq() {
        return ddjsrq;
    }

    public void setDdjsrq(Date ddjsrq) {
        this.ddjsrq = ddjsrq;
    }

    @Basic
    @Column(name = "DDSL")
    public Integer getDdsl() {
        return ddsl;
    }

    public void setDdsl(Integer ddsl) {
        this.ddsl = ddsl;
    }

    @Basic
    @Column(name = "DDZT")
    public String getDdzt() {
        return ddzt;
    }

    public void setDdzt(String ddzt) {
        this.ddzt = ddzt;
    }

    @Basic
    @Column(name = "DGSL")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    public Integer getDgsl() {
        return dgsl;
    }

    public void setDgsl(Integer dgsl) {
        this.dgsl = dgsl;
    }

    @Basic
    @Column(name = "FHSL")
    public Integer getFhsl() {
        return fhsl;
    }

    public void setFhsl(Integer fhsl) {
        this.fhsl = fhsl;
    }

    @Basic
    @Column(name = "FILTERTAGS")
    public String getFiltertags() {
        return filtertags;
    }

    public void setFiltertags(String filtertags) {
        this.filtertags = filtertags;
    }

    @Basic
    @Column(name = "FZBMID")
    public String getFzbmid() {
        return fzbmid;
    }

    public void setFzbmid(String fzbmid) {
        this.fzbmid = fzbmid;
    }

    @Basic
    @Column(name = "HTSM")
    public String getHtsm() {
        return htsm;
    }

    public void setHtsm(String htsm) {
        this.htsm = htsm;
    }

    @Basic
    @Column(name = "JFQX")
    public Date getJfqx() {
        return jfqx;
    }

    public void setJfqx(Date jfqx) {
        this.jfqx = jfqx;
    }

    @Basic
    @Column(name = "JJBZ")
    public String getJjbz() {
        return jjbz;
    }

    public void setJjbz(String jjbz) {
        this.jjbz = jjbz;
    }

    @Basic
    @Column(name = "JJSJ")
    public Date getJjsj() {
        return jjsj;
    }

    public void setJjsj(Date jjsj) {
        this.jjsj = jjsj;
    }

    @Basic
    @Column(name = "JLDWID")
    public String getJldwid() {
        return jldwid;
    }

    public void setJldwid(String jldwid) {
        this.jldwid = jldwid;
    }

    @Basic
    @Column(name = "JSRQ")
    public Date getJsrq() {
        return jsrq;
    }

    public void setJsrq(Date jsrq) {
        this.jsrq = jsrq;
    }

    @Basic
    @Column(name = "JZDW")
    public String getJzdw() {
        return jzdw;
    }

    public void setJzdw(String jzdw) {
        this.jzdw = jzdw;
    }

    @Basic
    @Column(name = "JZSJ")
    public Date getJzsj() {
        return jzsj;
    }

    public void setJzsj(Date jzsj) {
        this.jzsj = jzsj;
    }

    @Basic
    @Column(name = "KHJC")
    public String getKhjc() {
        return khjc;
    }

    public void setKhjc(String khjc) {
        this.khjc = khjc;
    }

    @Id
    @Column(name = "L_DDID")
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Basic
    @Column(name = "L_DDNAME")
    public String getlDdname() {
        return lDdname;
    }

    public void setlDdname(String lDdname) {
        this.lDdname = lDdname;
    }


    @Basic
    @Column(name = "QRBZ")
    public String getQrbz() {
        return qrbz;
    }

    public void setQrbz(String qrbz) {
        this.qrbz = qrbz;
    }

    @Basic
    @Column(name = "SFDPA")
    public String getSfdpa() {
        return sfdpa;
    }

    public void setSfdpa(String sfdpa) {
        this.sfdpa = sfdpa;
    }

    @Basic
    @Column(name = "SFECSX")
    public String getSfecsx() {
        return sfecsx;
    }

    public void setSfecsx(String sfecsx) {
        this.sfecsx = sfecsx;
    }

    @Basic
    @Column(name = "SFFKZDD")
    public String getSffkzdd() {
        return sffkzdd;
    }

    public void setSffkzdd(String sffkzdd) {
        this.sffkzdd = sffkzdd;
    }

    @Basic
    @Column(name = "SFJJ")
    public String getSfjj() {
        return sfjj;
    }

    public void setSfjj(String sfjj) {
        this.sfjj = sfjj;
    }

    @Basic
    @Column(name = "SFJZ")
    public String getSfjz() {
        return sfjz;
    }

    public void setSfjz(String sfjz) {
        this.sfjz = sfjz;
    }

    @Basic
    @Column(name = "SFXP")
    public String getSfxp() {
        return sfxp;
    }

    public void setSfxp(String sfxp) {
        this.sfxp = sfxp;
    }

    @Basic
    @Column(name = "SFXY")
    public String getSfxy() {
        return sfxy;
    }

    public void setSfxy(String sfxy) {
        this.sfxy = sfxy;
    }

    @Basic
    @Column(name = "SFYS")
    public String getSfys() {
        return sfys;
    }

    public void setSfys(String sfys) {
        this.sfys = sfys;
    }

    @Basic
    @Column(name = "SFZDGC")
    public String getSfzdgc() {
        return sfzdgc;
    }

    public void setSfzdgc(String sfzdgc) {
        this.sfzdgc = sfzdgc;
    }

    @Basic
    @Column(name = "TCQPS")
    public String getTcqps() {
        return tcqps;
    }

    public void setTcqps(String tcqps) {
        this.tcqps = tcqps;
    }

    @Basic
    @Column(name = "TGXGCL")
    public String getTgxgcl() {
        return tgxgcl;
    }

    public void setTgxgcl(String tgxgcl) {
        this.tgxgcl = tgxgcl;
    }

    @Basic
    @Column(name = "XH")
    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    @Basic
    @Column(name = "XQSL")
    public Integer getXqsl() {
        return xqsl;
    }

    public void setXqsl(Integer xqsl) {
        this.xqsl = xqsl;
    }

    @Basic
    @Column(name = "YCDHS")
    public Integer getYcdhs() {
        return ycdhs;
    }

    public void setYcdhs(Integer ycdhs) {
        this.ycdhs = ycdhs;
    }

    @Basic
    @Column(name = "YFHRQ")
    public Date getYfhrq() {
        return yfhrq;
    }

    public void setYfhrq(Date yfhrq) {
        this.yfhrq = yfhrq;
    }

    @Basic
    @Column(name = "YHDCS")
    public Integer getYhdcs() {
        return yhdcs;
    }

    public void setYhdcs(Integer yhdcs) {
        this.yhdcs = yhdcs;
    }

    @Basic
    @Column(name = "YHDJS")
    public Integer getYhdjs() {
        return yhdjs;
    }

    public void setYhdjs(Integer yhdjs) {
        this.yhdjs = yhdjs;
    }

    @Basic
    @Column(name = "YJRW_ID")
    public String getYjrwId() {
        return yjrwId;
    }

    public void setYjrwId(String yjrwId) {
        this.yjrwId = yjrwId;
    }

    @Basic
    @Column(name = "YPYGS")
    public Integer getYpygs() {
        return ypygs;
    }

    public void setYpygs(Integer ypygs) {
        this.ypygs = ypygs;
    }

    @Basic
    @Column(name = "YSDW")
    public String getYsdw() {
        return ysdw;
    }

    public void setYsdw(String ysdw) {
        this.ysdw = ysdw;
    }

    @Basic
    @Column(name = "YSSJ")
    public Date getYssj() {
        return yssj;
    }

    public void setYssj(Date yssj) {
        this.yssj = yssj;
    }

    @Basic
    @Column(name = "YXJ")
    public Integer getYxj() {
        return yxj;
    }

    public void setYxj(Integer yxj) {
        this.yxj = yxj;
    }

    @Basic
    @Column(name = "ZBDW")
    public String getZbdw() {
        return zbdw;
    }

    public void setZbdw(String zbdw) {
        this.zbdw = zbdw;
    }

    @Basic
    @Column(name = "ZLDJ")
    public String getZldj() {
        return zldj;
    }

    public void setZldj(String zldj) {
        this.zldj = zldj;
    }

    @Basic
    @Column(name = "ZXBZ")
    public String getZxbz() {
        return zxbz;
    }

    public void setZxbz(String zxbz) {
        this.zxbz = zxbz;
    }

    @Basic
    @Column(name = "L_YJRWID")
    public String getlYjrwid() {
        return lYjrwid;
    }

    public void setlYjrwid(String lYjrwid) {
        this.lYjrwid = lYjrwid;
    }

    @Basic
    @Column(name = "CPBM")
    public String getCpbm() {
        return cpbm;
    }

    public void setCpbm(String cpbm) {
        this.cpbm = cpbm;
    }

    @Basic
    @Column(name = "CPLX")
    public String getCplx() {
        return cplx;
    }

    public void setCplx(String cplx) {
        this.cplx = cplx;
    }

    @Basic
    @Column(name = "CPMC")
    public String getCpmc() {
        return cpmc;
    }

    public void setCpmc(String cpmc) {
        this.cpmc = cpmc;
    }

    @Basic
    @Column(name = "DDBH")
    public String getDdbh() {
        return ddbh;
    }

    public void setDdbh(String ddbh) {
        this.ddbh = ddbh;
    }

    @Basic
    @Column(name = "HSJG")
    public String getHsjg() {
        return hsjg;
    }

    public void setHsjg(String hsjg) {
        this.hsjg = hsjg;
    }

    @Basic
    @Column(name = "JJYQ")
    public String getJjyq() {
        return jjyq;
    }

    public void setJjyq(String jjyq) {
        this.jjyq = jjyq;
    }

    @Basic
    @Column(name = "JZYQ")
    public String getJzyq() {
        return jzyq;
    }

    public void setJzyq(String jzyq) {
        this.jzyq = jzyq;
    }

    @Basic
    @Column(name = "LJDZ")
    public String getLjdz() {
        return ljdz;
    }

    public void setLjdz(String ljdz) {
        this.ljdz = ljdz;
    }

    @Basic
    @Column(name = "LY")
    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    @Basic
    @Column(name = "QDRQ")
    public Date getQdrq() {
        return qdrq;
    }

    public void setQdrq(Date qdrq) {
        this.qdrq = qdrq;
    }

    @Basic
    @Column(name = "SCJL")
    public String getScjl() {
        return scjl;
    }

    public void setScjl(String scjl) {
        this.scjl = scjl;
    }

    @Basic
    @Column(name = "XMGZD")
    public String getXmgzd() {
        return xmgzd;
    }

    public void setXmgzd(String xmgzd) {
        this.xmgzd = xmgzd;
    }

    @Basic
    @Column(name = "YHDDH")
    public String getYhddh() {
        return yhddh;
    }

    public void setYhddh(String yhddh) {
        this.yhddh = yhddh;
    }

    @Basic
    @Column(name = "YHDW")
    public String getYhdw() {
        return yhdw;
    }

    public void setYhdw(String yhdw) {
        this.yhdw = yhdw;
    }

    @Basic
    @Column(name = "YHFL")
    public String getYhfl() {
        return yhfl;
    }

    public void setYhfl(String yhfl) {
        this.yhfl = yhfl;
    }

    @Basic
    @Column(name = "YQJZBDW")
    public String getYqjzbdw() {
        return yqjzbdw;
    }

    public void setYqjzbdw(String yqjzbdw) {
        this.yqjzbdw = yqjzbdw;
    }

    @Basic
    @Column(name = "YSYQ")
    public String getYsyq() {
        return ysyq;
    }

    public void setYsyq(String ysyq) {
        this.ysyq = ysyq;
    }

    @Basic
    @Column(name = "YZBM")
    public String getYzbm() {
        return yzbm;
    }

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    @Basic
    @Column(name = "YZBMID")
    public String getYzbmid() {
        return yzbmid;
    }

    public void setYzbmid(String yzbmid) {
        this.yzbmid = yzbmid;
    }

    @Basic
    @Column(name = "ZZJYXS")
    public String getZzjyxs() {
        return zzjyxs;
    }

    public void setZzjyxs(String zzjyxs) {
        this.zzjyxs = zzjyxs;
    }

    @Basic
    @Column(name = "XMJSSJ")
    public Date getXmjssj() {
        return xmjssj;
    }

    public void setXmjssj(Date xmjssj) {
        this.xmjssj = xmjssj;
    }

    @Basic
    @Column(name = "XMKSSJ")
    public Date getXmkssj() {
        return xmkssj;
    }

    public void setXmkssj(Date xmkssj) {
        this.xmkssj = xmkssj;
    }

    @Basic
    @Column(name = "XMLX")
    public String getXmlx() {
        return xmlx;
    }

    public void setXmlx(String xmlx) {
        this.xmlx = xmlx;
    }

    @Basic
    @Column(name = "XMMC")
    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    @Basic
    @Column(name = "CKBH")
    public String getCkbh() {
        return ckbh;
    }

    public void setCkbh(String ckbh) {
        this.ckbh = ckbh;
    }

    @Basic
    @Column(name = "CSDDFK")
    public String getCsddfk() {
        return csddfk;
    }

    public void setCsddfk(String csddfk) {
        this.csddfk = csddfk;
    }

    @Basic
    @Column(name = "DDLB")
    public String getDdlb() {
        return ddlb;
    }

    public void setDdlb(String ddlb) {
        this.ddlb = ddlb;
    }

    @Basic
    @Column(name = "YQFHWCRQ")
    public Date getYqfhwcrq() {
        return yqfhwcrq;
    }

    public void setYqfhwcrq(Date yqfhwcrq) {
        this.yqfhwcrq = yqfhwcrq;
    }

    @Basic
    @Column(name = "YQJJWCRQ")
    public Date getYqjjwcrq() {
        return yqjjwcrq;
    }

    public void setYqjjwcrq(Date yqjjwcrq) {
        this.yqjjwcrq = yqjjwcrq;
    }

    @Basic
    @Column(name = "CKBZ")
    public String getCkbz() {
        return ckbz;
    }

    public void setCkbz(String ckbz) {
        this.ckbz = ckbz;
    }

    @Basic
    @Column(name = "CKSJ")
    public Date getCksj() {
        return cksj;
    }

    public void setCksj(Date cksj) {
        this.cksj = cksj;
    }

    @Basic
    @Column(name = "JYWCSJ")
    public Date getJywcsj() {
        return jywcsj;
    }

    public void setJywcsj(Date jywcsj) {
        this.jywcsj = jywcsj;
    }


}
