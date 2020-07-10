package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.WaferFather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WaferFatherMapper extends JpaSpecificationExecutor<WaferFather>, JpaRepository<WaferFather,String> {
}
