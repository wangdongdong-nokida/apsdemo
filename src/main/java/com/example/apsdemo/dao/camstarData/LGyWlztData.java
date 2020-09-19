package com.example.apsdemo.dao.camstarData;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public class LGyWlztData {
    private String bz;
    private String lGyWlztid;
    private String lGyWlztname;
    private String lWlxt;
//    private String lWlztid;
    private String notes;
//    private String workflowid;

    @Basic
    @Column(name = "BZ")
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    @Id
    @Column(name = "L_GY_WLZTID")
    public String getlGyWlztid() {
        return lGyWlztid;
    }

    public void setlGyWlztid(String lGyWlztid) {
        this.lGyWlztid = lGyWlztid;
    }

    @Basic
    @Column(name = "L_GY_WLZTNAME")
    public String getlGyWlztname() {
        return lGyWlztname;
    }

    public void setlGyWlztname(String lGyWlztname) {
        this.lGyWlztname = lGyWlztname;
    }

    @Basic
    @Column(name = "L_WLXT")
    public String getlWlxt() {
        return lWlxt;
    }

    public void setlWlxt(String lWlxt) {
        this.lWlxt = lWlxt;
    }

    @Basic
    @Column(name = "NOTES")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
