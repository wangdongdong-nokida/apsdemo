package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.PickingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PickingOrderMapper extends JpaRepository<PickingOrder,Long>, JpaSpecificationExecutor {
}
