package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.ScheduleTestItem;
import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferModelWarehouseService;
import com.example.apsdemo.service.WaferWarehouseService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
        Result result = Tools.getResult(params, service);


        if (result.getData().size() > 0) {
            List<WaferWarehouse> waferWarehouses = result.getData();
            for (WaferWarehouse waferWarehouse : waferWarehouses) {
                TestScribingCenter testScribingCenter = waferWarehouse.getTestScribingCenter();
                if (testScribingCenter != null) {
                    Set<ScheduleTestItem> testItems = testScribingCenter.getScheduleTestItem();
                    Set<SecondOrder> orders = new HashSet<>();
                    for (ScheduleTestItem item : testItems) {
                        SecondOrder secondOrder = item.getSecondOrder();
                        if (secondOrder != null) {
                            orders.add(secondOrder);
                        }
                    }
                    if (orders.size() > 0) {
                        StringBuilder secondOrder = new StringBuilder();
                        for (SecondOrder order : orders) {
                            if (order.getName() != null && !"".equals(order.getName())) {
                                secondOrder.append(order.getName()).append(";");
                            }
                        }
                        waferWarehouse.setBindingSecondOrders(secondOrder.toString());
                    }
                }
            }
        }
        return result;
    }


    @RequestMapping(path = "/findWaferWarehouse")
    public Result findWaferWarehouse(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = (Map<String, Object>) params.computeIfAbsent("params", key -> new HashMap<String, Object>());
//        map.put("stockName", "芯片库");
        Result result = Tools.getResult(params, service);
        List<WaferWarehouse> waferWarehouses = result.getData();
        if (waferWarehouses.size() > 0) {
            for (WaferWarehouse waferWarehouse : waferWarehouses) {
                StringBuilder salesOrderBuilder = new StringBuilder();
                StringBuilder quantityBuilder = new StringBuilder();
                StringBuilder contractBuilder = new StringBuilder();
                StringBuilder customerBuilder = new StringBuilder();
                StringBuilder salesOrderTypeBuilder = new StringBuilder();
                Set<WaferModelWarehouse> waferModelWarehouses = waferWarehouse.getWaferModelWarehouse();
                for (WaferModelWarehouse waferModelWarehouse : waferModelWarehouses) {
                    Set<SalesOrder> salesOrders = new HashSet<>();
                    for (WaferGearWarehouse waferGearWarehouse : waferModelWarehouse.getWaferGearWarehouses()) {
                        Set<Occupy> occupies = waferGearWarehouse.getOccupies();
                        for (Occupy occupy : occupies) {
                            SalesOrder salesOrder = occupy.getSalesOrder();
                            salesOrders.add(salesOrder);
                        }
                    }
                    for (SalesOrder salesOrder : salesOrders) {
                        if (salesOrder != null) {
                            salesOrderBuilder.append(salesOrder.getDdh()).append(";");
                            quantityBuilder.append(salesOrder.getDdsl()).append(";");
                            salesOrderTypeBuilder.append(salesOrder.getDdlb()).append(";");
                            LHt lHt = salesOrder.getlHt();
                            if (lHt != null) {
                                contractBuilder.append(lHt.getHth()).append(";");
                                customerBuilder.append(lHt.getKh()).append(";");
                            }
                        }
                    }
                }
                waferWarehouse.setBindingContracts(contractBuilder.toString());
                waferWarehouse.setBindingCustomers(customerBuilder.toString());
                waferWarehouse.setBindingQuantity(quantityBuilder.toString());
                waferWarehouse.setBindingSalesOrders(salesOrderBuilder.toString());
                waferWarehouse.setBindingSalesOrderType(salesOrderTypeBuilder.toString());
            }
        }


        return result;
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
                StringBuilder salesOrders = new StringBuilder();
                int quantity = 0;
                for (WaferGearWarehouse waferGearWarehouse : product.getWaferGearWarehouses()) {
                    Set<Occupy> occupies = waferGearWarehouse.getOccupies();
                    for (Occupy occupy : occupies) {
                        if (occupy.getSalesOrder() != null && occupy.getSalesOrder().getDdh() != null) {
                            salesOrders.append(occupy.getSalesOrder().getDdh());
                        }
                    }
//                    if("圆片".equals(waferGearWarehouse.getWLXT())){
                    quantity += waferGearWarehouse.getQuantity();
//                    }
                }
                product.setBindSalesOrder(salesOrders.toString());
                product.setQuantity(quantity);
            }
        }
        return result;
    }

    @PostMapping(path = "/getWaferAll")
    public Result getWaferAll(@RequestBody Map<String, Object> params) {

        if (params.get("waferNr") != null) {
            Map parameter = (Map) params.computeIfAbsent("params", k -> new HashMap<>());
            parameter.put("waferNr", params.get("waferNr"));
        }
        return Tools.getResult(params, service);
    }

}
