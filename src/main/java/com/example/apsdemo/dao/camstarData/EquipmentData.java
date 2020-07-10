package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@Data
@MappedSuperclass
public class EquipmentData {

    @Id
    @Column(name = "RESOURCEGROUPID")
    protected String ID;
    @Column(name = "DESCRIPTION")
    protected String description;
    @Column(name = "RESOURCEGROUPNAME")
    protected String name;
    @Column(name = "XTZT")
    protected String status;
    @Column(name = "EQUIPMENTTYPE")
    protected String type;
}
