package com.example.apsdemo.dao.mapper;


import com.example.apsdemo.dao.camstarObject.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesOrderMapper extends JpaRepository<SalesOrder,String>, JpaSpecificationExecutor {

}
