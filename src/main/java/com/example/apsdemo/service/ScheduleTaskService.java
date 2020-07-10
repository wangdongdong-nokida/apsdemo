package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.ScheduleTaskMapper;
import com.example.apsdemo.schedule.ScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleTaskService extends BaseService<ScheduleTaskMapper> {
    @Autowired
    ScheduleTaskMapper mapper;

    @Override
    ScheduleTaskMapper getMapper() {
        return mapper;
    }
}
