package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.*;
import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.dao.dto.PickingItemDto;
import com.example.apsdemo.dao.dto.TestItemDto;
import com.example.apsdemo.domain.CreateOperationParams;
import com.example.apsdemo.domain.EditBrief;
import com.example.apsdemo.domain.PickingItemParams;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.ExcelUtils;
import com.example.apsdemo.utils.Tools;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
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

    @Autowired
    WaferProductService waferProductService;


    @RequestMapping(path = "/createPickingOrder")
    @Transactional
    public void createPickingOrder(@RequestBody PickingItemParams params) {
        List<String> modelIds = params.getModelIds();
        Map<String, String> modelNrs = params.getModelNrs();
        if (modelIds != null && modelIds.size() > 0) {
            List<WaferModelWarehouse> modelWarehouses = waferModelWarehouseService.findAll(modelIds);
            for (WaferModelWarehouse model : modelWarehouses) {
                PickingOrder order = new PickingOrder(false);
                order.setModelNr(modelNrs.get(model.getID()));
                order.setCircuitNr(model.getCircuitNr());

                StringBuilder stringBuilder = new StringBuilder();
                WaferWarehouse waferWarehouse = model.getWaferWarehouse();
                if (waferWarehouse != null) {
                    order.setWaferNr(waferWarehouse.getWaferNr());
                    order.setSliceNr(waferWarehouse.getSliceNr());
                }
                StringBuilder salesOrders = new StringBuilder();
                StringBuilder salesOrdersQuantity = new StringBuilder();
                int stockQuantity = 0;
                Map<String, SalesOrder> bindingSalesOrders = new HashMap();
                StringBuilder bindingContract = new StringBuilder();
                StringBuilder bindingCustomer = new StringBuilder();
                StringBuilder bindingTestTime = new StringBuilder();
                for (WaferGearWarehouse waferGearWarehouse : model.getWaferGearWarehouses()) {
                    if (waferGearWarehouse.getQuantity() != null && !"测试不合格".equals(waferGearWarehouse.getWLZT())) {
                        stockQuantity += Integer.parseInt(waferGearWarehouse.getQuantity());
                        Set<Occupy> occupies = waferGearWarehouse.getOccupies();
                        for (Occupy occupy : occupies) {
                            bindingSalesOrders.put(occupy.getSalesOrder().getID(), occupy.getSalesOrder());
                        }
                    }

                    if (waferGearWarehouse.getDWID() != null && !Objects.equals("0", waferGearWarehouse.getQuantity()) && waferGearWarehouse.getWLZT() != null && stringBuilder.lastIndexOf(waferGearWarehouse.getWLZT()) < 0) {
                        stringBuilder.append(waferGearWarehouse.getWLZT());
                    }

                    GearPickingOrder gearPickingOrder = new GearPickingOrder(order, waferGearWarehouse);
                    for (SalesOrder salesOrder : bindingSalesOrders.values()) {
                        if (salesOrder.getlDdname() != null) {
                            salesOrders.append(salesOrder.getlDdname()).append(";");
                            salesOrdersQuantity.append(salesOrder.getDgsl()).append(";");
                        }
                        if (salesOrder.getYqfhwcrq() != null) {
                            bindingTestTime.append(salesOrder.getYqfhwcrq().toString()).append(";");
                        }
                        bindingContract.append(salesOrder.getlHt().getlHtname()).append(";");
                        bindingCustomer.append(salesOrder.getlHt().getKh()).append(";");
                    }
                    gearPickingOrderService.save(gearPickingOrder);
                }
                order.setSalesOrderQuantities(salesOrdersQuantity.toString());
                order.setQuantity(String.valueOf(stockQuantity));
                order.setBindSalesOrder(salesOrders.toString());
                order.setSliceState(stringBuilder.toString());
                order.setBindContract(bindingContract.toString());
                order.setBindCustomer(bindingCustomer.toString());
                order.setSalesOrderTestDate(bindingTestTime.toString());
//                pickingOrderService.save(order);
            }
        }
    }

    @Data
    class WaferGearOccupyQuantity {
        Set<WaferGearWarehouse> waferGearWarehouses = new HashSet<>();
        int quantity = 0;
    }

    @RequestMapping(path = "/createSalesPickingOrder")
    @Transactional
    public void createSalesPickingOrder(@RequestBody Map<String, List<String>> params) {
        List<String> salesOrderIds = params.get("salesOrderIds");
        if (salesOrderIds != null && salesOrderIds.size() > 0) {
            List<SalesOrder> salesOrders = salesOrderService.findAll(salesOrderIds);
            for (SalesOrder salesOrder : salesOrders) {
                Map<String, WaferGearOccupyQuantity> waferGears = new HashMap<>();
                Set<Occupy> occupies = salesOrder.getOccupies();
                for (Occupy occupy : occupies) {
                    if (occupy != null && occupy.getWaferGearWarehouse() != null && occupy.getWaferGearWarehouse().getWaferModelWarehouse() != null) {
                        boolean created = false;
                        for (GearPickingOrder gearPickingOrder : occupy.getWaferGearWarehouse().getGearPickingOrders()) {
                            if (Objects.equals(gearPickingOrder.getPickingOrder().getBindSalesOrderID(), salesOrder.getID())) {
                                created = true;
                                break;
                            }
                        }
                        if (created) {
                            continue;
                        }
                        String sliceNr = occupy.getWaferGearWarehouse().getWaferModelWarehouse().getWaferWarehouse().getSliceNr();
                        if (sliceNr != null && !"".equals(sliceNr)) {
                            WaferGearOccupyQuantity waferGearOccupyQuantity = waferGears.computeIfAbsent(sliceNr, key -> new WaferGearOccupyQuantity());
                            waferGearOccupyQuantity.getWaferGearWarehouses().add(occupy.getWaferGearWarehouse());
                            if (occupy.getZlsl() != null && occupy.getZlsl() > 0) {
                                waferGearOccupyQuantity.setQuantity(waferGearOccupyQuantity.getQuantity() + occupy.getZlsl());
                            }
                        }
                    }
                }

                for (WaferGearOccupyQuantity waferGear : waferGears.values()) {

                    PickingOrder order = new PickingOrder(true);
                    order.setWaferNr(salesOrder.getBh());
                    order.setModelNr(salesOrder.getXh());
                    order.setBindSalesOrder(salesOrder.getlDdname());
                    order.setBindSalesOrderID(salesOrder.getID());
                    order.setSalesOrderQuantities(salesOrder.getDgsl() + "");
                    order.setQuantity(waferGear.getQuantity() + "");
                    order.setSalesOrderTestDate(salesOrder.getYqfhwcrq() != null ? salesOrder.getYqfhwcrq().toString() : null);
                    order.setBindCustomer(salesOrder.getlHt() != null ? (salesOrder.getlHt().getKh() == null ? "" : salesOrder.getlHt().getKh()) : "");
                    order.setBindContract(salesOrder.getlHt() != null ? (salesOrder.getlHt().getlHtname() == null ? "" : salesOrder.getlHt().getlHtname()) : "");
                    for (WaferGearWarehouse waferGearWarehouse : waferGear.getWaferGearWarehouses()) {
                        if (waferGearWarehouse != null && "芯片".equals(waferGearWarehouse.getWLXT())) {
                            GearPickingOrder gearPickingOrder = new GearPickingOrder(order, waferGearWarehouse);
                            gearPickingOrderService.save(gearPickingOrder);
                            if (order.getWaferNr() == null || order.getModelNr() == null) {
                                if (waferGearWarehouse.getWaferModelWarehouse() != null && waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse() != null) {
                                    order.setWaferNr(waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse().getWaferNr());
                                    order.setModelNr(waferGearWarehouse.getWaferModelWarehouse().getModelNr());
                                    order.setSliceState(waferGearWarehouse.getWLZT());
                                    order.setSliceNr(waferGearWarehouse.getWaferModelWarehouse().getWaferWarehouse().getSliceNr());
                                    order.setCircuitNr(waferGearWarehouse.getWaferModelWarehouse().getCircuitNr());
                                }
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
        String waferNr = params.get("waferNr").toUpperCase();
        String salesOrderId = params.get("salesOrder");
        String brief = params.get("brief");

//        return Tools.getResult(requestPage, service);

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
                    Map insideParams = new HashMap();
                    insideParams.put("wafer-nr", waferNr);
                    insideParams.put("product", salesOrder.getXh());
                    Specification specification = Tools.getSpecificationByParams(insideParams);
                    List<WaferProduct> waferProducts = waferProductService.findAll(specification);

                    WaferProduct waferProduct = waferProducts.size() > 0 ? waferProducts.get(0) : null;
                    int quantity = 0;
                    if (waferProduct != null) {
                        try {
                            int unitNumber = Integer.parseInt(waferProduct.getWafer().getUnitNumber());
                            quantity = waferProduct.getQuantity() * unitNumber;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    PickingOrder order = new PickingOrder(false);
                    order.setWaferNr(waferNr);
                    order.setModelNr(salesOrder.getXh());
                    order.setSliceNr("无片" + random.nextLong());
                    order.setBindSalesOrder(salesOrder.getlDdname());
                    order.setSalesOrderTestDate(salesOrder.getYqfhwcrq() != null ? salesOrder.getYqfhwcrq().toString() : null);
                    order.setBindCustomer(salesOrder.getlHt() != null ? (salesOrder.getlHt().getKh() == null ? "" : salesOrder.getlHt().getKh()) : "");
                    order.setBindContract(salesOrder.getlHt() != null ? (salesOrder.getlHt().getlHtname() == null ? "" : salesOrder.getlHt().getlHtname()) : "");
                    order.setBrief(brief);
                    order.setSalesOrderQuantities(salesOrder.getDgsl() + "");
                    order.setQuantity(quantity + "");
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
        Map params = new HashMap();
        params.put("params", map.get("params"));
        Result pickingOrderResult = Tools.getResult(params, gearPickingOrderService);
        List<GearPickingOrder> gearPickingOrders = pickingOrderResult.getData();
        List<PickingOrder> pickingOrders = new LinkedList<>();
        for (GearPickingOrder gearPickingOrder : gearPickingOrders) {
            PickingOrder pickingOrder = gearPickingOrder.getPickingOrder();
            if (pickingOrder != null && !pickingOrders.contains(pickingOrder)) {
                pickingOrders.add(pickingOrder);
            }
        }
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int current = Integer.parseInt(map.get("current").toString());

        Result result = new Result();
        result.setTotal(pickingOrders.size());
        result.setSuccess(true);
        result.setPageSize(pageSize);
        result.setCurrent(current);
        if (current <= 0 || pageSize <= 0 || pickingOrders.size() <= 0) {
            return result;
        }
        int start = (current - 1) * pageSize;
        int end = (current) * pageSize;
        int listSize = pickingOrders.size();
        List resultData;
        if (start < listSize && listSize >= end) {
            resultData = pickingOrders.subList(start, end);
        } else if (start >= listSize) {
            return result;
        } else {
            resultData = pickingOrders.subList(start, listSize);
        }
        return new Result(resultData, resultData.size(), true, pageSize, current);
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
        if (map.get("params") != null) {
            Object createState = ((Map) map.get("params")).get("createState");
            if (createState != null && Objects.equals("created", createState)) {
                ((Map) map.get("params")).put("!*operation", "");
            } else {
                ((Map) map.get("params")).put("*operation", "");
            }
        } else {
            ((Map) map.computeIfAbsent("params", key -> new HashMap<>())).put("*operation", "");
        }
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
                        if (salesOrder != null && salesOrder.getlDdname() != null) {
                            salesOrders.append(salesOrder.getlDdname()).append(";");
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
        if (map.get("params") == null && map.get("searchInfo") == null) {
            return new Result();
        }

        Map<String, Object> searchInfo = (Map<String, Object>) map.computeIfAbsent("searchInfo", key -> new HashMap());
        searchInfo.putAll((Map) map.get("params"));
        Object createState = searchInfo.get("createState");
        Specification specification = Tools.getSpecificationByParams(searchInfo);
        Sort sort = Sort.by(Sort.Order.asc("salesOrder.jywcsj"), Sort.Order.asc("salesOrder.lHt.lHtname"));
        List<Occupy> occupies = occupyService.findAll(specification, sort);

        List<Occupy> occupySet = new LinkedList<>();

        Map<String, SalesOrder> salesOrders = new TreeMap<>();
        for (Occupy occupy : occupies) {
            if (occupy.getSalesOrder() != null) {
                if (createState != null && Objects.equals("created", createState)) {
                    if (occupy.getSalesOrder().getPickingOrders().size() > 0) {
                        if (salesOrders.get(occupy.getSalesOrder().getID()) == null) {
                            occupySet.add(occupy);
                        }
                        salesOrders.put(occupy.getSalesOrder().getID(), occupy.getSalesOrder());
                    }
                } else if (occupy.getSalesOrder().getPickingOrders().size() <= 0) {
                    if (salesOrders.get(occupy.getSalesOrder().getID()) == null) {
                        occupySet.add(occupy);
                    }
                    salesOrders.put(occupy.getSalesOrder().getID(), occupy.getSalesOrder());
                }
            }
        }
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int current = Integer.parseInt(map.get("current").toString());
        Result result = new Result();
        result.setTotal(occupySet.size());
        result.setSuccess(true);
        result.setPageSize(pageSize);
        result.setCurrent(current);
        if (current <= 0 || pageSize <= 0 || occupySet.size() <= 0) {
            return result;
        }
        int start = (current - 1) * pageSize;
        int end = (current) * pageSize;
        int listSize = occupySet.size();
        List salesOrdersResult;

        if (start < listSize && listSize >= end) {
            salesOrdersResult = occupySet.subList(start, end);
        } else if (start >= listSize) {
            return result;
        } else {
            salesOrdersResult = occupySet.subList(start, listSize);
        }
        return new Result(salesOrdersResult, occupySet.size(), true, pageSize, current);
    }

    @RequestMapping(path = "/deleteGearPickingOrders")
    @Transactional
    public void deleteGearPickingOrders(@RequestBody Map<String, List<Long>> ids) {

        List inside = ids.get("ids");
        if (inside != null && inside.size() > 0) {
            List<PickingOrder> pickingOrders = pickingOrderService.findAll(inside);
            if (pickingOrders.size() > 0) {
                for (PickingOrder pickingOrder : pickingOrders) {
                    cleanPickingOrderOperation(pickingOrder);
                    if (pickingOrder.getGearPickingOrders().size() > 0) {
                        gearPickingOrderService.deleteAll(pickingOrder.getGearPickingOrders());
                    }
                }
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
                    ScheduleTaskLine line=operation.getScheduleTaskLine();
                    ScheduleTaskLine.ScheduleLine scheduleLine = line.getScheduleLine();
                    ScheduleTask task = scheduleLine.deleteFromLine(operation.getID());
                    if (task != null) {
                        scheduleTaskService.delete(task);
                    }
                    scheduleLine.calcScheduleLineDate(scheduleMethod.getBitSetWrapper(line.getEquipment()));
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
                specificationParams.put("lWlzt-lWlztname", state);
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
                boolean hasCreated = false;
                for (WorkStep step : workFlow.getWorkSteps()) {
                    WorkStepName workStepName = step.getWorkStepName();
                    if (workStepName != null && workStepName.isCreateOperation()) {
                        if (workFlow.getWorkFlowName() != null) {
                            hasCreated = true;
                            boolean hasRepeat = false;
                            for (Operation operation : indexPickingOrder.getOperation()) {
                                if (operation.getR_workStepName() != null && Objects.equals(operation.getR_workStepName().getID(), workStepName.getID())) {
                                    hasRepeat = true;
                                    break;
                                }
                            }
                            if (!hasRepeat) {
                                Operation operation = new Operation(indexPickingOrder);
                                operation.setSalesOrderTestDate(indexPickingOrder.getSalesOrderTestDate());
                                operation.setQuantity(indexPickingOrder.getQuantity() == null ? "0" : indexPickingOrder.getQuantity());
                                operation.setR_workStepName(workStepName);
                                operation.setWorkStepName(workStepName.getStepName());
                                operation.setWorkFlowName(workFlow.getWorkFlowName().getWorkFlowName());
                                operation.setBindContract(indexPickingOrder.getBindContract());
                                operation.setBindCustomer(indexPickingOrder.getBindCustomer());
                                operationService.save(operation);
                                changePickingOrder.add(indexPickingOrder);
                            }
                        }
                    }
                }

                if (!hasCreated) {
                    Operation operation = new Operation(indexPickingOrder);
                    operation.setSalesOrderTestDate(indexPickingOrder.getSalesOrderTestDate());
                    operation.setQuantity(indexPickingOrder.getQuantity() == null ? "0" : indexPickingOrder.getQuantity());
                    operation.setWorkStepName("结束");
                    operation.setWorkFlowName(workFlow.getWorkFlowName().getWorkFlowName());
                    operation.setBindContract(indexPickingOrder.getBindContract());
                    operation.setBindCustomer(indexPickingOrder.getBindCustomer());
                    operationService.save(operation);
                    changePickingOrder.add(indexPickingOrder);
                }
            }
        }

        return changePickingOrder;
    }


    @RequestMapping(path = "/getOperation")
    public Result getOperation(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = (Map) params.computeIfAbsent("params", key -> new HashMap<String, Object>());
        if (map.get("showType") != null) {
            if ("uncreated".equals(map.get("showType"))) {
                map.put("^equipmentName", "");
            } else if ("created".equals(map.get("showType"))) {
                map.put("!^equipmentName", "");
            }
        } else {
            map.put("^equipmentName", "");
        }
        Result result = Tools.getResult(params, operationService);
        List<Operation> operations = result.getData();

        for (Operation operation : operations) {
            if (operation.getR_workStepName() != null) {
                Set<OperationEquipment> operationEquipments = operation.getR_workStepName().getOperationEquipments();
                for (OperationEquipment operationEquipment : operationEquipments) {
                    operation.getEquipments().add(operationEquipment.getEquipment());
                }
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
            String itemBrief = map.get("itemBrief");
//            int quantity = Integer.parseInt(map.get("quantity"));
            Optional<Equipment> equipmentOptional = equipmentService.findById(equipmentSelected);
            if (equipmentOptional.isPresent()) {
                Equipment equipment = equipmentOptional.get();
                Optional<Operation> operationOptional = operationService.findById(id);
                if (operationOptional.isPresent()) {
                    Operation operation = operationOptional.get();
                    operation.setDurationTime(duration);
                    operation.setEquipmentName(equipment.getName());
                    operation.setItemBrief(itemBrief);
//                    operation.setQuantity(quantity);
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

    @SneakyThrows
    @RequestMapping(path = "/editBrief")
    @Transactional
    public void editBrief(@RequestBody EditBrief brief) {
        if (brief.getIds() == null) {
            throw new Exception("没有选中测试明细！");
        }
        List<Operation> tasks = operationService.findAll(brief.getIds());
        for (Operation task : tasks) {
            task.setBrief(brief.getBrief());
        }
    }

    @RequestMapping(path = "/exportPackingItemData")
    public ResponseEntity<byte[]> exportPackingItemData(@RequestBody TestItemDto data) throws Exception {
        try {
            data.getTestItemParamsList().remove("current");
            data.getTestItemParamsList().remove("pageSize");
            if (data.getTestItemParamsList().get("params") == null || ((Map) data.getTestItemParamsList().get("params")).get("scheduleTaskLine-equipment-ID") == null) {
                return new ResponseEntity<byte[]>(HttpStatus.FAILED_DEPENDENCY);
            }
            Result result = Tools.getResult(data.getTestItemParamsList(), scheduleTaskService);
            List<PickingItemDto.PickingItem> testItemList = new ArrayList<>();
            for (Operation o : (List<Operation>) result.getData()) {
                testItemList.add(this.convertData(o));
            }
            List<Object> objData = new ArrayList<>(result.getData());
            SXSSFWorkbook wb = ExcelUtils.newInstance().createExcelSXSSF(data.getHeaderNameArray(), data.getHeaderKeyArray(), "挑粒排产", testItemList);
            if (wb == null)
                return null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            byte[] bytes = os.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("fileName", URLEncoder.encode("挑粒排产", "UTF-8"));
            List list = new ArrayList<>();
            list.add(URLEncoder.encode("挑粒排产"));
            headers.setAccessControlExposeHeaders(list);
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
            return response;
        } catch (Exception ex) {
            return new ResponseEntity<byte[]>(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    private PickingItemDto.PickingItem convertData(Operation o) {
        PickingItemDto.PickingItem item = new PickingItemDto.PickingItem();
        item.setDurationTime(o.getDurationTime());
        item.setDurationDelayTime(o.getDurationDelayTime());
        item.setEndDate(o.getEndDate());
        item.setStartDate(o.getStartDate());
        item.setBindContract(o.getBindContract());
        item.setBindCustomer(o.getBindCustomer());
        item.setBindSalesOrder(o.getBindSalesOrder());
        item.setCircuitNr(o.getCircuitNr());
        item.setSliceNr(o.getSliceNr());
        item.setWaferNr(o.getWaferNr());
        item.setModelNr(o.getModelNr());
        item.setSalesOrderQuantities(o.getSalesOrderQuantities());
        item.setWorkFlowName(o.getWorkFlowName());
        item.setWorkStepName(o.getWorkStepName());
        item.setItemBrief(o.getItemBrief());
        item.setEquipmentName(o.getEquipmentName());
        return item;
    }
}
