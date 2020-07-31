package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.ScheduleScribingItem;
import com.example.apsdemo.dao.businessObject.ScheduleTask;
import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.domain.BindSecondOrderParams;
import com.example.apsdemo.domain.EditBrief;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.domain.ScribingItemRequest;
import com.example.apsdemo.schedule.ScheduleTaskLine;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.Tools;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


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
    public Result getScribingItem() {
        return Tools.getResult(new HashMap<>(), testScribingCenterService);
    }

    @SneakyThrows
    @RequestMapping("/create")
    @Transactional
    public void createItem(@RequestBody ScribingItemRequest request) {
        if (request.getStocks().size() <= 0) {
            return;
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
        }
        scheduleMethod.updateScheduleLineDate(equipment.get());
        scheduleTaskLineService.save(line);
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

}
