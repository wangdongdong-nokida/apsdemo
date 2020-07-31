package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.ScheduleScribingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduleScribingItemMapper extends JpaSpecificationExecutor, JpaRepository<ScheduleScribingItem,Long> {
}
