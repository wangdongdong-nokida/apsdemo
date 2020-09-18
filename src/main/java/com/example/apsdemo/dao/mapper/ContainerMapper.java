package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.camstarObject.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContainerMapper extends JpaRepository<Container,String>, JpaSpecificationExecutor {
}
