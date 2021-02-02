package com.example.apsdemo.service;

import com.example.apsdemo.dao.dto.FileDto;
import com.example.apsdemo.dao.mapper.ScheduleTestItemMapper;
import com.example.apsdemo.dao.mapper.mybatisMapper.TestItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleTestItemService extends BaseService<ScheduleTestItemMapper> {
    @Autowired
    ScheduleTestItemMapper mapper;
    @Autowired
    TestItemMapper testItemMapper;
    @Override
    ScheduleTestItemMapper getMapper() {
        return mapper;
    }

    public List<Map<String,Object>> findYjrwBySecondOrderId(String secondOrderId){
        return testItemMapper.findYjrwBySecondOrderId(secondOrderId);
    }

    public List<Map<String,Object>> findSalesOrderByYjrwId(String yjrwId) {
        return testItemMapper.findSalesOrderByYjrwId(yjrwId);
    }
    public FileDto findImgByName(String bhName){
        return testItemMapper.findImgByName(bhName);
    }

    public List<Map<String,Object>> findBhInfoByName(String bhName){
        return testItemMapper.findBhInfoByName(bhName);
    }
}
