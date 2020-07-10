package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.WaferProduct;
import com.example.apsdemo.dao.mapper.WaferProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaferProductService extends BaseService<WaferProductMapper> {
    @Autowired
    WaferProductMapper mapper;

    @Override
    WaferProductMapper  getMapper() {
        return mapper;
    }
}
