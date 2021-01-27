package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.EquipmentC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentCMapper extends JpaRepository<EquipmentC,String>, JpaSpecificationExecutor<EquipmentC> {

}
