package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.LGyWlztMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LGyWlztService extends BaseService<LGyWlztMapper> {

    @Autowired
    LGyWlztMapper mapper;

    @Override
    LGyWlztMapper getMapper() {
        return mapper;
    }

}
