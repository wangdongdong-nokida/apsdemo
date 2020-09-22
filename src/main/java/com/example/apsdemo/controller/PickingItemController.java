package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.*;
import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.domain.CreateOperationParams;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.Tools;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.*;

@RestController
@RequestMapping(path = "/pickingItem")
public class PickingItemController {

    @Autowired
    WaferModelWarehouseService waferModelWarehouseService;

    @Autowired
    WaferWarehouseService waferWarehouseService;

    @Autowired
    PickingOrderService pickingOrderService;

    @Autowired
    GearPickingOrderService gearPickingOrderService;

    @Autowired
    SalesOrderService salesOrderService;

    @Autowired
    OccupyService occupyService;

    @Autowired
    WorkStepService WorkStepService;

    @Autowired
    WorkFLowService workFLowService;

    @Autowired
    OperationService operationService;

    @Autowired
    ScheduleTaskService scheduleTaskService;

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    ScheduleMethod scheduleMethod;

    @Autowired
    TestItemController testItemController;

    @Autowired
    ContainerService containerService;

    @Autowired
    LGyWlztService lGyWlztService;

    @RequestMapping(path = "/createPickingOrder")
    @Transactional
    public void createPickingOrder(@RequestBody Map<String, List<String>> params) {
        List<String> modelNrs = params.get("modelIds");
        if (modelNrs != null && modelNrs.size() > 0) {
            List<WaferModelWarehouse> modelWarehouses = waferModelWarehouseService.findAll(modelNrs);
            for (WaferModelWarehouse model : modelWarehouses) {
                PickingOrder order = new PickingOrder(false);
                order.setModelNr(model.getModelNr());
                order.setCircuitNr(model.getCircuitNr());
                WaferWarehouse waferWarehouse = model.getWaferWarehouse();
                if (waferWarehouse != null) {
                    order.setWaferNr(waferWarehouse.getWaferNr());
                    order.setSliceState(waferWarehouse.getStatus());
                    order.setSliceNr(waferWarehouse.getSliceNr());
                }
                StringBuilder salesOrders = new StringBuilder();
                StringBuilder salesOrdersQuantity = new StringBuilder();
                for (WaferGearWarehouse waferGearWarehouse : model.getWaferGearWarehouses()) {
                    GearPickingOrder gearPickingOrder = new GearPickingOrder(order, waferGearWarehouse);
                    for (Occupy occupy : waferGearWarehouse.getOccupies()) {
                        if (occupy.getSalesOrder() != null && occupy.getSalesOrder().getDdh() != null) {
                            salesOrders.append(occupy.getSalesOrder().getDdh()).append(";");
                            salesOrdersQuantity.append(occupy.getSalesOrder().getDdsl()).append(";");
                        }
                    }
                    gearPickingOrderService.save(gearPickingOrder);
                }
                order.setSalesOrderQuantities(salesOrdersQuantity.toString());
                order.setBindSalesOrder(salesOrders.toString());
            }
        }
    }

    @RequestMapping(path = "/createSalesPickingOrder")
    @Transactional
    public void createSalesPickingOrder(@RequestBody Map<String, List<String>> params) {
        List<String> salesOrderIds = params.get("salesOrderIds");
        if (salesOrderIds != null && salesOrderIds.size() > 0) {
            List<SalesOrder> salesOrders = salesOrderService.findAll(salesOrderIds);
            for (SalesOrder salesOrder : salesOrders) {
                PickingOrder order = new PickingOrder(true);
                Set<Occupy> occupies = salesOrder.getOccupies();
                order.setWaferNr(salesOrder.getBh());
                order.setModelNr(salesOrder.getXh());
                order.setBindSalesOrder(salesOrder.getDdh());
                order.setSalesOrderQuantities(salesOrder.getDdsl() + "");
                for (Occupy occupy : occupies) {
                    if (occupy.getWaferGearWarehouse() != null && "芯片".equals(occupy.getWaferGearWarehouse().getWLXT())) {
                        GearPickingOrder gearPickingOrder = new GearPickingOrder(order, occupy.getWaferGearWarehouse());
                        gearPickingOrderService.save(gearPickingOrder);
                        if (order.getWaferNr() == null || order.getModelNr() == null) {
                            if (occupy.getWaferGearWarehouse().getWaferModelWarehouse() != null && occupy.getWaferGearWarehouse().getWaferModelWarehouse().getWaferWarehouse() != null) {
                                order.setWaferNr(occupy.getWaferGearWarehouse().getWaferModelWarehouse().getWaferWarehouse().getWaferNr());
                                order.setModelNr(occupy.getWaferGearWarehouse().getWaferModelWarehouse().getModelNr());
                                order.setSliceState(occupy.getWaferGearWarehouse().getWLZT());
                            }
                        }
                    }
                }
            }
        }
    }


