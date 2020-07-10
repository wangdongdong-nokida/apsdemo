package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.SecondOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecondOrderService extends BaseService<SecondOrderMapper>{
    @Autowired
    protected SecondOrderMapper mapper;

    @Override
    SecondOrderMapper getMapper() {
        return mapper;
    }
}
