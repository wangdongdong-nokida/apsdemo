package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.WaferGearWarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaferGearWarehouseService extends BaseService<WaferGearWarehouseMapper> {

    @Autowired
    WaferGearWarehouseMapper mapper;

    @Override
    WaferGearWarehouseMapper getMapper() {
        return mapper;
    }

}
