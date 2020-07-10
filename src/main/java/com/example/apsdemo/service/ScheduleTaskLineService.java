package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.ScheduleTaskLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleTaskLineService extends BaseService<ScheduleTaskLineMapper> {
    @Autowired
    ScheduleTaskLineMapper mapper;
    @Override
    ScheduleTaskLineMapper getMapper() {
        return mapper;
    }
}
