package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.ScheduleTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestScheduleQueueMapper extends JpaRepository<ScheduleTask,Long> {

}
