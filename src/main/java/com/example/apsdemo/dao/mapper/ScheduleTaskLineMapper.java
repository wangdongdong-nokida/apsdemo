package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.ScheduleTaskLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduleTaskLineMapper extends JpaSpecificationExecutor<ScheduleTaskLine>, JpaRepository<ScheduleTaskLine,Long> {
}
