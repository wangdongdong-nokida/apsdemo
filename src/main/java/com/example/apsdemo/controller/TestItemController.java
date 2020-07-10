package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.ScheduleTestItem;
import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.domain.TestItemChangeEquipment;
import com.example.apsdemo.domain.TestItemCreateParams;
import com.example.apsdemo.logicSchedule.EquipmentCalendarBitSet;
import com.example.apsdemo.schedule.ScheduleTask;
import com.example.apsdemo.schedule.ScheduleTaskLine;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.Tools;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.Transient;
import java.util.*;


@RestController
@RequestMapping(path = "/testItem")
public class TestItemController {

    public static final String TestType = "测试";
    public static final String ScreenType = "筛选";
    public static final String AssessmentType = "考核";

    @Autowired
    WaferWarehouseService waferWarehouseService;
    @Autowired
    TestScribingCenterService testScribingCenterService;
    @Autowired
    ScheduleTestItemService scheduleTestItemService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    ScheduleTaskLineService scheduleTaskLineService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    EquipmentCalendarBitSet equipmentCalendarBitSet;

    @SneakyThrows
    @RequestMapping(path = "/create")
    public void createTestItem(@RequestBody TestItemCreateParams requestPage) {
        createItem(requestPage);
    }

    @Transactional
    public synchronized void createItem(TestItemCreateParams requestPage) throws Exception {

        Optional<Equipment> equipmentOptional = equipmentService.findById(requestPage.getEquipmentId());
        if (!equipmentOptional.isPresent()) {
            throw new Exception("没有找到设备ID：" + requestPage.getEquipmentId());
        }
        Equipment equipment = equipmentOptional.get();
        ScheduleTaskLine line = getScheduleTaskLine(equipment);
        for (String product : requestPage.getProduct()) {
            List<WaferWarehouse> waferWarehouseList = waferWarehouseService.findAll(requestPage.getStock());
            for (WaferWarehouse waferWarehouse : waferWarehouseList) {
                TestScribingCenter center = waferWarehouse.getTestScribingCenter();
                if (center == null) {
                    center = new TestScribingCenter();
                    center.setWaferWarehouse(waferWarehouse);
                    testScribingCenterService.save(center);
                }
                for (String forecast : requestPage.getForecast()) {
                    ScheduleTestItem item = new ScheduleTestItem(line, center, product, waferWarehouse.getWaferNr(), waferWarehouse.getSliceNr(), forecast, TestType, requestPage.getForecastHours() * 60);
                    ScheduleTask task = item.getScheduleTask();
                    line.addLast(task);
                    scheduleTaskService.save(task);
                }
                for (String screen : requestPage.getScreen()) {
                    ScheduleTestItem item = new ScheduleTestItem(line, center, product, waferWarehouse.getWaferNr(), waferWarehouse.getSliceNr(), screen, ScreenType, requestPage.getScreenHours() * 60);
                    ScheduleTask task = item.getScheduleTask();
                    line.addLast(task);
                    scheduleTaskService.save(task);
                }
             }
        }

        updateScheduleLineDate(equipment);
        scheduleTaskLineService.save(line);
    }

