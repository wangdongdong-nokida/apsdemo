package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentMapper extends JpaRepository<Equipment,String>, JpaSpecificationExecutor<Equipment> {
}
