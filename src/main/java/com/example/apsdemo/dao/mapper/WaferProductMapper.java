package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.WaferProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WaferProductMapper extends JpaRepository<WaferProduct, String>, JpaSpecificationExecutor<WaferProduct> {
}
