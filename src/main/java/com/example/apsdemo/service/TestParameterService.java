package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.TestParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestParameterService extends BaseService<TestParameterMapper>{
    @Autowired
    TestParameterMapper mapper;

    @Override
    TestParameterMapper getMapper() {
        return mapper;
    }
}
