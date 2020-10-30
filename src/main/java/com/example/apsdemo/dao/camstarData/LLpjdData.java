package com.example.apsdemo.dao.camstarData;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
public class LLpjdData {
    private String bh;
    private String jdb;
    private String lLpjdid;
    private String lLpjdname;
    private String lpdh;
    private String lx;
    private String pch;
    private Date rpsj;
    private Time createTime;
    private String userId;
    private String yph;

    @Basic
    @Column(name = "BH")
    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    @Basic
    @Column(name = "JDB")
    public String getJdb() {
        return jdb;
    }

    public void setJdb(String jdb) {
        this.jdb = jdb;
    }

    @Id
    @Column(name = "L_LPJDID")
    public String getlLpjdid() {
        return lLpjdid;
    }

    public void setlLpjdid(String lLpjdid) {
        this.lLpjdid = lLpjdid;
    }

    @Basic
    @Column(name = "L_LPJDNAME")
    public String getlLpjdname() {
        return lLpjdname;
    }

    public void setlLpjdname(String lLpjdname) {
        this.lLpjdname = lLpjdname;
    }

    @Basic
    @Column(name = "LPDH")
    public String getLpdh() {
        return lpdh;
    }

    public void setLpdh(String lpdh) {
        this.lpdh = lpdh;
    }

    @Basic
    @Column(name = "LX")
    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    @Basic
    @Column(name = "PCH")
    public String getPch() {
        return pch;
    }

    public void setPch(String pch) {
        this.pch = pch;
    }

    @Basic
    @Column(name = "RPSJ")
    public Date getRpsj() {
        return rpsj;
    }

    public void setRpsj(Date rpsj) {
        this.rpsj = rpsj;
    }

    @Basic
    @Column(name = "CREATE_TIME")
    public Time getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Time createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "YPH")
    public String getYph() {
        return yph;
    }

    public void setYph(String yph) {
        this.yph = yph;
    }

}
