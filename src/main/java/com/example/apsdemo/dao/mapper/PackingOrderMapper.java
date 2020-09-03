package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.PackingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PackingOrderMapper extends JpaRepository<PackingOrder,Long>, JpaSpecificationExecutor {
}
