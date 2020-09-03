package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.Operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationMapper extends JpaSpecificationExecutor, JpaRepository<Operation, Long> {
}
