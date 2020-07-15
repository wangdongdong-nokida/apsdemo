package com.example.apsdemo.domain;

import lombok.Data;

import java.util.List;


@Data
public class TestItemChangeEquipment {
   private List<Long> ids;
   private String equipmentID;
   private String belongEquipmentID;
}
