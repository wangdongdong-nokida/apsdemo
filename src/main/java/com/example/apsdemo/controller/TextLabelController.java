package com.example.apsdemo.controller;

import com.example.apsdemo.dao.businessObject.TextLabel;
import com.example.apsdemo.dao.businessData.TextLabelData;
import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.TextLabelService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(path = "/textLabel")
public class TextLabelController {
    @Autowired
    TextLabelService service;

    @RequestMapping(path = "/getByParams")
    public Result getByParams(@RequestBody RequestPage<TextLabelData> requestPage) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                TextLabelData params = requestPage.getParams();
                List<Predicate> predicateList=new LinkedList<>();
                if (params != null) {
                    if (params.getType() != null && !"".equals(params.getType())) {
                        predicateList.add(criteriaBuilder.equal(root.get("type"), params.getType()));
                    }
                    if (params.getName() != null && !"".equals(params.getName())) {
                        predicateList.add(criteriaBuilder.equal(root.get("name"), params.getName()));
                    }
                }
               return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Pageable pageable=Tools.getDefaultPageable(requestPage);
        if(pageable!=null){
            Page page = service.findAll(specification, pageable);
            return new Result(page.getContent(), page.getTotalElements(), true, requestPage.getPageSize(), requestPage.getCurrent());
        }else {
            List<TextLabel> labels=service.findAll(specification);
            return new Result(labels,labels.size(),true,0,0);
        }
     }


    @RequestMapping(path = "/createOrUpdate")
    public void createOrUpdate(TextLabel label){
        service.createOrUpdate(label);
    }

}
