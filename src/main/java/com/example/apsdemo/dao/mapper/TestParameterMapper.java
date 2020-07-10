package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.TestParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TestParameterMapper extends JpaSpecificationExecutor<TestParameter>, JpaRepository<TestParameter,String> {

}
