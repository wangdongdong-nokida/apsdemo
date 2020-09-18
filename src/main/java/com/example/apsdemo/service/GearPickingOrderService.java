package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.GearPickingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GearPickingOrderService extends BaseService<GearPickingOrderMapper> {
    @Autowired
    GearPickingOrderMapper mapper;

    @Override
    GearPickingOrderMapper getMapper() {
        return mapper;
    }
}
