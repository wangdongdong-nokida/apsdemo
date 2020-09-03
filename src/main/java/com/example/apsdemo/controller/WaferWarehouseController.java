package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferModelWarehouseService;
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
import java.util.Set;

@RestController
@RequestMapping(path = "/waferWarehouse")
public class WaferWarehouseController {
    @Autowired
    WaferWarehouseService service;

    @Autowired
    WaferModelWarehouseService waferModelWarehouseService;

    public List<WaferWarehouse> findAll(RequestPage<WaferWarehouse> requestPage) {
        Specification specification = Tools.getSpecification(new HashMap<>());
        return service.findAll(specification);
    }

    @RequestMapping(path = "/findAllByPage")
    public Result findAllByPage(@RequestBody Map<String, Object> params) {
        if (params.get("params") == null || ((Map) params.get("params")).get("waferNr") == null) {
            return new Result();
        }
        return Tools.getResult(params, service);
    }


    @RequestMapping(path = "/findWaferWarehouse")
    public Result findWaferWarehouse(@RequestBody Map<String, Object> params) {
        return Tools.getResult(params, service);
    }

    @RequestMapping(path = "/findProductByParams")
    public Result findProductByParams(@RequestBody Map params) {
        if (params == null || params.get("params") == null || ((Map) params.get("params")).get("waferWarehouse-ID") == null) {
            return new Result();
        }

        Result result = Tools.getResult(params, waferModelWarehouseService);
        List<WaferModelWarehouse> data = result.getData();
        if (data != null && data.size() > 0) {
            for (WaferModelWarehouse product : data) {
                StringBuffer salesOeders = new StringBuffer();
                for (WaferGearWarehouse waferGearWarehouse : product.getWaferGearWarehouses()) {
                    Set<Occupy> occupies = waferGearWarehouse.getOccupies();
                    for (Occupy occupy : occupies) {
                        if (occupy.getSalesOrder() != null &&occupy.getSalesOrder().getDdh()!=null){
                            salesOeders.append(occupy.getSalesOrder().getDdh());
                        }
                    }
                }
                product.setBindSalesOrder(salesOeders.toString());
            }
        }
        return result;
    }

}
