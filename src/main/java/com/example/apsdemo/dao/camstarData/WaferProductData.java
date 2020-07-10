package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@MappedSuperclass
public class WaferProductData {
    @Id
    @Column(name = "L_XHID")
    protected String ID;
    @Column(name = "DLXH")
    protected String circuitNo;
    @Column(name = "SJS")
    protected String designer;
    @Column(name = "SL")
    protected Integer quantity;
}
