package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.WorkFLowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkFLowService extends BaseService<WorkFLowMapper> {

    @Autowired
    WorkFLowMapper mapper;

    @Override
    WorkFLowMapper getMapper() {
        return mapper;
    }


}
