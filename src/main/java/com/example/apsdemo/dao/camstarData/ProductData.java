package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;


@Data
@MappedSuperclass
@Table(name = "PRODUCT", catalog = "")
public class ProductData {
    @Id
    @Column(name = "PRODUCTID")
    private String ID;
    @Column(name = "CPBH")
    private String nr;
    @Column(name = "CPMC")
    private String name;
    @Column(name = "CPXH")
    private String modelNr;
    @Column(name = "CPZT")
    private String status;
}
