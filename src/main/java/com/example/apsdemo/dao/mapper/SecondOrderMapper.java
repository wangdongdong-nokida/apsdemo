package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.SecondOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondOrderMapper extends JpaRepository<SecondOrder,String>, JpaSpecificationExecutor {
}
