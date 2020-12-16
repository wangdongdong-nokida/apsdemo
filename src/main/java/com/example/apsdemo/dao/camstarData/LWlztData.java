package com.example.apsdemo.dao.camstarData;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public class LWlztData {

    private String lWlztid;
    private String lWlztname;

    @Id
    @Column(name = "L_WLZTID")
    public String getlWlztid() {
        return lWlztid;
    }

    public void setlWlztid(String lWlztid) {
        this.lWlztid = lWlztid;
    }

    @Basic
    @Column(name = "L_WLZTNAME")
    public String getlWlztname() {
        return lWlztname;
    }

    public void setlWlztname(String lWlztname) {
        this.lWlztname = lWlztname;
    }




}
