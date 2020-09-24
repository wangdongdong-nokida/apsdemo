package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public class OccupyData {

    @Id
    @Column(name = "L_ZLID")
    private String ID;
    @Column(name = "YPBZ")
    private String ypbz;
    @Column(name = "ZLSL")
    private Integer zlsl;

}
