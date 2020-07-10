package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.WaferWarehouse;
import com.example.apsdemo.dao.mapper.WaferWarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class WaferWarehouseService extends BaseService<WaferWarehouseMapper>{
    @Autowired
    WaferWarehouseMapper mapper;

    @Override
    WaferWarehouseMapper getMapper() {
        return mapper;
    }
}
