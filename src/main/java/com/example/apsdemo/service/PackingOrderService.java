package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.PackingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackingOrderService extends BaseService<PackingOrderMapper> {
    @Autowired
    PackingOrderMapper mapper;
    @Override
    PackingOrderMapper getMapper() {
        return mapper;
    }
}
