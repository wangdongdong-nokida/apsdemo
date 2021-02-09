package com.example.apsdemo.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.apsdemo.admin.authority.security.pojo.SysUser;
import com.example.apsdemo.admin.authority.security.service.SysUserService;
import com.example.apsdemo.admin.common.api.CommonResult;
import com.example.apsdemo.dao.businessData.ScheduleTaskData;
import com.example.apsdemo.dao.businessObject.*;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.dao.dto.FileDto;
import com.example.apsdemo.dao.dto.TestItemDto;
import com.example.apsdemo.domain.*;
import com.example.apsdemo.logicSchedule.EquipmentCalendarBitSet;
import com.example.apsdemo.service.*;
import com.example.apsdemo.utils.ExcelUtils;
import com.example.apsdemo.utils.Tools;
import com.github.dockerjava.api.model.LogConfig;
import lombok.SneakyThrows;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.SQLException;
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
    OperationService operationService;

    private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);

    @Autowired
    private SysUserService userService;

    @SneakyThrows
    @RequestMapping(path = "/create")
    public void createTestItem(@RequestBody TestItemCreateParams requestPage) {

        Set<Long> ids;
        try {
            LOG.info("createTestItem--------start");
            ids = createItem(requestPage);
            LOG.info("createTestItem--------end");
        } catch (Exception e) {
            throw new Exception("创建失败");
        }
        LOG.info("createTestItem--------post start");
        postHttp(ids);
        LOG.info("createTestItem--------post end");
    }

    @Transactional(propagation = Propagation.NEVER)
    public void postHttp(Set<Long> ids) {
        if (ids.size() > 0) {
            HttpController.postHttp(ids, "测试");
        }
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
                TestItemCreateParams.Product product = new TestItemCreateParams.Product();
                try {
                    int quantity = order.getQuantity() != null ? Integer.parseInt(order.getQuantity()) : 0;
                    product.setForecast(quantity);
                    product.setAssessment(quantity);
                    product.setScreen(quantity);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ids.addAll(createItemByParams(order, requestPage, line, testSymbol.length, forecastSize, screenSize, assessmentSize, requestPage.getModelNr(), symbol, center, product));
            }
        }
        scheduleMethod.updateScheduleLineDate(equipment);
        scheduleTaskLineService.save(line);
        return ids;
    }


    public List<Long> createItemByParams(SecondOrder secondOrder, TestItemCreateParams requestPage, ScheduleTaskLine line, int productWaferSize, int forecastSize, int screenSize, int assessmentSize, String modelNr, String sliceNr, TestScribingCenter center, TestItemCreateParams.Product product) {

        List<Long> ids = new LinkedList<>();
        for (String forecast : requestPage.getForecast()) {
            ScheduleTestItem item = new ScheduleTestItem(requestPage.getTestBrief(), secondOrder, line, center, modelNr, requestPage.getWaferNr(), sliceNr, forecast, TestType, (int) ((requestPage.getForecastHours() * 60) / (forecastSize * productWaferSize)), product.getForecast(), product.getCircuitNr());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
            ids.add(task.getID());
        }
        for (String screen : requestPage.getScreen()) {
            ScheduleTestItem item = new ScheduleTestItem(requestPage.getTestBrief(), secondOrder, line, center, modelNr, requestPage.getWaferNr(), sliceNr, screen, ScreenType, (int) ((requestPage.getScreenHours() * 60) / (screenSize * productWaferSize)), product.getScreen(), product.getCircuitNr());
            ScheduleTask task = item.getScheduleTask();
            line.addLast(task);
            scheduleTaskService.save(task);
            ids.add(task.getID());
        }
        for (String screen : requestPage.getAssessment()) {
            ScheduleTestItem item = new ScheduleTestItem(requestPage.getTestBrief(), secondOrder, line, center, modelNr, requestPage.getWaferNr(), sliceNr, screen, AssessmentType, (int) ((requestPage.getAssessmentHours() * 60) / (assessmentSize * productWaferSize)), product.getAssessment(), product.getCircuitNr());
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


    @RequestMapping(path = "/findAllBySecondOrder")
    public Result findAllBySecondOrder(@RequestBody Map<String, Object> map) {
        if (map.get("params") == null) {
            return new Result();
        }
        return Tools.getResult(map, scheduleTaskService);
    }


    @RequestMapping(path = "/findOperationAll")
    public Result findOperationAll(@RequestBody Map<String, Object> map) {
        if (map.get("params") == null || ((Map) map.get("params")).get("scheduleTaskLine-equipment-ID") == null) {
            return new Result();
        }
        return Tools.getResult(map, operationService);
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
    @RequestMapping(path = "/editDurationDelayTime")
    @Transactional
    public void editDurationDelayTime(@RequestBody EditDurationTime durationTime) {
        if (durationTime.getIds() == null) {
            throw new Exception("没有选中明细！");
        }
        List<ScheduleTask> tasks = scheduleTaskService.findAll(durationTime.getIds());
        for (ScheduleTask task : tasks) {
            task.setDurationDelayTime(durationTime.getDurationTime());
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
    @RequestMapping(path = "/editSupplyTime")
    @Transactional
    public void editSupplyTime(@RequestBody SupplyTime durationTime) {
        if (durationTime.getIds() == null) {
            throw new Exception("没有选中测试明细！");
        }
        List<ScheduleTask> tasks = scheduleTaskService.findAll(durationTime.getIds());
        for (ScheduleTask task : tasks) {
            task.setPlanSupplyDate(durationTime.getSupplyTime());
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

    @RequestMapping(path = "/exportTestItemData")
    public ResponseEntity<byte[]> exportTestItemData(@RequestBody TestItemDto data) throws Exception {
        try {
            data.getTestItemParamsList().remove("current");
            data.getTestItemParamsList().remove("pageSize");
            if (data.getTestItemParamsList().get("params") == null || ((Map) data.getTestItemParamsList().get("params")).get("scheduleTaskLine-equipment-ID") == null) {
                return new ResponseEntity<byte[]>(HttpStatus.FAILED_DEPENDENCY);
            }
            data.getTestItemParamsList().put("orderBy","indexOrder");
            Result result = Tools.getResult(data.getTestItemParamsList(), scheduleTaskService);
            List<TestItemDto.TestItem> testItemList = new ArrayList<>();
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

    @RequestMapping(path = "/findYjrwBySecondOrderId")
    public Result findYjrwBySecondOrderId(String secondOrderId) {
        if (StringUtils.isEmpty(secondOrderId))
            return new Result();
        List<Map<String, Object>> data = scheduleTestItemService.findYjrwBySecondOrderId(secondOrderId);
        Result result = new Result();
        result.setData(data);
        return result;
    }

    @RequestMapping(path = "/findSalesOrderByFirstOrderId")
    public Result findSalesOrderByFirstOrderId(String firstOrderId) {
        if (StringUtils.isEmpty(firstOrderId))
            return new Result();
        List<Map<String, Object>> data = scheduleTestItemService.findSalesOrderByYjrwId(firstOrderId);
        Result result = new Result();
        result.setData(data);
        return result;
    }

    @RequestMapping(path = "/querySecondOrderInfoByName")
    public Result querySecondOrderInfoByName(String secondOrderName) {
        if (StringUtils.isEmpty(secondOrderName))
            return new Result();
        List<Map<String, Object>> data = scheduleTaskService.querySecondOrderInfoByName(secondOrderName);
        Result result = new Result();
        result.setData(data);
        return result;
    }

    @RequestMapping(path = "/queryBhImgByName")
    public byte[] queryBhImgByName(String bhName, HttpServletResponse response) throws SQLException {
        if (StringUtils.isEmpty(bhName))
            return null;
        FileDto fileDto = scheduleTestItemService.findImgByName(bhName);
        if(fileDto == null)
            return null;
        return fileDto.getBytes();
        /*BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");// 设置response内容的类型
            response.setHeader("Content-disposition", "attachment;filename=aaa.jpg");// 设置头部信息
            out.write((byte[])obj);
            out.flush();
        } catch (IOException e) {

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    @RequestMapping(path = "/queryBhInfoByName")
    public Result queryBhInfoByName(String bhName) {
        if (StringUtils.isEmpty(bhName))
            return new Result();
        List<Map<String, Object>> data = scheduleTestItemService.findBhInfoByName(bhName);
        Result result = new Result();
        result.setData(data);
        return result;
    }

    private TestItemDto.TestItem convertData(ScheduleTask task) {
        TestItemDto.TestItem item = new TestItemDto.TestItem();
        item.setDurationTime(task.getDurationTime());
        item.setEndDate(task.getEndDate());
        item.setStartDate(task.getStartDate());
        item.setPlanSupplyDate(task.getPlanSupplyDate());
        item.setArrivalDelay(task.getScheduleTestItem().getArrivalDelay());
        item.setCircuitNr(task.getScheduleTestItem().getCircuitNr());
        item.setItemBrief(task.getScheduleTestItem().getItemBrief());
        item.setOperationStatus(task.getScheduleTestItem().getOperationStatus());
        item.setProductNr(task.getScheduleTestItem().getProductNr());
        item.setTestBrief(task.getScheduleTestItem().getSecondOrder().getTestBrief());
        item.setTestParameter(task.getScheduleTestItem().getTestParameter());
        item.setTestType(task.getScheduleTestItem().getTestType());
        item.setSliceNr(task.getScheduleTestItem().getSliceNr());
        item.setWaferNr(task.getScheduleTestItem().getWaferNr());
        if (task.getScheduleTestItem().getSecondOrder() != null) {
            item.setName(task.getScheduleTestItem().getSecondOrder().getName());
            item.setQuantity(task.getScheduleTestItem().getSecondOrder().getQuantity());
        }
        if (task.getScheduleTestItem().getTestScribingCenter() != null && task.getScheduleTestItem().getTestScribingCenter().getWaferWarehouse() != null) {
            item.setDpsj(task.getScheduleTestItem().getTestScribingCenter().getWaferWarehouse().getDPSJ());
            if (task.getScheduleTestItem().getTestScribingCenter().getWaferWarehouse().getlLpjd() != null) {
                item.setJdb(task.getScheduleTestItem().getTestScribingCenter().getWaferWarehouse().getlLpjd().getJdb());
                item.setRpsj(task.getScheduleTestItem().getTestScribingCenter().getWaferWarehouse().getlLpjd().getRpsj());
            }
        }
        return item;
    }
}
