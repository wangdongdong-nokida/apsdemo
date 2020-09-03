package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.WorkStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WorkStepMapper extends JpaRepository<WorkStep,String>, JpaSpecificationExecutor {
}
