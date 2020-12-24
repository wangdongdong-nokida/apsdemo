package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.ScheduleTaskMapper;
import com.example.apsdemo.dao.mapper.mybatisMapper.TestSchedulingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ScheduleTaskService extends BaseService<ScheduleTaskMapper> {
    @Autowired
    ScheduleTaskMapper mapper;

    @Autowired
    TestSchedulingMapper testSchedulingMapper;
    @Override
    ScheduleTaskMapper getMapper() {
        return mapper;
    }

    public List<Map<String,Object>> querySecondOrderInfoByName(String secondOrderName){
        return testSchedulingMapper.querySecondOrderInfoByName(secondOrderName);
    }
}
