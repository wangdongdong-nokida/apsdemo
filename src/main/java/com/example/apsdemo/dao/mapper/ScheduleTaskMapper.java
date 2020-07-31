package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.ScheduleTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduleTaskMapper extends JpaRepository<ScheduleTask,Long>, JpaSpecificationExecutor<ScheduleTask> {
}
