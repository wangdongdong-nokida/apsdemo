package com.example.apsdemo;

import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarObject.*;
import com.example.apsdemo.dao.mapper.*;
import com.example.apsdemo.service.TestScribingCenterService;
import com.example.apsdemo.schedule.ScheduleTask;
import com.example.apsdemo.schedule.ScheduleTaskLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class ApsdemoApplicationTests {

    @Autowired
    TestScheduleQueueMapper mapper;

    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    EquipmentCalendarMapper equipmentCalendarMapper;

    @Autowired
    TestParameterMapper testParameterMapper;

    @Autowired
    WaferProductMapper waferProductMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductBaseMapper productBaseMapper;

    @Autowired
    WaferMapper waferMapper;

    @Autowired
    WaferFatherMapper waferFatherMapper;

    @Autowired
    SecondOrderMapper secondOrderMapper;

    @Autowired
    WaferModelWarehouseMapper waferModelWarehouseMapper;

    @Autowired
    WaferWarehouseMapper waferWarehouseMapper;

    @Autowired
    WaferGearWarehouseMapper waferGearWarehouseMapper;

    @Autowired
    TestScribingCenterService testScribingCenterService;

    @Test
    void byteTest(){

        BitSet bitSet=new BitSet();
        bitSet.set(0,100,true);
        bitSet.set(0,10,false);


        System.out.println(bitSet.toByteArray());
        System.out.println(bitSet.toLongArray());
    }

    @Test
    void testScribingCenterService() {
        TestScribingCenter center = new TestScribingCenter();
        testScribingCenterService.saveAndFlush(center);
        System.out.println(center);
    }

    @Test
    void createWarehouse() {

        for (int i = 0; i < 10; i++) {
            WaferWarehouse waferWarehouse = new WaferWarehouse();
            waferWarehouse.setID("waferWarehouse" + i);
            waferWarehouse.setWaferNr("waferFather" + i);
            waferWarehouse.setSliceNr("sliceNr" + i);
            waferWarehouseMapper.saveAndFlush(waferWarehouse);


            WaferModelWarehouse waferModelWarehouse = new WaferModelWarehouse();
            waferModelWarehouse.setID("waferModelWarehouse" + i);
            waferModelWarehouse.setModelNr("waferModelNr" + i);
            waferModelWarehouse.setWaferWarehouse(waferWarehouse);
            waferModelWarehouseMapper.saveAndFlush(waferModelWarehouse);

            WaferGearWarehouse waferGearWarehouse = new WaferGearWarehouse();
            waferGearWarehouse.setID("waferGearWarehouse" + i);
            waferGearWarehouse.setGearNr("waferGearWarehouse" + i);
            waferGearWarehouse.setQuantity(100);
            waferGearWarehouse.setWaferModelWarehouse(waferModelWarehouse);

            waferGearWarehouseMapper.saveAndFlush(waferGearWarehouse);
        }


    }

    @Test
    void createBase() {

        WaferFather father = new WaferFather();
        father.setID("father1");
        father.setNr("waferFather1");

        waferFatherMapper.saveAndFlush(father);

        Wafer wafer = new Wafer();
        wafer.setID("wafer1");
        wafer.setResponsiblePerson("wdd");
        wafer.setNr("wafer1");

        waferMapper.saveAndFlush(wafer);

        Product product = new Product();
        product.setID("product1");
        product.setNr("1");
        product.setName("product1");
        product.setModelNr("半成品");
        product.setStatus("可用");

        productMapper.saveAndFlush(product);
    }

    @Test
    void createBaseRelation() {
        Wafer wafer = waferMapper.findAll().iterator().next();
        WaferFather father = waferFatherMapper.findAll().iterator().next();
        father.setPositionSecond(wafer);

        waferFatherMapper.saveAndFlush(father);

        Product product = productMapper.findAll().iterator().next();
        WaferProduct waferProduct = new WaferProduct();
        waferProduct.setID("waferProduct1");
        waferProduct.setCircuitNo("1");
        waferProduct.setDesigner("wdd");
        waferProduct.setQuantity(1111);
        waferProduct.setProduct(product);
        waferProduct.setWafer(wafer);

        waferProductMapper.saveAndFlush(waferProduct);
    }


    @Test
    void createSecondOrder() {
        SecondOrder secondOrder = new SecondOrder();
        secondOrder.setID("secondOrder1");
        secondOrder.setName("order");
        secondOrder.setWaferNr("waferFather1;waferFather2");
        secondOrder.setStatus("可用");
        secondOrder.setType("正常");
        secondOrderMapper.saveAndFlush(secondOrder);
    }


    @Test
    void test1() {
        for (int i = 0; i < 10; i++) {
            TestParameter parameter = new TestParameter();
            parameter.setLCscsid(i + "cs");
            parameter.setName("测试" + i);
            parameter.setType("测试");
            testParameterMapper.saveAndFlush(parameter);
        }

        for (int i = 0; i < 10; i++) {
            TestParameter parameter = new TestParameter();
            parameter.setLCscsid(i + "sx");
            parameter.setName("筛选" + i);
            parameter.setType("筛选");
            testParameterMapper.saveAndFlush(parameter);
        }


        for (int i = 0; i < 10; i++) {
            TestParameter parameter = new TestParameter();
            parameter.setLCscsid(i + "kh");
            parameter.setName("考核" + i);
            parameter.setType("考核");
            testParameterMapper.saveAndFlush(parameter);
        }

    }

    @Test
    void createEquipment() {

        List<Equipment> EquipmentList = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            Equipment data = new Equipment();
            data.setID((i + 1) + "");
            data.setType("生产");
            data.setName("生产设备" + (i + 1));
            EquipmentList.add(data);
        }
        equipmentMapper.saveAll(EquipmentList);
        equipmentMapper.flush();
    }


    @Test
    void test() {
        Optional<ScheduleTask> optional = mapper.findById((long) 1);
        if (optional.isPresent()) {
            ScheduleTask queue = optional.get();
            queue.setStartDate(Calendar.getInstance().getTime());
//            queue.scheduleDate();
        }
    }
}
