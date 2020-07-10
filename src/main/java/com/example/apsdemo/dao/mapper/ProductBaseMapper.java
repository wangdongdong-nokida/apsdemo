package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.ProductBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBaseMapper extends  JpaRepository<ProductBase,String> ,JpaSpecificationExecutor<ProductBase>{
}
