package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.WaferGearWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WaferGearWarehouseMapper extends JpaRepository<WaferGearWarehouse,String>, JpaSpecificationExecutor {

}
