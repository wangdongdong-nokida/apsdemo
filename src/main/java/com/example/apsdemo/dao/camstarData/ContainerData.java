package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@MappedSuperclass
@Data
public class ContainerData {

    @Id
    @Column(name = "CONTAINERID")
    protected String ID;
}
