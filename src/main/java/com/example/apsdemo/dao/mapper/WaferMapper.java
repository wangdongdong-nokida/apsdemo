package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.Wafer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WaferMapper extends JpaRepository<Wafer,String>, JpaSpecificationExecutor<Wafer> {
}
