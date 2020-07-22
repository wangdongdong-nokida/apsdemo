package com.example.apsdemo.dao.businessObject;

import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.businessData.EquipmentCalendarData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "A_EquipmentCalendar")
public class EquipmentCalendar extends EquipmentCalendarData {

    @JsonIgnore
    @ManyToOne(targetEntity = Equipment.class)
    private Equipment equipment;

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

}
