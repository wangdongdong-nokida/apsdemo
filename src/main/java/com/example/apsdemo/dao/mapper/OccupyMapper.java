package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.Occupy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OccupyMapper extends JpaSpecificationExecutor<Occupy>, JpaRepository<Occupy,String> {
}
