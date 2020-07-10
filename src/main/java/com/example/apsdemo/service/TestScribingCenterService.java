package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.TestScribingCenterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestScribingCenterService extends BaseService<TestScribingCenterMapper> {
    @Autowired
    TestScribingCenterMapper mapper;
    @Override
    TestScribingCenterMapper getMapper() {
        return mapper;
    }
}
