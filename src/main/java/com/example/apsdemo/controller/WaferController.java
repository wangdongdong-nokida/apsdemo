package com.example.apsdemo.controller;

import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferFatherService;
import com.example.apsdemo.service.WaferService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(path = "/wafer")
public class WaferController {
    @Autowired
    WaferService service;

    @Autowired
    WaferFatherService fatherService;

}
