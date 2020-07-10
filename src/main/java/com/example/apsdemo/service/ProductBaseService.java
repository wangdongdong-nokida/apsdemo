package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.ProductBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductBaseService extends BaseService<ProductBaseMapper> {
    @Autowired
    ProductBaseMapper mapper;

    @Override
    ProductBaseMapper getMapper() {
        return mapper;
    }
}
