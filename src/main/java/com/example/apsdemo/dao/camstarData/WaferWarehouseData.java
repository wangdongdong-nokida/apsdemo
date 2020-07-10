package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class WaferWarehouseData {
    @Id
    protected String ID;
    protected String waferNr;
    protected String sliceNr;
}
