package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.EquipmentCMapper;
import com.example.apsdemo.dao.mapper.EquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentCService extends BaseService<EquipmentCMapper>{
    @Autowired
    EquipmentCMapper mapper;

    @Override
    EquipmentCMapper getMapper() {
        return mapper;
    }

}
