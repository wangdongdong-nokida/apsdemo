package com.example.apsdemo.controller;

import com.example.apsdemo.dao.mapper.TestScheduleQueueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ScheduleTestController {
    @Autowired
    TestScheduleQueueMapper mapper;
}
