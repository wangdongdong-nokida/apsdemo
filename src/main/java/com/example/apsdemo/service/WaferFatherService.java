package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.WaferFather;
import com.example.apsdemo.dao.mapper.WaferFatherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaferFatherService {
    @Autowired
    WaferFatherMapper mapper;

    public List<WaferFather> findAll(Specification<WaferFather> specification){
        return mapper.findAll(specification);
    }

    public Page findAll(Specification<WaferFather> specification, Pageable pageable){
        return mapper.findAll(specification,pageable);
    }
}
