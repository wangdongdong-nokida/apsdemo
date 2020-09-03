package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.GearPackingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GearPackingOrderService extends BaseService<GearPackingOrderMapper> {
    @Autowired
    GearPackingOrderMapper mapper;

    @Override
    GearPackingOrderMapper getMapper() {
        return mapper;
    }
}
