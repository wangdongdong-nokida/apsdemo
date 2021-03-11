package com.example.apsdemo.controller;


import com.example.apsdemo.admin.authority.security.pojo.SysUser;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.dao.mapper.SecondOrderMapper;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.SecondOrderService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "/secondOrder")
public class SecondOrderController {
    @Autowired
    SecondOrderService service;

    @RequestMapping(path = "/findSecondOrders")
    public Result findByParams(@RequestBody Map<String, Object> requestPage) {

        SysUser sysUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String teamName = sysUser.getTeamName();
        Object testContainer = requestPage.get("testContainer");
        if (requestPage.get("params") == null) {
            requestPage.computeIfAbsent("params", key -> new HashMap<>());
        }

        ((Map) requestPage.get("params")).computeIfAbsent("!status", key -> "未发布");

        Object showState = ((Map) requestPage.get("params")).get("showState");


        if (requestPage.get("noTest") != null) {
            ((Map) requestPage.get("params")).put("!^scribingGroup", "");
        }
        if (testContainer == null) {
            ((Map) requestPage.get("params")).put("!productType-name", "载体");
        } else {
            ((Map) requestPage.get("params")).put("productType-name", "载体");
        }

        Specification specification = Tools.getSpecificationByParams((Map) requestPage.get("params"));

        int current = requestPage.get("current") != null ? Integer.valueOf(requestPage.get("current").toString()) : 0;
        int pageSize = requestPage.get("pageSize") != null ? Integer.valueOf(requestPage.get("pageSize").toString()) : 0;
        String orderBy = requestPage.get("orderBy") != null ? requestPage.get("orderBy").toString() : null;
        Result result = new Result(current, pageSize);
        Pageable pageable = Tools.getPageableByPagination(current, pageSize, orderBy);


//        Map params = new HashMap();
//        if (showState == null || Objects.equals(showState, "uncreated")) {
////            ((Map) requestPage.get("params")).computeIfAbsent("*scheduleTestItems", key -> "");
//            params.put("!<>createdTeamName", teamName);
//        } else {
////            ((Map) requestPage.get("params")).computeIfAbsent("!*scheduleTestItems", key -> "");
//            params.put("<>createdTeamName", teamName);
//        }

        Specification teamNameSpecification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (showState == null || Objects.equals(showState, "uncreated")) {
                    return criteriaBuilder.or(
                            criteriaBuilder.notLike(root.get("createdTeamName"), "%" + teamName + "%"),
                            criteriaBuilder.isNull(root.get("createdTeamName")),
                            criteriaBuilder.equal(root.get("createdTeamName"), "")
                    );
                } else {
                    return criteriaBuilder.like(root.get("createdTeamName"), "%" + teamName + "%");
                }
            }
        };
        specification=specification.and(teamNameSpecification);
        Page page = service.findAll(specification, pageable);
        if (page.hasContent()) {
            result.setData(page.getContent());
            result.setTotal(page.getTotalElements());
        }
        return result;
    }

}
