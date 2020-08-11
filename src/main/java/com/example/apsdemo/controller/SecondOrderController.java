package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.SecondOrder;
import com.example.apsdemo.domain.RequestPage;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.SecondOrderService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/secondOrder")
public class SecondOrderController {
    @Autowired
    SecondOrderService service;

    @RequestMapping(path = "/findSecondOrders")
    public Result findByParams(@RequestBody Map<String,Object> requestPage) {
        Object testContainer=requestPage.get("testContainer");
        if(requestPage.get("params")==null){
            requestPage.put("params",new HashMap<>());
        }
        if(requestPage.get("noTest")!=null){
            ((Map)requestPage.get("params")).put("!^scribingGroup","");
        }
        if(testContainer==null){
            ((Map)requestPage.get("params")).put("!productType-name","载体");
        }else {
            ((Map)requestPage.get("params")).put("productType-name","载体");
        }
       return Tools.getResult(requestPage,service);
    }
}
