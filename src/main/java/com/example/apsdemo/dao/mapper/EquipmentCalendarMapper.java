package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.EquipmentCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentCalendarMapper extends JpaRepository<EquipmentCalendar,Long>, JpaSpecificationExecutor<EquipmentCalendar> {
}
