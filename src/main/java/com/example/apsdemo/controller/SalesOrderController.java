package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.SalesOrder;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.SalesOrderService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/salesOrder")
public class SalesOrderController {

    @Autowired
    SalesOrderService salesOrderService;

    @RequestMapping("/findSalesOrder")
    public Result findSalesOrders(@RequestBody Map params) {
        return Tools.getResult(params, salesOrderService);
    }


}
