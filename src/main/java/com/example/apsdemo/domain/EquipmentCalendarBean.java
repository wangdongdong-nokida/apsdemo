package com.example.apsdemo.domain;

import com.example.apsdemo.dao.businessData.EquipmentCalendarData;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EquipmentCalendarBean{

    private EquipmentCalendarData data;

    @NotNull(message = "请选中一台设备！！！")
    private String equipmentId;
}
