package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.logicSchedule.EquipmentCalendarBitSet;
import com.example.apsdemo.dao.businessObject.ScheduleTaskLine;
import com.example.apsdemo.service.ScheduleTaskLineService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Controller
public class ScheduleMethod {
    @Autowired
    ScheduleTaskLineService scheduleTaskLineService;
    @Autowired
    EquipmentCalendarBitSet equipmentCalendarBitSet;

    @NotNull
    @Transactional
    synchronized ScheduleTaskLine getScheduleTaskLine(Equipment equipment) {
        ScheduleTaskLine line = equipment.getScheduleTaskLine();
        if (line == null) {
            line = new ScheduleTaskLine();
            line.setEquipment(equipment);
            equipment.setScheduleTaskLine(line);
            scheduleTaskLineService.save(line);
        }
        return line;
    }


    synchronized void updateScheduleLineDate(Equipment equipment) {
        ScheduleTaskLine line = equipment.getScheduleTaskLine();
        EquipmentCalendarBitSet.BitSetWrapper wrapper = getBitSetWrapper(equipment);
        ScheduleTaskLine.ScheduleLine scheduleLine = line.getScheduleLine();
        scheduleLine.calcScheduleLineDate(wrapper);
    }

    synchronized EquipmentCalendarBitSet.BitSetWrapper getBitSetWrapper(Equipment equipment) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 2);
        ScheduleTaskLine line = getScheduleTaskLine(equipment);
        Date standardTime = line.getStandardTime();
        if ((line.getFirst() != null && line.getFirst().getStartDate() != null)) {
            if (standardTime != null) {
                start.setTime(standardTime.before(line.getFirst().getStartDate()) ? standardTime : line.getFirst().getStartDate());
            } else {
                start.setTime(line.getFirst().getStartDate());
            }
        }
        return equipmentCalendarBitSet.initialize(start, end, equipment);
    }

}
