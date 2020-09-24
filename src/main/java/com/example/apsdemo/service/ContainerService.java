package com.example.apsdemo.service;

import com.example.apsdemo.dao.mapper.ContainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerService extends BaseService<ContainerMapper> {

    @Autowired
    ContainerMapper mapper;

    @Override
    ContainerMapper getMapper() {
        return mapper;
    }
}
