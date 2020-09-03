package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class WaferData {
    @Id
    @Column(name = "L_BHID")
    protected String ID;
    @Column(name = "L_BHNAME")
    protected String nr;
    @Column(name = "PFZR")
    protected String responsiblePerson;
    @Column(name="HPFS")
    protected String ScribingType;
    @Column(name="DYS")
    protected String unitNumber;
}
