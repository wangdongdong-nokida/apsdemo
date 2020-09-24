package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.OperationEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OperationEquipmentMapper extends JpaRepository<OperationEquipment,String>, JpaSpecificationExecutor {
}
