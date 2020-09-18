package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class GearPickingOrderData extends DataBase {

}
