package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.WaferModelWarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaferModelWarehouseService extends BaseService<WaferModelWarehouseMapper> {
    @Autowired
    WaferModelWarehouseMapper mapper;
    @Override
    WaferModelWarehouseMapper getMapper() {
        return mapper;
    }
}
