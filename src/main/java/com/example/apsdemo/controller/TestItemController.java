package com.example.apsdemo.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.apsdemo.admin.authority.security.pojo.SysUser;
import com.example.apsdemo.admin.authority.security.service.SysUserService;
import com.example.apsdemo.admin.common.api.CommonResult;
import com.example.apsdemo.dao.businessObject.*;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.domain.*;
import com.example.apsdemo.logicSchedule.EquipmentCalendarBitSet;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.Tools;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.*;


@RestController
@RequestMapping(path = "/testItem")
public class TestItemController {

    public static final String TestType = "预测";
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
    @Autowired
    SecondOrderService secondOrderService;
    @Autowired
    ScheduleMethod scheduleMethod;

    @Autowired
    private SysUserService userService;

    @SneakyThrows
    @RequestMapping(path = "/create")
    public synchronized void createTestItem(@RequestBody TestItemCreateParams requestPage) {

        Set<Long> ids;
        try {
            ids = createItem(requestPage);
        } catch (Exception e) {
            throw new Exception("创建失败");
        }
        HttpController.postHttp(ids, "测试");
    }

    @Transactional
    public synchronized Set<Long> createItem(TestItemCreateParams requestPage) throws Exception {

        Optional<Equipment> equipmentOptional = equipmentService.findById(requestPage.getEquipmentId());
        if (!equipmentOptional.isPresent()) {
            throw new Exception("没有找到设备ID：" + requestPage.getEquipmentId());
        }
        Set<Long> ids = new HashSet<>();
        Optional<SecondOrder> secondOrders = secondOrderService.findById(requestPage.getSecondOrder());
        SecondOrder order = secondOrders.orElse(null);
        Equipment equipment = equipmentOptional.get();
        ScheduleTaskLine line = scheduleMethod.getScheduleTaskLine(equipment);
        List<WaferWarehouse> waferWarehouseList = waferWarehouseService.findAll(requestPage.getStock());
        int forecastSize = requestPage.getForecast().length;
        int screenSize = requestPage.getScreen().length;
        int assessmentSize = requestPage.getAssessment().length;
        if (!requestPage.isTestContainer()) {
            if (requestPage.getSliceNum() > 0) {
                int productWaferSize = requestPage.getProduct().size();
                Random random = new Random();
                for (int i = 0; i < requestPage.getSliceNum(); i++) {
                    String sliceNr = "无片_" + random.nextInt(1000000000) + "";
                    TestScribingCenter center = new TestScribingCenter(sliceNr, requestPage.getWaferNr());
                    testScribingCenterService.save(center);
                    for (TestItemCreateParams.Product product : requestPage.getProduct()) {
                        ids.addAll(createItemByParams(order, requestPage, line, productWaferSize, forecastSize, screenSize, assessmentSize, product.getModelNr(), sliceNr, center, product));
                    }
                }
            } else {
                for (WaferWarehouse waferWarehouse : waferWarehouseList) {
                    TestScribingCenter center = waferWarehouse.getTestScribingCenter();
                    if (center == null) {
                        center = new TestScribingCenter(waferWarehouse.getSliceNr(), waferWarehouse.getWaferNr());
                        center.setWaferWarehouse(waferWarehouse);
                        testScribingCenterService.save(center);
                    }
                    int productWaferSize = requestPage.getProduct().size();
                    for (TestItemCreateParams.Product product : requestPage.getProduct()) {
                        ids.addAll(createItemByParams(order, requestPage, line, productWaferSize, forecastSize, screenSize, assessmentSize, product.getModelNr(), waferWarehouse.getSliceNr(), center, product));
                    }
                }
            }
        } else {
            String[] testSymbol = requestPage.getTestSymbol().split(";");
            for (String symbol : testSymbol) {
                TestScribingCenter center = new TestScribingCenter(symbol, requestPage.getWaferNr());
                testScribingCenterService.save(center);
                ids.addAll(createItemByParams(order, requestPage, line, testSymbol.length, forecastSize, screenSize, assessmentSize, requestPage.getModelNr(), symbol, center, new TestItemCreateParams.Product()));
            }
        }
        scheduleMethod.updateScheduleLineDate(equipment);
        scheduleTaskLineService.save(line);
        return ids;
    }


    public List<Long> createItemByParams(SecondOrder secondOrder, TestItemCreateParams requestPage, ScheduleTaskLine line, int productWaferSize, int forecastSize, int screenSize, int assessmentSize, String modelNr, String sliceNr, TestScribingCenter center, TestItemCreateParams.Product product) {

        List<Long> ids = new LinkedList<>();
        for (String forecast : requestPage.getForecast()) {
            ScheduleTestItem item = new ScheduleTestItem(requestPage.getTestBrief(),secondOrder, line, center, modelNr, requestPage.getWaferNr(), sliceNr, forecast, TestType, (int) ((requestPage.getForecastHours() * 60) / (forecastSize * productWaferSize)), product.getForecast(), product.getCircuitNr());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
            ids.add(task.getID());
        }
        for (String screen : requestPage.getScreen()) {
            ScheduleTestItem item = new ScheduleTestItem(requestPage.getTestBrief(),secondOrder, line, center, modelNr, requestPage.getWaferNr(), sliceNr, screen, ScreenType, (int) ((requestPage.getScreenHours() * 60) / (screenSize * productWaferSize)), product.getScreen(), product.getCircuitNr());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
            ids.add(task.getID());
        }
        for (String screen : requestPage.getAssessment()) {
            ScheduleTestItem item = new ScheduleTestItem(requestPage.getTestBrief(),secondOrder, line, center, modelNr, requestPage.getWaferNr(), sliceNr, screen, AssessmentType, (int) ((requestPage.getAssessmentHours() * 60) / (assessmentSize * productWaferSize)), product.getAssessment(), product.getCircuitNr());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
            ids.add(task.getID());
        }
        return ids;
    }

