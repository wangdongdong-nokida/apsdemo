package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.ScheduleTestItem;
import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.domain.TestItemChangeEquipment;
import com.example.apsdemo.domain.TestItemCreateParams;
import com.example.apsdemo.domain.TestMoveTaskParams;
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
import java.text.SimpleDateFormat;
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
        List<WaferWarehouse> waferWarehouseList = waferWarehouseService.findAll(requestPage.getStock());
        int forecastSize = requestPage.getForecast().length;
        int screenSize = requestPage.getScreen().length;
        int assessmentSize = requestPage.getAssessment().length;
        if (!requestPage.isTestContainer()) {
            for (TestItemCreateParams.Product product : requestPage.getProduct()) {
                if (requestPage.getSliceNum() > 0) {
                    int productWaferSize = requestPage.getProduct().size();
                    Random random = new Random();
                    for (int i = 0; i < requestPage.getSliceNum(); i++) {
                        TestScribingCenter center = new TestScribingCenter();
                        testScribingCenterService.save(center);
                        createItemByParams(requestPage, line, productWaferSize, forecastSize, screenSize, assessmentSize, product.getModelNr(), random.nextInt(100000000) + "", center, product);
                    }
                } else {
                    for (WaferWarehouse waferWarehouse : waferWarehouseList) {
                        TestScribingCenter center = waferWarehouse.getTestScribingCenter();
                        if (center == null) {
                            center = new TestScribingCenter();
                            center.setWaferWarehouse(waferWarehouse);
                            waferWarehouse.setTestScribingCenter(center);
                            testScribingCenterService.save(center);
                        }
                        int productWaferSize = requestPage.getProduct().size() * waferWarehouseList.size();
                        createItemByParams(requestPage, line, productWaferSize, forecastSize, screenSize, assessmentSize, product.getModelNr(), waferWarehouse.getSliceNr(), center, product);
                    }
                }
            }
        } else {
            String[] testSymbol = requestPage.getTestSymbol().split(";");
            for (String symbol : testSymbol) {
                TestScribingCenter center = new TestScribingCenter();
                testScribingCenterService.save(center);
                createItemByParams(requestPage, line, testSymbol.length, forecastSize, screenSize, assessmentSize, requestPage.getModelNr(), symbol, center, new TestItemCreateParams.Product());
            }
        }
        updateScheduleLineDate(equipment);
        scheduleTaskLineService.save(line);
    }


    public void createItemByParams(TestItemCreateParams requestPage, ScheduleTaskLine line, int productWaferSize, int forecastSize, int screenSize, int assessmentSize, String modelNr, String sliceNr, TestScribingCenter center, TestItemCreateParams.Product product) {
        for (String forecast : requestPage.getForecast()) {
            ScheduleTestItem item = new ScheduleTestItem(line, center, modelNr, requestPage.getWaferNr(), sliceNr, forecast, TestType, (int) ((requestPage.getForecastHours() * 60) / (forecastSize * productWaferSize)), product.getForecast());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
        }
        for (String screen : requestPage.getScreen()) {
            ScheduleTestItem item = new ScheduleTestItem(line, center, modelNr, requestPage.getWaferNr(), sliceNr, screen, ScreenType, (int) ((requestPage.getScreenHours() * 60) / (screenSize * productWaferSize)), product.getScreen());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
        }
        for (String screen : requestPage.getAssessment()) {
            ScheduleTestItem item = new ScheduleTestItem(line, center, modelNr, requestPage.getWaferNr(), sliceNr, screen, AssessmentType, (int) ((requestPage.getScreenHours() * 60) / (assessmentSize * productWaferSize)), product.getAssessment());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
        }
    }

    private synchronized void updateScheduleLineDate(Equipment equipment) {
        ScheduleTaskLine line = equipment.getScheduleTaskLine();
        EquipmentCalendarBitSet.BitSetWrapper wrapper = getBitSetWrapper(equipment);
        ScheduleTaskLine.ScheduleLine scheduleLine = line.getScheduleLine();
        scheduleLine.calcScheduleLineDate(wrapper);
    }

    @SneakyThrows
    @RequestMapping("/updateCalendar")
    public void updateCalendar() {
        List<Equipment> equipments = equipmentService.findAll();
        for (Equipment equipment : equipments) {
            getBitSetWrapper(equipment);
        }
    }

    private synchronized EquipmentCalendarBitSet.BitSetWrapper getBitSetWrapper(Equipment equipment) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 2);
        return equipmentCalendarBitSet.initialize(start, end, equipment);
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
        if (optional.isPresent()) {
            ScheduleTask task = optional.get();
            if (task.getScheduleTaskLine() != null) {
                ScheduleTaskLine line = task.getScheduleTaskLine();
                line.getScheduleLine().calcScheduleLineDate(getBitSetWrapper(line.getEquipment()));
            }
        }
    }

    @SneakyThrows
    @RequestMapping(path = "/editEquipment")
    @Transactional
    public void editEquipment(@RequestBody TestItemChangeEquipment params) {
        if (params.getIds() == null) {
            throw new Exception("没有选中测试明细！");
        }
        Optional<Equipment> from = equipmentService.findById(params.getBelongEquipmentID());
        Optional<Equipment> to = equipmentService.findById(params.getEquipmentID());
        if (!to.isPresent()) {
            throw new Exception("没有找到放置的设备");
        }
        if (!from.isPresent()) {
            throw new Exception("没有找到任务的设备");
        }

        ScheduleTaskLine.ScheduleLine fromScheduleLine = getScheduleTaskLine(from.get()).getScheduleLine();
        ScheduleTaskLine.ScheduleLine toScheduleLine = getScheduleTaskLine(to.get()).getScheduleLine();
        for (Long id : params.getIds()) {
            ScheduleTask task = fromScheduleLine.deleteFromLine(id);
            if (task != null) {
                toScheduleLine.addLast(task);
            }
        }
        fromScheduleLine.calcScheduleLineDate(getBitSetWrapper(from.get()));
        toScheduleLine.calcScheduleLineDate(getBitSetWrapper(to.get()));
    }

    public static ScheduleTaskLine line;

    @SneakyThrows
    @RequestMapping(path = "/testItemDelete")
    @Transactional
    public synchronized void testItemDelete(@RequestBody Map<String, List<String>> params) {
        Optional<Equipment> equipmentOptional = equipmentService.findById(params.get("equipmentId").iterator().next());
        if (!equipmentOptional.isPresent()) {
            throw new Exception("没有找到设备ID：");
        }
        ScheduleTaskLine line = equipmentOptional.get().getScheduleTaskLine();
        ScheduleTaskLine.ScheduleLine scheduleLine = line.getScheduleLine();
        for (String id : params.get("ids")) {
            ScheduleTask task = scheduleLine.deleteFromLine(Long.valueOf(id));
            scheduleTaskService.delete(task);
        }
        scheduleLine.calcScheduleLineDate(getBitSetWrapper(equipmentOptional.get()));
    }

    @SneakyThrows
    @RequestMapping(path = "/moveTask")
    @Transactional
    public void moveTask(@RequestBody TestMoveTaskParams params) {
        List<Long> place = params.getToPlace();
        List<Long> keys = params.getMoveKeys();
        Optional<Equipment> equipmentOptional = equipmentService.findById(params.getEquipmentId());
        if (!equipmentOptional.isPresent()) {
            throw new Exception("没有找到设备ID：");
        }
        if (place.size() == 0 || keys.size() == 0) {
            throw new Exception("请选中需要调整的任务或要调整到的位置。");
        }

        ScheduleTaskLine line = equipmentOptional.get().getScheduleTaskLine();
        ScheduleTaskLine.ScheduleLine scheduleLine = line.getScheduleLine();
        scheduleLine.removeTo(keys, place.iterator().next(), true);
        scheduleLine.calcScheduleLineDate(getBitSetWrapper(equipmentOptional.get()));
    }
}
