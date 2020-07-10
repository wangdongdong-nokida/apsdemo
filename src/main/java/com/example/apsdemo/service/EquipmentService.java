package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.mapper.EquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.*;

@Service
public class EquipmentService extends BaseService<EquipmentMapper>{
    @Autowired
    EquipmentMapper mapper;

    public Optional<Equipment> findById(String id) {
        return mapper.findById(id);
    }

    @Override
    EquipmentMapper getMapper() {
        return mapper;
    }


//    public Map<String, Map<Integer, Set<EquipmentCalendar>>> getClassifiedCalendar(){
//        return classifyCalendar();
//    }


//    private Map<String, Map<Integer, Set<EquipmentCalendar>>> classifyCalendar() {
//        Map<String, Map<Integer, Set<EquipmentCalendar>>> equipmentCalendars = new HashMap<>();
//        List<Equipment> equipments = mapper.findAll();
//        for (Equipment equipment : equipments) {
//            Map<Integer, Set<EquipmentCalendar>> calendarMapList = new HashMap<>();
//            Set<EquipmentCalendar> calendars = equipment.getCalendars();
//            for (EquipmentCalendar calendar : calendars) {
//                classifyCalendar(calendarMapList, calendar);
//            }
//            equipmentCalendars.put(equipment.getID(), calendarMapList);
//        }
//        return equipmentCalendars;
//    }


//    private void classifyCalendar(Map<Integer, Set<EquipmentCalendar>> calendarMapList, EquipmentCalendar calendar) {
//        boolean repeat = false;
//        if (calendar.isMonday()) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(Calendar.MONDAY, key -> new HashSet<>());
//            calendarList.add(calendar);
//            repeat = true;
//        }
//        if (calendar.isTuesday()) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(Calendar.TUESDAY, key -> new HashSet<>());
//            calendarList.add(calendar);
//            repeat = true;
//        }
//        if (calendar.isWednesday()) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(Calendar.WEDNESDAY, key -> new HashSet<>());
//            calendarList.add(calendar);
//            repeat = true;
//        }
//        if (calendar.isThursday()) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(Calendar.THURSDAY, key -> new HashSet<>());
//            calendarList.add(calendar);
//            repeat = true;
//        }
//        if (calendar.isFriday()) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(Calendar.FRIDAY, key -> new HashSet<>());
//            calendarList.add(calendar);
//            repeat = true;
//        }
//        if (calendar.isSaturday()) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(Calendar.SATURDAY, key -> new HashSet<>());
//            calendarList.add(calendar);
//            repeat = true;
//        }
//        if (calendar.isSunday()) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(Calendar.SUNDAY, key -> new HashSet<>());
//            calendarList.add(calendar);
//            repeat = true;
//        }
//        if (!repeat) {
//            Set<EquipmentCalendar> calendarList = calendarMapList.computeIfAbsent(0, key -> new HashSet<>());
//            calendarList.add(calendar);
//        }
//    }
}
