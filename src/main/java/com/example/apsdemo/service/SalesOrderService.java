package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.SalesOrder;
import com.example.apsdemo.dao.mapper.SalesOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderService extends BaseService<SalesOrderMapper>{

    @Autowired
    SalesOrderMapper mapper;
    @Override
    SalesOrderMapper getMapper() {
        return mapper;
    }
}
