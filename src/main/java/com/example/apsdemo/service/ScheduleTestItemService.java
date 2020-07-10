package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.ScheduleTestItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleTestItemService extends BaseService<ScheduleTestItemMapper> {
    @Autowired
    ScheduleTestItemMapper mapper;
    @Override
    ScheduleTestItemMapper getMapper() {
        return mapper;
    }
}
