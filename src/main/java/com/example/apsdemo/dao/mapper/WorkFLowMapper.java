package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WorkFLowMapper extends JpaRepository<WorkFlow,String>, JpaSpecificationExecutor {
}
