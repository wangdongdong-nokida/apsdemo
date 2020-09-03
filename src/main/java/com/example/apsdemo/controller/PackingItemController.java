package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.GearPackingOrder;
import com.example.apsdemo.dao.businessObject.Operation;
import com.example.apsdemo.dao.businessObject.PackingOrder;
import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(path = "/packingItem")
public class PackingItemController {

    @Autowired
    WaferModelWarehouseService waferModelWarehouseService;

    @Autowired
    WaferWarehouseService waferWarehouseService;

    @Autowired
    PackingOrderService packingOrderService;

    @Autowired
    GearPackingOrderService gearPackingOrderService;

    @Autowired
    SalesOrderService salesOrderService;

    @Autowired
    OccupyService occupyService;

    @Autowired
    WorkStepService workStepService;

    @Autowired
    WorkFLowService workFLowService;

    @Autowired
    OperationService operationService;


    @RequestMapping(path = "/createPackingOrder")
    @Transactional
    public void createPackingOrder(@RequestBody Map<String, List<String>> params) {
        List<String> modelNrs = params.get("modelIds");
        if (modelNrs != null && modelNrs.size() > 0) {
            List<WaferModelWarehouse> modelWarehouses = waferModelWarehouseService.findAll(modelNrs);
            for (WaferModelWarehouse model : modelWarehouses) {
                PackingOrder order = new PackingOrder(false);
                order.setModelNr(model.getModelNr());
                order.setCircuitNr(model.getCircuitNr());
                WaferWarehouse waferWarehouse = model.getWaferWarehouse();
                if (waferWarehouse != null) {
                    order.setWaferNr(waferWarehouse.getWaferNr());
                    order.setSliceState(waferWarehouse.getStatus());
                    order.setSliceNr(waferWarehouse.getSliceNr());
                }
                StringBuffer salesOrders = new StringBuffer();
                StringBuffer salesOrdersQuantity = new StringBuffer();
                for (WaferGearWarehouse waferGearWarehouse : model.getWaferGearWarehouses()) {
                    GearPackingOrder gearPackingOrder = new GearPackingOrder(order, waferGearWarehouse);
                    for (Occupy occupy : waferGearWarehouse.getOccupies()) {
                        if (occupy.getSalesOrder() != null && occupy.getSalesOrder().getDdh() != null) {
                            salesOrders.append(occupy.getSalesOrder().getDdh() + ";");
                            salesOrdersQuantity.append(occupy.getSalesOrder().getDdsl() + ";");
                        }
                    }
                    gearPackingOrderService.save(gearPackingOrder);
                }
                order.setSalesOrderQuantities(salesOrdersQuantity.toString());
                order.setBindSalesOrder(salesOrders.toString());
            }
        }
    }

    @RequestMapping(path = "/createSalesPackingOrder")
    @Transactional
    public void createSalesPackingOrder(@RequestBody Map<String, List<String>> params) {
        List<String> salesOrderIds = params.get("salesOrderIds");
        if (salesOrderIds != null && salesOrderIds.size() > 0) {
            List<SalesOrder> salesOrders = salesOrderService.findAll(salesOrderIds);
            for (SalesOrder salesOrder : salesOrders) {
                PackingOrder order = new PackingOrder(true);
                Set<Occupy> occupies = salesOrder.getOccupies();
                order.setWaferNr(salesOrder.getBh());
                order.setModelNr(salesOrder.getXh());
                order.setBindSalesOrder(salesOrder.getDdh());
                order.setSalesOrderQuantities(salesOrder.getDdsl() + "");
                for (Occupy occupy : occupies) {
                    if (occupy.getWaferGearWarehouse() != null && "芯片".equals(occupy.getWaferGearWarehouse().getWLXT())) {
                        GearPackingOrder gearPackingOrder = new GearPackingOrder(order, occupy.getWaferGearWarehouse());
                        gearPackingOrderService.save(gearPackingOrder);
                    }
                }
            }
        }
    }


