package com.example.apsdemo.service;

import com.example.apsdemo.dao.camstarObject.Equipment;
import com.example.apsdemo.dao.mapper.EquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.*;

@Service
public class EquipmentService extends BaseService<EquipmentMapper>{
    @Autowired
    EquipmentMapper mapper;

    @Override
    EquipmentMapper getMapper() {
        return mapper;
    }

}
