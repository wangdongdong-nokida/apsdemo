package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.Wafer;
import com.example.apsdemo.dao.mapper.WaferMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class WaferService extends BaseService<WaferMapper>{
    @Autowired
    private WaferMapper mapper;

    @Override
    WaferMapper getMapper() {
        return mapper;
    }
}
