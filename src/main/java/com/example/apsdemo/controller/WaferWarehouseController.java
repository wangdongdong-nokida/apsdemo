package com.example.apsdemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.apsdemo.dao.businessObject.*;
import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.Tools;
import com.google.gson.JsonObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.criteria.*;
import javax.swing.text.html.Option;
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

    @Autowired
    OperationService operationService;

    @Autowired
    GearPickingOrderService gearPickingOrderService;


    @RequestMapping(path = "/findAllWaferWarehouse")
    public Result findAll(@RequestBody Map<String, Object> params) {
        if (params.get("params") == null || (((Map) params.get("params")).get("waferNr") == null && ((Map) params.get("params")).get("==waferNr") == null)) {
            return new Result();
        }
        return Tools.getResult(params, service);
    }

    @RequestMapping(path = "/findAllByPage")
    public Result findAllByPage(@RequestBody Map<String, Object> params) {
        Result result = findAll(params);
        if (result.getData().size() > 0) {
            List<WaferWarehouse> waferWarehouses = result.getData();
            for (WaferWarehouse waferWarehouse : waferWarehouses) {
                TestScribingCenter testScribingCenter = waferWarehouse.getTestScribingCenter();
                waferWarehouse.setBindingScribing("未绑定明细");
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
                    waferWarehouse.setBindingScribing("已绑定明细");
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
            if (!Objects.equals("_timestamp", entry.getKey().toString()) && !Objects.equals("createState", entry.getKey().toString())) {
                insideMap.put("waferModelWarehouse-waferWarehouse-" + entry.getKey(), entry.getValue());
            }
        }

//        params.put("params", insideMap);

//        Result gearResult = Tools.getResult(params, waferGearWarehouseService);

        Specification specification = Tools.getSpecificationByParams(insideMap);
        List<WaferGearWarehouse> waferGearWarehouses = waferGearWarehouseService.findAll(specification);

        Set<WaferWarehouse> waferWarehouses = new HashSet<>();
        Set<String> unsatisfiedWaferWarehouses = new HashSet<>();
        for (WaferGearWarehouse waferGearWarehouse : (List<WaferGearWarehouse>) waferGearWarehouses) {
            if (waferGearWarehouse.getWaferModelWarehouse() != null && waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse() != null) {
                WaferWarehouse waferWarehouse = waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse();
                if (unsatisfiedWaferWarehouses.contains(waferWarehouse.getID())) {
                    continue;
                }
                waferWarehouses.add(waferWarehouse);
                if (insideParams.get("createState") != null && insideParams.get("createState").equals("created")) {
                    boolean created = isCreated(waferWarehouse);
                    if (!created) {
                        waferWarehouses.remove(waferWarehouse);
                        unsatisfiedWaferWarehouses.add(waferWarehouse.getID());
                    }
                } else {
                    boolean created = isCreated(waferWarehouse);
                    if (created) {
                        waferWarehouses.remove(waferWarehouse);
                        unsatisfiedWaferWarehouses.add(waferWarehouse.getID());
                    }
                }
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
            StringBuilder contractBriefBuilder = new StringBuilder();
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
                            String brief = lHt.getBz();
                            quantityBuilder.append(orderQuantity == null ? "" : orderQuantity).append(";");
                            contractBuilder.append(contact == null ? "" : contact).append(";");
                            customerBuilder.append(customer == null ? "" : customer).append(";");
                            salesOrderTypeBuilder.append(orderType == null ? "" : orderType).append(";");
                            contractBriefBuilder.append(brief == null ? "" : brief).append(";");
                        }
                    }
                }
            }
            waferWarehouse.setBindingContracts(contractBuilder.toString());
            waferWarehouse.setBindingCustomers(customerBuilder.toString());
            waferWarehouse.setBindingQuantity(quantityBuilder.toString());
            waferWarehouse.setBindingSalesOrders(salesOrderBuilder.toString());
            waferWarehouse.setBindingSalesOrderType(salesOrderTypeBuilder.toString());
            waferWarehouse.setBindingContractBrief(contractBriefBuilder.toString());
        }

        return new Result(resultData, waferWarehouses.size(), true, pageSize, current);
    }

    private boolean isCreated(WaferWarehouse waferWarehouse) {
        boolean created = false;
        for (WaferModelWarehouse waferModelWarehouse : waferWarehouse.getWaferModelWarehouse()) {
            for (WaferGearWarehouse waferGearWarehouse1 : waferModelWarehouse.getWaferGearWarehouses()) {
                if (waferGearWarehouse1.getGearPickingOrders().size() > 0) {
                    created = true;
                    break;
                }
            }
            if (created) {
                break;
            }
        }
        return created;
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
                    if (!"测试不合格".equals(waferGearWarehouse.getWLZT())) {
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

                        Integer integer = quantity.computeIfAbsent(waferGearWarehouse.getXH(), k -> 0);
                        integer += gearQuantity;
                        quantity.put(waferGearWarehouse.getXH(), integer);

                    }
                    product.setModelNrs(quantity.keySet().toArray());
                    product.setBindSalesOrder(salesOrders.toString());
                    product.setQuantity(quantity.values().toString());
                }
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

    @PostMapping(path = "/getWaferGearWarehouse")
    public Result getWaferGearWarehouse(@RequestBody Map<String, Object> params) {
        if (params.get("params") == null || ((Map) params.get("params")).get("waferModelWarehouse-waferWarehouse-ID") == null) {
            return new Result();
        }
        return Tools.getResult(params, waferGearWarehouseService);
    }

    @PostMapping(path = "/bindingWaferGearWarehouse")
    @Transactional
    public void bindingWaferGearWarehouse(@RequestBody Map<String, Object> params) throws Exception {


        if (params.get("taskID") == null || params.get("gearWarehouse") == null) {
            throw new Exception("未选中需要改绑的工序或库存");
        }

        Long waferId = Long.valueOf(params.get("taskID").toString());
        List<String> gearWarehouse = (List<String>) params.get("gearWarehouse");


        Optional<Operation> optional = operationService.findById(waferId);

        List<WaferGearWarehouse> waferGearWarehouses = waferGearWarehouseService.findAll(gearWarehouse);

        if (optional.isPresent() && waferGearWarehouses.size() > 0) {
            Operation operation = optional.get();
            PickingOrder pickingOrder = operation.getPickingOrder();
            String pickingOrderSliceNr = pickingOrder.getSliceNr();
            for (WaferGearWarehouse waferGearWarehouse : waferGearWarehouses) {
                if (pickingOrderSliceNr.lastIndexOf("无片") < 0 && !Objects.equals(pickingOrderSliceNr, waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse().getSliceNr())) {
                    throw new Exception("挑粒明细片号和改绑片号不一致");
                }
                if (waferGearWarehouse.getGearPickingOrders().size() > 0) {
                    boolean result = false;
                    for (GearPickingOrder gearPickingOrder : waferGearWarehouse.getGearPickingOrders()) {
                        if (pickingOrder.getID() == gearPickingOrder.getPickingOrder().getID()) {
                            result = true;
                            break;
                        }
                    }
                    if (result) {
                        continue;
                    }
                }
                GearPickingOrder gearPickingOrder = new GearPickingOrder(pickingOrder, waferGearWarehouse);
                gearPickingOrderService.save(gearPickingOrder);
            }


            if (pickingOrder.getGearPickingOrders().size() > 0) {
                WaferGearWarehouse waferGearWarehouse = pickingOrder.getGearPickingOrders().iterator().next().getWaferGearWarehouse();

                String sliceNr = waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse().getSliceNr();
                String waferNr = waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse().getWaferNr();
                String modelNr = waferGearWarehouse.getWaferModelWarehouse().getModelNr();
                String circuitNr = waferGearWarehouse.getWaferModelWarehouse().getCircuitNr();

                pickingOrder.setSliceNr(sliceNr);
                pickingOrder.setWaferNr(waferNr);
                pickingOrder.setCircuitNr(circuitNr);
                pickingOrder.setModelNr(modelNr);

                for (Operation operation1 : pickingOrder.getOperation()) {
                    operation1.setSliceNr(sliceNr);
                    operation1.setWaferNr(waferNr);
                    operation1.setCircuitNr(circuitNr);
                    operation1.setModelNr(modelNr);
                }
            }
        }
    }
}
