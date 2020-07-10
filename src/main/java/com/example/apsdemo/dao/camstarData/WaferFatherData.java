package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@MappedSuperclass

public class WaferFatherData {

    @Id
    @Column(name = "L_FBHID")
    protected String ID;
    @Column(name = "L_FBHNAME")
    protected String nr;
    @Column(name = "NOTES")
    protected String notes;

}
