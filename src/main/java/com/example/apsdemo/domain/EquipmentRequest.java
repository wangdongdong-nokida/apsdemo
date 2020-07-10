package com.example.apsdemo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentRequest extends RequestPage{
    private String name;
    private String type;
}
