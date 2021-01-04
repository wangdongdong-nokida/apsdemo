package com.example.apsdemo.controller;

import com.example.apsdemo.service.WaferFatherService;
import com.example.apsdemo.service.WaferService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "/wafer")
public class WaferController {
    @Autowired
    WaferService service;

    @Autowired
    WaferFatherService fatherService;

}
