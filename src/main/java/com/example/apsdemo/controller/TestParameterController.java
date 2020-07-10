package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.TestParameter;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.TestParameterService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/testParameter")
public class TestParameterController {
    @Autowired
    TestParameterService service;

    @RequestMapping(path = "/findAllByParams")
    public Result findAll(@RequestBody Map<String,Object> params){

        Map<String,Object> map=new HashMap<>();
        return Tools.getResult(params,service);
    }

}
