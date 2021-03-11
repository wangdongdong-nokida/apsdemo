package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.dao.mapper.SecondOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondOrderService extends BaseService<SecondOrderMapper>{
    @Autowired
    protected SecondOrderMapper mapper;

    @Override
    SecondOrderMapper getMapper() {
        return mapper;
    }

    public List<SecondOrder> findByParams(){
        return mapper.findByParams();
    }
}
