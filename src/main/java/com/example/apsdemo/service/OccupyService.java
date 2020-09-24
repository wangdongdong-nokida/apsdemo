package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.OccupyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccupyService extends BaseService<OccupyMapper> {

    @Autowired
    OccupyMapper mapper;

    @Override
    OccupyMapper getMapper() {
        return mapper;
    }
}