    @RequestMapping(path = "/createEmptyPickingOrder")
    @Transactional
    public void createEmptyPickingOrder(@RequestBody Map<String, String> params) throws Exception {

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
                    PickingOrder order = new PickingOrder(true);
                    order.setWaferNr(salesOrder.getBh());
                    order.setModelNr(salesOrder.getXh());
                    order.setSliceNr("无片" + random.nextLong());
                    order.setBindSalesOrder(salesOrder.getDdh());
                    order.setBrief(brief);
                    pickingOrderService.save(order);
                }
            }
        }
    }

    @RequestMapping(path = "/getPickingOrders")
    public Result getPickingOrdersByWarehouse(@RequestBody Map map) {
        if (map.get("params") == null || (((Map) map.get("params")).get("waferGearWarehouse-waferModelWarehouse-waferWarehouse-ID") == null)) {
            return new Result();
        }
        return Tools.getResult(map, gearPickingOrderService);
    }

    @RequestMapping(path = "/getPickingOrdersBySales")
    public Result getPickingOrdersBySales(@RequestBody Map map) {
        if (map.get("params") == null || (((Map) map.get("params")).get("<>bindSalesOrder") == null)) {
            return new Result();
        }
        return Tools.getResult(map, pickingOrderService);
    }

    @RequestMapping(path = "/getPickingOrdersAll")
    public Result getPickingOrders(@RequestBody Map map) {
//        if (map.get("params") == null || (((Map) map.get("params")).get("<>bindSalesOrder") == null)) {
//            return new Result();
//        }
        return Tools.getResult(map, pickingOrderService);
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


    @RequestMapping(path = "/getSalesOrderByOccupy")
    public Result getSalesOrderByOccupy(@RequestBody Map map) {
        if (map.get("params") == null) {
            return new Result();
        }
        Specification specification = Tools.getSpecificationByParams((Map<String, Object>) map.get("params"));
        List<Occupy> occupies = occupyService.findAll(specification);
        Map<String, SalesOrder> salesOrders = new TreeMap<>();
        for (Occupy occupy : occupies) {
            if (occupy.getSalesOrder() != null) {
                salesOrders.put(occupy.getSalesOrder().getID(), occupy.getSalesOrder());
            }
        }
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int current = Integer.parseInt(map.get("current").toString());
        int numberStart = current > 0 ? (current - 1) * pageSize : 0;
        int numberEnd = current > 0 ? current * pageSize : 0;
        List<SalesOrder> salesOrderList = new LinkedList<>(salesOrders.values());
        List salesOrdersResult;
        if(salesOrderList.size()>=numberEnd){
            salesOrdersResult= salesOrderList.subList(numberStart, numberEnd);
        }else {
            salesOrdersResult= salesOrderList.subList(numberStart,salesOrderList.size());
        }

        return new Result(salesOrdersResult, salesOrderList.size(), true, pageSize, current);
    }

    @RequestMapping(path = "/deleteGearPickingOrders")
    @Transactional
    public void deleteGearPickingOrders(@RequestBody Map<String, List<Long>> ids) {

        List inside = ids.get("ids");
        if (inside != null && inside.size() > 0) {
            List<GearPickingOrder> gearPickingOrders = gearPickingOrderService.findAll(inside);
            if (gearPickingOrders.size() > 0) {
                for (GearPickingOrder gearPickingOrder : gearPickingOrders) {
                    PickingOrder pickingOrder = gearPickingOrder.getPickingOrder();
                    cleanPickingOrderOperation(pickingOrder);
                }
                gearPickingOrderService.deleteAll(gearPickingOrders);
            }
        }
    }


    @RequestMapping(path = "/deletePickingOrders")
    @Transactional
    public void deletePickingOrders(@RequestBody Map<String, List<Long>> ids) {
        List inside = ids.get("ids");
        if (inside != null && inside.size() > 0) {
            List<PickingOrder> pickingOrders = pickingOrderService.findAll(inside);
            for (PickingOrder pickingOrder : pickingOrders) {
                cleanPickingOrderOperation(pickingOrder);
            }
            if (pickingOrders.size() > 0) {
                pickingOrderService.deleteAll(pickingOrders);
            }
        }
    }

    private void cleanPickingOrderOperation(PickingOrder pickingOrder) {
        if (pickingOrder != null) {
            Set<Operation> operations = pickingOrder.getOperation();
            List<Long> operationIds = new LinkedList<>();
            for (Operation operation : operations) {
                operationIds.add(operation.getID());
            }
            operationIds.remove(null);
            deleteOperationsMethod(operationIds);
        }
    }

    @RequestMapping(path = "/deleteOperations")
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteOperations(@RequestBody Map<String, List<Long>> ids) {
        List<Long> pickingOrderIDs = deleteOperationsMethod(ids.get("ids"));

        List<PickingOrder> pickingOrders = pickingOrderService.findAll(pickingOrderIDs);
        for (PickingOrder pickingOrder : pickingOrders) {
            if (pickingOrder.getOperation().size() == 0 && pickingOrder.getContainer() != null) {
                containerService.delete(pickingOrder.getContainer());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized List<Long> deleteOperationsMethod(List<Long> inside) {
        Set<Long> pickingOrders = new HashSet<>();
        if (inside != null && inside.size() > 0) {
            List<Operation> operations = operationService.findAll(inside);
            for (Operation operation : operations) {
                pickingOrders.add(operation.getPickingOrder().getID());
                if (operation.getScheduleTaskLine() != null) {
                    ScheduleTaskLine.ScheduleLine scheduleLine = operation.getScheduleTaskLine().getScheduleLine();
                    ScheduleTask task = scheduleLine.deleteFromLine(operation.getID());
                    if (task != null) {
                        scheduleTaskService.delete(task);
                    }
                    scheduleLine.calcScheduleLineDate(scheduleMethod.getBitSetWrapper(operation.getScheduleTaskLine().getEquipment()));
                } else {
                    scheduleTaskService.delete(operation);
                }
            }
        }
        pickingOrders.remove(null);
        return new LinkedList<>(pickingOrders);
    }

    @RequestMapping(path = "/createOperationItem")
    @Transactional
    public synchronized void createOperationItem(@RequestBody CreateOperationParams params) throws Exception {
        Set<PickingOrder> pickingOrders;
        pickingOrders = createOperationItemMethod(params);
        if (pickingOrders.size() > 0) {
            HttpController.postPickingOrderHttp(pickingOrders, "镜检");
        }
    }

    @RequestMapping(path = "/autoCreateOperationItem")
    @Transactional
    public synchronized void autoCreateOperationItem(@RequestBody CreateOperationParams params) throws Exception {
        Set<PickingOrder> pickingOrders;
        pickingOrders = createOperationItemMethod(params);
        if (pickingOrders.size() > 0) {
            HttpController.postPickingOrderHttp(pickingOrders, "镜检");
        }
    }

    @Transactional
    public Set<PickingOrder> createOperationItemMethod(CreateOperationParams params) throws Exception {
        List<Long> pickingOrder = params.getPickingOrder();
        String workFlowId = params.getWorkFlow();
        Set<PickingOrder> changePickingOrder = new HashSet<>();
        if (pickingOrder == null || pickingOrder.size() == 0) {
            return changePickingOrder;
        }

        List<PickingOrder> pickingOrders = pickingOrderService.findAll(pickingOrder);
        WorkFlow workFlow = null;
        if (workFlowId != null) {
            Optional<WorkFlow> workFlowOptional = workFLowService.findById(workFlowId);
            if (workFlowOptional.isPresent()) {
                workFlow = workFlowOptional.get();
            }
        }


        for (PickingOrder indexPickingOrder : pickingOrders) {
            if (workFlow == null) {
                boolean box = indexPickingOrder.isSalesOrder();
                String state = indexPickingOrder.getSliceState();
                Map specificationParams = new HashMap();
                specificationParams.put("lWlxt", box ? "芯片" : "圆片");
                specificationParams.put("lGyWlztname", state);
                Specification specification = Tools.getSpecificationByParams(specificationParams);
                List<LGyWlzt> lGyWlzts = lGyWlztService.findAll(specification);
                if (lGyWlzts.size() > 0) {
                    workFlow = lGyWlzts.get(0).getWorkFlow();
                }
            }
            if (workFlow != null) {
                for (Operation operation : indexPickingOrder.getOperation()) {
                    if (!Objects.equals(operation.getWorkFlowName(), workFlow.getWorkFlowName().getWorkFlowName())) {
                        throw new Exception("如需更换工艺路径请确保原工艺路径中的工序已被删除");
                    }
                }
                indexPickingOrder.setWorkFlowName(workFlow.getWorkFlowName());
                for (WorkStep step : workFlow.getWorkSteps()) {
                    WorkStepName workStepName = step.getWorkStepName();
                    if (workStepName != null && workStepName.isCreateOperation()) {
                        if (workFlow.getWorkFlowName() != null) {
                            boolean hasRepeat = false;
                            for (Operation operation : indexPickingOrder.getOperation()) {
                                if (operation.getR_workStepName() != null && Objects.equals(operation.getR_workStepName().getID(), workStepName.getID())) {
                                    hasRepeat = true;
                                    break;
                                }
                            }
                            if (!hasRepeat) {
                                Operation operation = new Operation(indexPickingOrder);
                                operation.setR_workStepName(workStepName);
                                operation.setWorkStepName(workStepName.getStepName());
                                operation.setWorkFlowName(workFlow.getWorkFlowName().getWorkFlowName());
                                operationService.save(operation);
                                changePickingOrder.add(indexPickingOrder);
                            }
                        }
                    }
                }
            }
        }

        return changePickingOrder;
    }


    @RequestMapping(path = "/getOperation")
    public Result getOperation(@RequestBody Map<String, Object> params) {
        Result result = Tools.getResult(params, operationService);

        List<Operation> operations = result.getData();

        for (Operation operation : operations) {
            Set<OperationEquipment> operationEquipments = operation.getR_workStepName().getOperationEquipments();
            for (OperationEquipment operationEquipment : operationEquipments) {
                operation.getEquipments().add(operationEquipment.getEquipment());
            }
        }
        return result;
    }

    @RequestMapping(path = "/schedulePickingItem")
    @Transactional
    public synchronized void schedulePickingItem(@RequestBody Map<String, List<Map<String, String>>> params) throws Exception {
        Set<PickingOrder> pickingOrders;
        try {
            pickingOrders = SchedulePickingItemMethod(params);
        } catch (Exception e) {
            throw new Exception("创建失败");
        }
        if (pickingOrders != null && pickingOrders.size() > 0) {
            HttpController.postPickingOrderHttp(pickingOrders, "镜检");
        }
    }

    private Set<PickingOrder> SchedulePickingItemMethod(@RequestBody Map<String, List<Map<String, String>>> params) throws Exception {
        Set<PickingOrder> packingOrders = new HashSet<>();
        if (params.get("params") == null) {
            return null;
        }
        List<Map<String, String>> operations = params.get("params");
        for (Map<String, String> map : operations) {

            if (map.get("ID") == null || map.get("equipmentSelected") == null || map.get("duration") == null) {
                continue;
            }
            Long id = Long.valueOf(map.get("ID"));
            String equipmentSelected = map.get("equipmentSelected");
            int duration = Integer.parseInt(map.get("duration"));
            int quantity = Integer.parseInt(map.get("quantity"));
            Optional<Equipment> equipmentOptional = equipmentService.findById(equipmentSelected);
            if (equipmentOptional.isPresent()) {
                Equipment equipment = equipmentOptional.get();
                Optional<Operation> operationOptional = operationService.findById(id);
                if (operationOptional.isPresent()) {
                    Operation operation = operationOptional.get();
                    operation.setDurationTime(duration);
                    operation.setEquipmentName(equipment.getName());
                    operation.setQuantity(quantity);
                    operationService.save(operation);
                    List<Long> ids = new LinkedList<>();
                    ids.add(operation.getID());
                    if (operation.getScheduleTaskLine() != null) {
                        testItemController.changeEquipment(ids, operation.getScheduleTaskLine().getEquipment(), equipment);
                    } else {
                        scheduleMethod.getScheduleTaskLine(equipment).addLast(operation);
                    }
                    packingOrders.add(operation.getPickingOrder());
                    scheduleMethod.updateScheduleLineDate(equipment);
                }
            }
        }
        return packingOrders;
    }
}
