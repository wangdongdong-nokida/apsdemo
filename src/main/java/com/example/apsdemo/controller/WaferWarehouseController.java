package com.example.apsdemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.apsdemo.dao.businessObject.ScheduleTestItem;
import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferGearWarehouseService;
import com.example.apsdemo.service.WaferModelWarehouseService;
import com.example.apsdemo.service.WaferWarehouseService;
import com.example.apsdemo.utils.Tools;
import com.google.gson.JsonObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/waferWarehouse")
public class WaferWarehouseController {
    @Autowired
    WaferWarehouseService service;

    @Autowired
    WaferModelWarehouseService waferModelWarehouseService;

    @Autowired
    WaferGearWarehouseService waferGearWarehouseService;

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
//        Map<String, Object> map = (Map<String, Object>) params.computeIfAbsent("params", key -> new HashMap<String, Object>());
//        map.put("stockName", "芯片库");

        int current = Integer.parseInt(params.get("current").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        Map<String, Object> insideMap = new HashMap<>();
        Map<String, String> insideParams = (Map<String, String>) params.computeIfAbsent("params", k -> new HashMap<>());
        insideMap.put("WLXT", "圆片");
        insideMap.put("stockName", "芯片库");

        for (Map.Entry entry : insideParams.entrySet()) {
            if (!Objects.equals("_timestamp", entry.getKey().toString())) {
                insideMap.put("waferModelWarehouse-waferWarehouse-" + entry.getKey(), entry.getValue());
            }
        }

        params.put("params", insideMap);

        Result gearResult = Tools.getResult(params, waferGearWarehouseService);


        Set<WaferWarehouse> waferWarehouses = new HashSet<>();
        for (WaferGearWarehouse waferGearWarehouse : (List<WaferGearWarehouse>) gearResult.getData()) {
            if (waferGearWarehouse.getWaferModelWarehouse() != null && waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse() != null) {
                waferWarehouses.add(waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse());
            }
        }

        int start = (current - 1) * pageSize;
        int end = (current) * pageSize;

        int subStart = 0 <= start && start < waferWarehouses.size() ? start : 0;
        int subEnd = 0 <= end && end < waferWarehouses.size() ? end : (0 <= start && end > waferWarehouses.size() ? waferWarehouses.size() : 0);

        List<WaferWarehouse> resultData = new LinkedList<>(waferWarehouses).subList(subStart, subEnd);

        for (WaferWarehouse waferWarehouse : resultData) {

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
                        String orderName = salesOrder.getlDdname();
                        Integer orderQuantity = salesOrder.getDgsl();
                        salesOrderBuilder.append(orderName == null ? "" : orderName).append(";");

                        LHt lHt = salesOrder.getlHt();

                        if (lHt != null) {
                            String contact = lHt.getlHtname();
                            String customer = lHt.getKh();
                            String orderType = lHt.getDdlx();
                            quantityBuilder.append(orderQuantity == null ? "" : orderQuantity).append(";");
                            contractBuilder.append(contact == null ? "" : contact).append(";");
                            customerBuilder.append(customer == null ? "" : customer).append(";");
                            salesOrderTypeBuilder.append(orderType == null ? "" : orderType).append(";");
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


        return new Result(resultData, waferWarehouses.size(), true, pageSize, current);
    }

    @RequestMapping(path = "/findProductByParams")
    public Result findProductByParams(@RequestBody Map params) {
        if (params == null || params.get("params") == null || ((Map) params.get("params")).get("waferWarehouse-ID") == null) {
            return new Result();
        }

        Result result = Tools.getResult(params, waferModelWarehouseService);
        List<WaferModelWarehouse> data = result.getData();
        Set<WaferModelWarehouse> waferModelWarehouseSet = new HashSet<>();
        if (data != null && data.size() > 0) {
            for (WaferModelWarehouse product : data) {
                StringBuilder salesOrders = new StringBuilder();

                Map<String, Integer> quantity = new HashMap<>();
                for (WaferGearWarehouse waferGearWarehouse : product.getWaferGearWarehouses()) {
                    int gearQuantity = waferGearWarehouse.getQuantity() != null ? Integer.parseInt(waferGearWarehouse.getQuantity()) : 0;
                    if (gearQuantity <= 0) {
                        continue;
                    }
                    waferModelWarehouseSet.add(waferGearWarehouse.getWaferModelWarehouse());


                    Set<Occupy> occupies = waferGearWarehouse.getOccupies();
                    for (Occupy occupy : occupies) {
                        if (occupy.getSalesOrder() != null && occupy.getSalesOrder().getlDdname() != null) {
                            salesOrders.append(occupy.getSalesOrder().getlDdname());
                        }
                    }
//                    if("圆片".equals(waferGearWarehouse.getWLXT())){

                    Integer integer = quantity.computeIfAbsent(waferGearWarehouse.getXH(), k -> 0);
                    integer += gearQuantity;
                    quantity.put(waferGearWarehouse.getXH(), integer);
//                    }
                }
                product.setModelNrs(quantity.keySet().toArray());
                product.setBindSalesOrder(salesOrders.toString());
                product.setQuantity(quantity.values().toString());
            }
        }
        result.setData(new ArrayList<>(waferModelWarehouseSet));
        result.setTotal(result.getData() != null ? result.getData().size() : 0);
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
