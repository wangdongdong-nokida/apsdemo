package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@MappedSuperclass
public class ProductBaseData {
    @Id
    @Column(name = "PRODUCTBASEID")
    protected String ID;
    @Column(name = "PRODUCTNAME")
    protected String name;
}
