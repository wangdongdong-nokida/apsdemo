package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.OperationEquipment;
import com.example.apsdemo.dao.mapper.OperationEquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationEquipmentService extends BaseService<OperationEquipmentMapper> {

    @Autowired
    OperationEquipmentMapper mapper;

    @Override
    OperationEquipmentMapper getMapper() {
        return mapper;
    }
}
