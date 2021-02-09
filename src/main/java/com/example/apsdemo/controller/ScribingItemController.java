package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.*;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.dao.dto.ScribingItemDto;
import com.example.apsdemo.dao.dto.TestItemDto;
import com.example.apsdemo.domain.*;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.ExcelUtils;
import com.example.apsdemo.utils.Tools;
import lombok.SneakyThrows;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.*;


@RestController
@RequestMapping("/scribingItem")
public class ScribingItemController {

    @Autowired
    TestScribingCenterService testScribingCenterService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    ScheduleMethod scheduleMethod;
    @Autowired
    ScheduleScribingItemService scheduleScribingItemService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    ScheduleTaskLineService scheduleTaskLineService;
    @Autowired
    SecondOrderService secondOrderService;
    @Autowired
    WaferWarehouseService waferWarehouseService;

    @RequestMapping("/getScribingItem")
    public Result getScribingItem(@RequestBody Map<String, Object> map) {
        Object params = map.get("params");
        HashMap paramsMap = new HashMap();
        if (params != null) {
            Object showType = ((Map) params).get("showType");
            paramsMap.putAll((Map) params);
            if (showType == null || showType.equals("uncreated")) {
                paramsMap.put("*scheduleScribingItems", "uncreated");
                map.put("params", paramsMap);
            } else if (showType.equals("created")) {
                paramsMap.put("!*scheduleScribingItems", "created");
                map.put("params", paramsMap);
            }
        } else {
            paramsMap.put("*scheduleScribingItems", "uncreated");
            map.put("params", paramsMap);
        }
        Result result = Tools.getResult(map, testScribingCenterService);

        List<TestScribingCenter> centers = result.getData();

        List<TestScribingCenterResult> results = new LinkedList<>();

        StringBuffer productType = new StringBuffer();
        StringBuffer orderType = new StringBuffer();
        for (TestScribingCenter center : centers) {
            Map<String, SecondOrder> secondOrders = new HashMap<>();
            if (center.getSecondOrder() != null) {
                addType(productType, orderType, secondOrders, center.getSecondOrder(), center);
            } else {
                for (ScheduleTestItem item : center.getScheduleTestItem()) {
                    if (item.getSecondOrder() != null) {
                        addType(productType, orderType, secondOrders, item.getSecondOrder(), center);
                    }
                }
            }
            TestScribingCenterResult testScribingCenterResult = new TestScribingCenterResult(center, secondOrders.values(), productType.toString(), orderType.toString());
            results.add(testScribingCenterResult);
        }
        result.setData(results);
        return result;
    }

    private void addType(StringBuffer productType, StringBuffer orderType, Map<String, SecondOrder> secondOrders, SecondOrder secondOrder, TestScribingCenter center) {
        if (secondOrder == null) {
            return;
        }
        secondOrders.put(secondOrder.getID(), secondOrder);
        if (secondOrder.getProductType() != null && productType.lastIndexOf(secondOrder.getProductType().getName()) < 0) {
            productType.append(secondOrder.getProductType().getName()).append(";");
        }
        if (secondOrder.getType() != null && orderType.lastIndexOf(secondOrder.getType()) < 0) {
            orderType.append(secondOrder.getType()).append(";");
        }
    }

