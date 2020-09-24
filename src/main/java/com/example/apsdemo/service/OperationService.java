package com.example.apsdemo.service;

import com.example.apsdemo.dao.businessObject.Operation;
import com.example.apsdemo.dao.mapper.OperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationService extends BaseService<OperationMapper> {

    @Autowired
    OperationMapper mapper;

    @Override
    OperationMapper getMapper() {
        return mapper;
    }

}
