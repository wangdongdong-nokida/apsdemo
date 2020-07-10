package com.example.apsdemo.service;

import com.example.apsdemo.dao.businessObject.TextLabel;
import com.example.apsdemo.dao.mapper.TextLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextLabelService {
    @Autowired
    TextLabelMapper mapper;

    public Page<TextLabel> findAll(Specification<TextLabel> specification, Pageable pageable) {
        return mapper.findAll(specification, pageable);
    }


    public List<TextLabel> findAll(Specification<TextLabel> specification) {
        return mapper.findAll(specification);
    }

    public void createOrUpdate(TextLabel label) {
        mapper.saveAndFlush(label);
    }
}
