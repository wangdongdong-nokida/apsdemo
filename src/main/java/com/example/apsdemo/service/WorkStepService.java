package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.WorkStepMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkStepService extends BaseService<WorkStepMapper> {

    @Autowired
    WorkStepMapper mapper;

    @Override
    WorkStepMapper getMapper() {
        return mapper;
    }
}
