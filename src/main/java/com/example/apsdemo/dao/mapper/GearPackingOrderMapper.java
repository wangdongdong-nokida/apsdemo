package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.GearPackingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GearPackingOrderMapper extends JpaSpecificationExecutor<GearPackingOrder>, JpaRepository<GearPackingOrder,Long> {
}
