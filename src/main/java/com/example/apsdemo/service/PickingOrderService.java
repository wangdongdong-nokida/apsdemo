package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.PickingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PickingOrderService extends BaseService<PickingOrderMapper> {
    @Autowired
    PickingOrderMapper mapper;
    @Override
    PickingOrderMapper getMapper() {
        return mapper;
    }
}
