package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
@Data
public class TestParameterData {

    @Column(name = "L_CSCSID")
    @Id
    protected String lCscsid;
    @Column(name = "L_CSCSNAME")
    protected String lCscsname;
    @Column(name = "NOTES")
    protected String notes;
    @Column(name = "CSCS")
    protected String name;
    @Column(name = "CSLX")
    protected String type;

}