    @SneakyThrows
    @RequestMapping("/create")
    @Transactional
    public void createItem(@RequestBody ScribingItemRequest request) {
        Set<Long> ids;
        try {
            ids = createScribingItemNormal(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (ids.size() > 0) {
            HttpController.postHttp(ids, "划片");
        }
    }

    @Transactional
    public Set<Long> createScribingItemNormal(@RequestBody ScribingItemRequest request) throws Exception {
        Set<Long> ids = new HashSet<>();
        if (request.getStocks().size() <= 0) {
            return ids;
        }
        Optional<Equipment> equipment = equipmentService.findById(request.getEquipmentId());
        if (!equipment.isPresent()) {
            throw new Exception("没有找到设备" + request.getEquipmentId());
        }
        ScheduleTaskLine line = scheduleMethod.getScheduleTaskLine(equipment.get());
        List<TestScribingCenter> centers = testScribingCenterService.findAll(request.getStocks());
        for (TestScribingCenter center : centers) {
            ScheduleScribingItem item = new ScheduleScribingItem(request.getBrief(), request.getDurationTime(), center, line);
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
            ids.add(task.getID());
        }
        scheduleMethod.updateScheduleLineDate(equipment.get());
        scheduleTaskLineService.save(line);
        return ids;
    }

    @SneakyThrows
    @RequestMapping("/createNoStock")
    @Transactional
    public void createNoStock(@RequestBody ScribingItemRequest request) {
        Set<Long> ids;
        try {
            ids = createScribingItemNoStock(request);
        } catch (Exception e) {
            throw new Exception("创建划片失败！");
        }
        if (ids.size() > 0) {
            HttpController.postHttp(ids, "划片");
        }
    }

    @Transactional
    public Set<Long> createScribingItemNoStock(@RequestBody ScribingItemRequest request) throws Exception {
        Set<Long> ids = new HashSet<>();
        Optional<Equipment> equipment = equipmentService.findById(request.getEquipmentId());
        if (!equipment.isPresent()) {
            throw new Exception("没有找到设备" + request.getEquipmentId());
        }
        ScheduleTaskLine line = scheduleMethod.getScheduleTaskLine(equipment.get());
        TestScribingCenter center = new TestScribingCenter(request.getSliceNr(), request.getWaferNr());
        testScribingCenterService.save(center);
        ScheduleScribingItem item = new ScheduleScribingItem(request.getBrief(), request.getDurationTime(), center, line, request.getResponsiblePerson(),
                request.getApplyPerson(), request.getApplyDate(), request.getOperationNr());
        ScheduleTask task = item.getScheduleTask();
        line.addLast(task);
        scheduleTaskService.save(task);
        ids.add(task.getID());
        scheduleMethod.updateScheduleLineDate(equipment.get());
        scheduleTaskLineService.save(line);
        return ids;
    }

    @SneakyThrows
    @RequestMapping(path = "/editBrief")
    @Transactional
    public void editBrief(@RequestBody EditBrief brief) {
        if (brief.getIds() == null) {
            throw new Exception("没有选中测试明细！");
        }
        List<ScheduleTask> tasks = scheduleTaskService.findAll(brief.getIds());
        for (ScheduleTask task : tasks) {
            if (task.getScheduleScribingItem() != null) {
                task.getScheduleScribingItem().setBrief(brief.getBrief());
            }
        }
    }

    @SneakyThrows
    @RequestMapping(path = "/bindSecondOrder")
    @Transactional
    public void bindSecondOrder(@RequestBody BindSecondOrderParams params) {
        if (params.getSecondOrder() == null || params.getStock().size() <= 0) {
            throw new Exception("没有选中二级任务或库存！");
        }
        Optional<SecondOrder> secondOrder = secondOrderService.findById(params.getSecondOrder());
        List<WaferWarehouse> waferWarehouses = waferWarehouseService.findAll(params.getStock());
        if (secondOrder.isPresent()) {
            for (WaferWarehouse waferWarehouse : waferWarehouses) {
                TestScribingCenter center = waferWarehouse.getTestScribingCenter();
                if (center == null) {
                    center = new TestScribingCenter(waferWarehouse.getSliceNr(), waferWarehouse.getWaferNr());
                }
                center.setWaferWarehouse(waferWarehouse);
                center.setSecondOrder(secondOrder.get());
                testScribingCenterService.save(center);
            }
        }
    }

    @RequestMapping(path = "/exportScribingItemData")
    public ResponseEntity<byte[]> exportScribingItemData(@RequestBody TestItemDto data) throws Exception {
        try {
            data.getTestItemParamsList().remove("current");
            data.getTestItemParamsList().remove("pageSize");
            if (data.getTestItemParamsList().get("params") == null || ((Map) data.getTestItemParamsList().get("params")).get("scheduleTaskLine-equipment-ID") == null) {
                return new ResponseEntity<byte[]>(HttpStatus.FAILED_DEPENDENCY);
            }
            Result result = Tools.getResult(data.getTestItemParamsList(), scheduleTaskService);
            List<ScribingItemDto.ScribingItem> testItemList = new ArrayList<>();
            for (ScheduleTask task : (List<ScheduleTask>) result.getData()) {
                testItemList.add(this.convertData(task));
            }
            SXSSFWorkbook wb = ExcelUtils.newInstance().createExcelSXSSF(data.getHeaderNameArray(), data.getHeaderKeyArray(), "测试排产", testItemList);
            if (wb == null)
                return null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            byte[] bytes = os.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("fileName", URLEncoder.encode("测试排产", "UTF-8"));
            List list = new ArrayList<>();
            list.add(URLEncoder.encode("测试排产"));
            headers.setAccessControlExposeHeaders(list);
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
            return response;
        } catch (Exception ex) {
            return new ResponseEntity<byte[]>(HttpStatus.FAILED_DEPENDENCY);
        }
    }


    private ScribingItemDto.ScribingItem convertData(ScheduleTask task) {
        ScribingItemDto.ScribingItem item = new ScribingItemDto.ScribingItem();
        item.setDurationTime(task.getDurationTime());
        item.setDurationDelayTime(task.getDurationDelayTime());
        item.setEndDate(task.getEndDate());
        item.setStartDate(task.getStartDate());
        item.setOperationStatus(task.getScheduleScribingItem().getOperationStatus());
        item.setResponsiblePerson(task.getScheduleScribingItem().getResponsiblePerson());
        item.setBrief(task.getScheduleScribingItem().getBrief());
        item.setOperationStatus(task.getScheduleScribingItem().getOperationStatus());
        item.setSliceNr(task.getScheduleScribingItem().getSliceNr());
        item.setWaferNr(task.getScheduleScribingItem().getWaferNr());
        return item;
    }

}
