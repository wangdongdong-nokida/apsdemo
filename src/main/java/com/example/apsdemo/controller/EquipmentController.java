package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.EquipmentService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import javax.swing.text.html.Option;
import java.util.*;

@RestController
@RequestMapping(path = "/equipment")
public class EquipmentController {

    @Autowired
    EquipmentService service;

    @RequestMapping(path = "/getAllByParams")
    public Result getEquipment(@RequestBody Map<String, Object> requestPage) {
        return Tools.getResult(requestPage, service);
    }

    @GetMapping(path = "/getByUser")
    public List<Equipment> getByUser() {
        return service.findAll();
    }

    @GetMapping(path = "/getEndDate")
    public Date getEndDate(String id) {
        Date date = new Date();
        if (id!=null) {
            Optional<Equipment> equipmentOption = service.findById(id);
            if (equipmentOption.isPresent() && equipmentOption.get().getScheduleTaskLine() != null) {
                Date lastTime=equipmentOption.get().getScheduleTaskLine().getLastTime();
                return lastTime==null?date:lastTime;
            }
        }
        return date;
    }
}
