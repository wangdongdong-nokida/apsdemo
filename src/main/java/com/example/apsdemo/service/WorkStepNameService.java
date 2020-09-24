package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.WorkStepMapper;
import com.example.apsdemo.dao.mapper.WorkStepNameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkStepNameService extends BaseService<WorkStepNameMapper> {

    @Autowired
    WorkStepNameMapper mapper;
    @Override
    WorkStepNameMapper getMapper() {
        return mapper;
    }
}
