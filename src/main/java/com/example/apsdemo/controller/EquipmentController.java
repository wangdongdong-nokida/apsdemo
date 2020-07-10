package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.EquipmentService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping(path = "/equipment")
public class EquipmentController {

    @Autowired
    EquipmentService service;

    @RequestMapping(path = "/getAllByParams")
    public Result getEquipment(@RequestBody Map<String,Object> requestPage) {
        return Tools.getResult(requestPage,service);
    }

    @GetMapping(path = "/getByUser")
    public List<Equipment> getByUser(){
       return service.findAll();
    }

}
