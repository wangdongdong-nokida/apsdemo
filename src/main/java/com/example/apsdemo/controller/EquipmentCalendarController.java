package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.EquipmentCalendar;
import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.businessData.EquipmentCalendarData;
import com.example.apsdemo.domain.*;
import com.example.apsdemo.service.EquipmentCalendarService;
import com.example.apsdemo.service.EquipmentService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/equipmentCalendar")
public class EquipmentCalendarController {
    @Autowired
    private EquipmentCalendarService service;
    @Autowired
    TestItemController testItemController;
    @Autowired
    ScheduleMethod scheduleMethod;

    @Autowired
    private EquipmentService equipmentService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
//    @Transactional
    public synchronized void create(@RequestBody @Valid EquipmentCalendarBean calendarBean) throws Exception {

        Optional<Equipment> equipment = equipmentService.findById(calendarBean.getEquipmentId());
        if (equipment.isPresent() && calendarBean.getData() != null) {
            EquipmentCalendar calendar = new EquipmentCalendar();
            BeanUtils.copyProperties(calendarBean.getData(), calendar);
            calendar.setEquipment(equipment.get());
            service.createOrUpdate(calendar);
            scheduleMethod.getBitSetWrapper(equipment.get());
        } else {
            throw new Exception("没有找到选中设备！");
        }
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
//    @Transactional
    public synchronized void update(@RequestBody EquipmentCalendarData data) throws Exception {

        Optional<EquipmentCalendar> calendar = service.findById(data.getID());
        if (calendar.isPresent()) {
            BeanUtils.copyProperties(data, calendar.get());
            service.createOrUpdate(calendar.get());
            if(calendar.get().getEquipment()!=null){
                scheduleMethod.getBitSetWrapper(calendar.get().getEquipment());
            }
        } else {
            throw new Exception("没有找到日历ID：" + data.getID());
        }
    }


    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public Result getCalendars(EquipmentCalendarRequest request) {

        Specification specification = (Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get("equipment").get("ID"), request.getEquipmentId()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page page = service.findAll(specification, Tools.getDefaultPageable(request));

        return new Result(page.getContent(), page.getTotalElements(), true, request.getPageSize(), request.getCurrent());
    }


    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @Transactional
    public synchronized void delete(@RequestBody Delete delete) {
        if (delete.getIds().size() > 0) {
            service.delete(delete.getIds());
        }
    }
}
