package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
@Data
public class CircuitTypeData {

    @Id
    @Column(name = "L_DLLXID")
    private String ID;

    @Column(name = "L_DLLXNAME")
    private String name;
}
