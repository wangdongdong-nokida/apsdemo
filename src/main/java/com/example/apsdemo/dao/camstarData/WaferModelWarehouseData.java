package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class WaferModelWarehouseData {
   @Id
    protected String ID;
    protected String modelNr;
}