    private void updateScheduleLineDate(Equipment equipment) {
        ScheduleTaskLine line=equipment.getScheduleTaskLine();
        EquipmentCalendarBitSet.BitSetWrapper wrapper = equipmentCalendarBitSet.bitSetWrapperMap.get(equipment.getID());
        if (wrapper == null) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 1);
            wrapper = equipmentCalendarBitSet.initialize(start, end, equipment);
        }
        line.updateScheduleDate(wrapper);
    }

    @NotNull
    @Transient
    private synchronized ScheduleTaskLine getScheduleTaskLine(Equipment equipment) {
        ScheduleTaskLine line = equipment.getScheduleTaskLine();
        if (line == null) {
            line = new ScheduleTaskLine();
            line.setEquipment(equipment);
            equipment.setScheduleTaskLine(line);
            scheduleTaskLineService.save(line);
        }
        return line;
    }


    @RequestMapping(path = "/findAll")
    public Result test(@RequestBody Map<String, Object> map) {
        if (map.get("params") == null || ((Map) map.get("params")).get("scheduleTaskLine-equipment-ID") == null) {
            return new Result();
        }
        return Tools.getResult(map, scheduleTaskService);
    }


    @SneakyThrows
    @RequestMapping(path = "/editBrief")
    @Transactional
    public void editBrief(@RequestBody Map<String, String> map) {
        if (map.get("ID") == null) {
            throw new Exception("没有选中测试明细！");
        }
        Optional<ScheduleTask> optional = scheduleTaskService.findById(Long.valueOf(map.get("ID")));
        if (optional.isPresent()) {
            optional.get().getScheduleTestItem().setItemBrief(map.get("brief"));
        }
    }

    @SneakyThrows
    @RequestMapping(path = "/editDurationTime")
    @Transactional
    public void editDurationTime(@RequestBody Map<String, String> map) {
        if (map.get("ID") == null) {
            throw new Exception("没有选中测试明细！");
        }
        Optional<ScheduleTask> optional = scheduleTaskService.findById(Long.valueOf(map.get("ID")));
        optional.ifPresent(scheduleTask -> scheduleTask.setDurationTime(Integer.parseInt(map.get("durationTime"))));
    }

    @SneakyThrows
    @RequestMapping(path = "/editEquipment")
    @Transactional
    public void editEquipment(@RequestBody TestItemChangeEquipment params) {
        if (params.getIds() == null) {
            throw new Exception("没有选中测试明细！");
        }
        List<ScheduleTask> tasks = scheduleTaskService.findAll(params.getIds());
        Optional<Equipment> optional = equipmentService.findById(params.getEquipmentID());

        if (optional.isPresent()) {
            Equipment equipmentInstance = optional.get();
            ScheduleTaskLine line = getScheduleTaskLine(equipmentInstance);
            removeTaskFromLine(tasks);
            for (ScheduleTask task : tasks) {
                task.setScheduleTaskLine(line);
                line.addLast(task);
            }
        }
    }


    @SneakyThrows
    @RequestMapping(path = "/testItemDelete")
    @Transactional
    public void testItemDelete(@RequestBody Map<String,ArrayList<Long>> params) {
        ArrayList<Long> arrayList=params.get("ids");
        List<ScheduleTask> tasks = scheduleTaskService.findAll(arrayList);
        if (tasks.size()>0) {
            removeTaskFromLine(tasks);
            scheduleTaskService.deleteAll(tasks);
        }
    }
    @SneakyThrows
    @RequestMapping(path = "/moveTask")
    @Transactional
    public void moveTask(@RequestBody Map<String,List<Long>> params) {
        List place=params.get("toPlace");
        List keys=params.get("moveKeys");

        if(place.size()==0||keys.size()==0){
            throw new Exception("请选中需要调整的任务或要调整到的位置。");
        }
        List<ScheduleTask> moveTasks=scheduleTaskService.findAll(keys);
        ScheduleTask movePlace=(ScheduleTask)scheduleTaskService.findAll(place).iterator().next();
        if(moveTasks.size()==0||movePlace==null){
            throw new Exception("未找到需要调整的任务或要调整到的位置。");
        }
        removeTaskFromLine(moveTasks);
        ScheduleTask son=movePlace.getSon();
        for(ScheduleTask task:moveTasks){
            ScheduleTask.setFatherSonRelation(movePlace,task);
            movePlace=task;
        }
        son.setFather(movePlace);
        updateScheduleLineDate(movePlace.getScheduleTaskLine().getEquipment());
    }

    private synchronized void removeTaskFromLine(Collection<ScheduleTask> tasks) {
        for(ScheduleTask task:tasks){
            if (task.getFather()!=null&&task.getSon()!=null){
                task.getSon().setFather(task.getFather());
            }else if(task.getFather()!=null){
                task.getScheduleTaskLine().setLast(task.getFather());
                task.getFather().setSon(null);
            }else if(task.getSon()!=null){
                task.getScheduleTaskLine().setFirst(task.getSon());
                task.getSon().setFather(null);
            }
            task.setFather(null);
            task.setSon(null);
        }
    }

}
