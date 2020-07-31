package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.ScheduleScribingItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleScribingItemService extends BaseService<ScheduleScribingItemMapper> {
    @Autowired
    ScheduleScribingItemMapper mapper;

    @Override
    ScheduleScribingItemMapper getMapper() {
        return mapper;
    }
}
