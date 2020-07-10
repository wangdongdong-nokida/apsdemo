package com.example.apsdemo.service;

import com.example.apsdemo.dao.businessObject.EquipmentCalendar;
import com.example.apsdemo.dao.mapper.EquipmentCalendarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EquipmentCalendarService {
    @Autowired
    private EquipmentCalendarMapper mapper;

    public void createOrUpdate(EquipmentCalendar calendar) {
        mapper.saveAndFlush(calendar);
    }

    public List<EquipmentCalendar> findAll() {
        return mapper.findAll();
    }


    public Page<EquipmentCalendar> findAll(Specification<EquipmentCalendar> specification, Pageable pageable) {
        return mapper.findAll(specification,pageable);
    }


    public void delete(List<Long> keys) {
        List<EquipmentCalendar> deleteList = mapper.findAllById(keys);
        if (deleteList.size() > 0) {
            mapper.deleteAll(deleteList);
        }
        mapper.flush();
    }


    public Optional<EquipmentCalendar> findById(Long id){
       return mapper.findById(id);
    }

}
