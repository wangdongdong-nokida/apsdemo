package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestScribingCenterMapper extends JpaRepository<TestScribingCenter,Long>, JpaSpecificationExecutor<TestScribingCenter> {
}
