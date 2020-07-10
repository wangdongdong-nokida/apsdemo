package com.example.apsdemo.dao.camstarData;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class WaferGearWarehouseData {
    @Id
    protected String ID;
    protected String GearNr;
    protected int quantity;
}