    @SneakyThrows
    @RequestMapping("/updateCalendar")
    public void updateCalendar() {
        List<Equipment> equipments = equipmentService.findAll();
        for (Equipment equipment : equipments) {
            scheduleMethod.getBitSetWrapper(equipment);
        }
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
    public void editBrief(@RequestBody EditBrief brief) {
//        SysUser sysUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (brief.getIds() == null) {
            throw new Exception("没有选中测试明细！");
        }
        List<ScheduleTask> tasks = scheduleTaskService.findAll(brief.getIds());
        for (ScheduleTask task : tasks) {
            task.getScheduleTestItem().setItemBrief(brief.getBrief());
        }
    }

    @SneakyThrows
    @RequestMapping(path = "/editDurationTime")
    @Transactional
    public void editDurationTime(@RequestBody EditDurationTime durationTime) {
        if (durationTime.getIds() == null) {
            throw new Exception("没有选中测试明细！");
        }
        List<ScheduleTask> tasks = scheduleTaskService.findAll(durationTime.getIds());
        for (ScheduleTask task : tasks) {
            task.setDurationTime(durationTime.getDurationTime());
        }
        if (tasks.size() > 0) {
            ScheduleTask task = tasks.get(0);
            if (task.getScheduleTaskLine() != null) {
                ScheduleTaskLine line = task.getScheduleTaskLine();
                line.getScheduleLine().calcScheduleLineDate(scheduleMethod.getBitSetWrapper(line.getEquipment()));
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
        changeEquipment(params.getIds(), from.get(), to.get());
    }

    public synchronized void changeEquipment(List<Long> ids, Equipment from, Equipment to) throws Exception {

        if (to == null) {
            throw new Exception("没有找到放置的设备");
        }
        if (from == null) {
            throw new Exception("没有找到任务的设备");
        }
        ScheduleTaskLine.ScheduleLine fromScheduleLine = scheduleMethod.getScheduleTaskLine(from).getScheduleLine();
        ScheduleTaskLine.ScheduleLine toScheduleLine = scheduleMethod.getScheduleTaskLine(to).getScheduleLine();
        for (Long id : ids) {
            ScheduleTask task = fromScheduleLine.deleteFromLine(id);
            if (task != null) {
                toScheduleLine.addLastAndQueen(task);
            }
        }
        fromScheduleLine.calcScheduleLineDate(scheduleMethod.getBitSetWrapper(from));
        toScheduleLine.calcScheduleLineDate(scheduleMethod.getBitSetWrapper(to));
    }

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
            if (task != null) {
                scheduleTaskService.delete(task);
            }
        }
        scheduleLine.calcScheduleLineDate(scheduleMethod.getBitSetWrapper(equipmentOptional.get()));
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
        scheduleLine.calcScheduleLineDate(scheduleMethod.getBitSetWrapper(equipmentOptional.get()));
    }

    @RequestMapping(path = "/changeTestStock")
    @Transactional
    public void changeTestStock(@RequestBody ChangeStock changeStock) throws Exception {
        if (Tools.checkIsEmpty(changeStock.getWaferWarehouseID()) || changeStock.getTaskIDs() == null) {
            return;
        }
        Optional<ScheduleTask> tasks = scheduleTaskService.findById(changeStock.getTaskIDs());
        Optional<WaferWarehouse> waferWarehouse = waferWarehouseService.findById(changeStock.getWaferWarehouseID());
        if (waferWarehouse.isPresent() && tasks.isPresent()) {
            ScheduleTask task = tasks.get();
            ScheduleTestItem item = task.getScheduleTestItem();
            TestScribingCenter center = item.getTestScribingCenter();
            TestScribingCenter to = waferWarehouse.get().getTestScribingCenter();
            if (to == null) {
                center.setWaferWarehouse(waferWarehouse.get());
            } else {
                boolean testZero = to.getScheduleTestItem().size() == 0;
                boolean scribingZero = to.getScheduleScribingItems().size() == 0;
                boolean secondOrderZero = to.getSecondOrder() == null;
                if (testZero && scribingZero && secondOrderZero) {
                    to.setSecondOrder(center.getSecondOrder());
                    for (ScheduleTestItem testItem : center.getScheduleTestItem()) {
                        testItem.setTestScribingCenter(to);
                    }
                    for (ScheduleScribingItem scribingItem : center.getScheduleScribingItems()) {
                        scribingItem.setTestScribingCenter(center);
                    }
                } else {
                    throw new Exception("更换的圆片已经创建测试明细或则划片明细，请重新选择！");
                }
            }
        }

    }


}
