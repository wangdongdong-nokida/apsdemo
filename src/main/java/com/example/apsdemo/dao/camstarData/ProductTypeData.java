package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class ProductTypeData {
    @Id
    @Column(name = "PRODUCTTYPEID")
    protected String ID;
    @Column(name = "PRODUCTTYPENAME")
    protected String name;

}
