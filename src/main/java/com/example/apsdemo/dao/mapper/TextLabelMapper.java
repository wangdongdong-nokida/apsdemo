package com.example.apsdemo.dao.mapper;

import com.example.apsdemo.dao.businessObject.TextLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TextLabelMapper extends JpaRepository<TextLabel,Long>, JpaSpecificationExecutor<TextLabel> {
}