    @RequestMapping(path = "/createEmptyPackingOrder")
    @Transactional
    public void createEmptyPackingOrder(@RequestBody Map<String, String> params) throws Exception {

        String emptyCount = params.get("emptyCount");
        String salesOrderId = params.get("salesOrder");
        String brief = params.get("brief");

        int count = 0;
        try {
            count = Integer.parseInt(emptyCount);
        } catch (Exception e) {
            throw new Exception("格式错误!!!");
        }
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            if (salesOrderId != null && !"".equals(salesOrderId)) {
                Optional<SalesOrder> orderOptional = salesOrderService.findById(salesOrderId);
                if (orderOptional.isPresent()) {
                    SalesOrder salesOrder = orderOptional.get();
                    PackingOrder order = new PackingOrder(true);
                    order.setWaferNr(salesOrder.getBh());
                    order.setModelNr(salesOrder.getXh());
                    order.setSliceNr("无片" + random.nextLong());
                    order.setBindSalesOrder(salesOrder.getDdh());
                    order.setBrief(brief);
                    packingOrderService.save(order);
                }
            }
        }
    }

    @RequestMapping(path = "/getPackingOrders")
    public Result getPackingOrdersByWarehouse(@RequestBody Map map) {
        if (map.get("params") == null || (((Map) map.get("params")).get("waferGearWarehouse-waferModelWarehouse-waferWarehouse-ID") == null)) {
            return new Result();
        }
        return Tools.getResult(map, gearPackingOrderService);
    }

    @RequestMapping(path = "/getPackingOrdersBySales")
    public Result getPackingOrdersBySales(@RequestBody Map map) {
        if (map.get("params") == null || (((Map) map.get("params")).get("<>bindSalesOrder") == null)) {
            return new Result();
        }
        return Tools.getResult(map, packingOrderService);
    }

    @RequestMapping(path = "/getPackingOrdersAll")
    public Result getPackingOrders(@RequestBody Map map) {
//        if (map.get("params") == null || (((Map) map.get("params")).get("<>bindSalesOrder") == null)) {
//            return new Result();
//        }
        return Tools.getResult(map, packingOrderService);
    }

    @RequestMapping(path = "/findOccupyBySalesOrder")
    public Result getOccupyBySalesOrder(@RequestBody Map map) {
        if (map.get("params") == null || (((Map) map.get("params")).get("salesOrder-ID") == null)) {
            return new Result();
        }
        Result result = Tools.getResult(map, occupyService);
        List<Occupy> occupies = result.getData();
        List<WaferGearWarehouse> waferGearWarehouses = new LinkedList<>();
        for (Occupy occupy : occupies) {
            if (occupy != null) {
                WaferGearWarehouse waferGearWarehouse = occupy.getWaferGearWarehouse();
                if (waferGearWarehouse != null) {
                    StringBuffer salesOrders = new StringBuffer();
                    waferGearWarehouses.add(waferGearWarehouse);
                    for (Occupy inside : waferGearWarehouse.getOccupies()) {
                        SalesOrder salesOrder = inside.getSalesOrder();
                        if (salesOrder != null && salesOrder.getDdh() != null) {
                            salesOrders.append(salesOrder.getDdh()).append(";");
                        }
                    }
                    waferGearWarehouse.setBindSalesOrder(salesOrders.toString());
                }
            }
        }
        result.setData(waferGearWarehouses);
        return result;
    }

    @RequestMapping(path = "/deleteGearPackingOrders")
    @Transactional
    public void deleteGearPackingOrders(@RequestBody Map<String, List<Long>> ids) {

        List inside = ids.get("ids");
        if (inside != null && inside.size() > 0) {
            List<GearPackingOrder> gearPackingOrders = gearPackingOrderService.findAll(inside);
            if (gearPackingOrders.size() > 0) {
                gearPackingOrderService.deleteAll(gearPackingOrders);
            }
        }
    }


    @RequestMapping(path = "/deletePackingOrders")
    @Transactional
    public void deletePackingOrders(@RequestBody Map<String, List<Long>> ids) {
        List inside = ids.get("ids");
        if (inside != null && inside.size() > 0) {
            List<PackingOrder> packingOrders = packingOrderService.findAll(inside);
            if (packingOrders.size() > 0) {
                packingOrderService.deleteAll(packingOrders);
            }
        }
    }

    @RequestMapping(path = "/createOperationItem")
    @Transactional
    public void createOperationItem(@RequestBody Map<String, List<String>> params) {
        List<String> packingOrder = params.get("packingOrder");
        List<String> workFlow = params.get("workFlow");
        List<String> workFlowStep = params.get("workFlowStep");
        if (packingOrder == null || workFlow == null || workFlowStep == null || workFlow.size() == 0 || workFlowStep.size() == 0 || packingOrder.size() == 0) {
            return;
        }

        Optional<PackingOrder> packingOrderOptional = packingOrderService.findById(Long.valueOf(packingOrder.get(0)));
        Optional<WorkFlow> workFlowOptional = workFLowService.findById(workFlow.get(0));
        if (packingOrderOptional.isPresent() && workFlowOptional.isPresent()) {
            for (String step : workFlowStep) {
                Optional<WorkStep> workStepOptional = workStepService.findById(step);
                if (workStepOptional.isPresent() && workStepOptional.get().getWorkStepName() != null && workFlowOptional.get().getWorkFlowName() != null) {
                    Operation operation = new Operation(packingOrderOptional.get());
                    operation.setWorkStepName(workStepOptional.get().getWorkStepName().getStepName());
                    operation.setWorkFlowName(workFlowOptional.get().getWorkFlowName().getWorkFlowName());
                    operationService.save(operation);
                }
            }
        }
    }


    @RequestMapping(path = "/getOperation")
    public Result getOperation(@RequestBody Map<String, Object> params) {
        if(params.get("params")==null||((Map)params.get("params")).get("packingOrder-ID")==null){
            return new Result();
        }
        return Tools.getResult(params, operationService);

    }


}
