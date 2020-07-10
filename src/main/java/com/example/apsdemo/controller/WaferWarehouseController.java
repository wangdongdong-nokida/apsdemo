package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferWarehouseService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/waferWarehouse")
public class WaferWarehouseController {
    @Autowired
    WaferWarehouseService service;

    public List<WaferWarehouse> findAll(RequestPage<WaferWarehouse> requestPage) {
        Specification specification = Tools.getSpecification(new HashMap<>());
        return service.findAll(specification);
    }

    @RequestMapping(path = "/findAllByPage")
    public Result findAllByPage(@RequestBody Map<String, Object> params) {
        if(params.get("params")==null||((Map)params.get("params")).get("waferNr")==null){
            return new Result();
        }
        return Tools.getResult(params, service);
    }
}
