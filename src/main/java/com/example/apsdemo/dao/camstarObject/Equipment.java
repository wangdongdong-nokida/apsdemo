package com.example.apsdemo.dao.camstarObject;

import com.example.apsdemo.dao.businessObject.EquipmentCalendar;
import com.example.apsdemo.dao.camstarData.EquipmentData;
import com.example.apsdemo.dao.businessObject.ScheduleTaskLine;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "RESOURCEGROUP")
public class Equipment  extends EquipmentData {
    @JsonIgnore
    @OneToMany(targetEntity = EquipmentCalendar.class,mappedBy = "equipment")
    private Set<EquipmentCalendar> calendars=new HashSet<>();

    @JsonIgnore
    @OneToOne(targetEntity = ScheduleTaskLine.class,mappedBy = "equipment",fetch = FetchType.LAZY )
    private ScheduleTaskLine scheduleTaskLine;

    @OneToMany(fetch= FetchType.LAZY,mappedBy = "equipment")
    @JsonIgnore
    private Set<OperationEquipment> operationEquipments=new HashSet<>();

    public Set<OperationEquipment> getOperationEquipments() {
        return operationEquipments;
    }

    public void setOperationEquipments(Set<OperationEquipment> operationEquipments) {
        this.operationEquipments = operationEquipments;
    }

    public ScheduleTaskLine getScheduleTaskLine() {
        return scheduleTaskLine;
    }

    public void setScheduleTaskLine(ScheduleTaskLine scheduleTaskLine) {
        this.scheduleTaskLine = scheduleTaskLine;
    }

    public Set<EquipmentCalendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(Set<EquipmentCalendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
