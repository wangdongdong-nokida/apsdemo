package com.example.apsdemo.dao.camstarData;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public class LTgfsData {
    private String lTgfsid;
    private String lTgfsname;

    @Id
    @Column(name = "L_TGFSID")
    public String getlTgfsid() {
        return lTgfsid;
    }

    public void setlTgfsid(String lTgfsid) {
        this.lTgfsid = lTgfsid;
    }

    @Basic
    @Column(name = "L_TGFSNAME")
    public String getlTgfsname() {
        return lTgfsname;
    }

    public void setlTgfsname(String lTgfsname) {
        this.lTgfsname = lTgfsname;
    }

}
