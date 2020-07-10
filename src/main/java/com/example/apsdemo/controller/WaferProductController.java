package com.example.apsdemo.controller;

import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferProductService;
import com.example.apsdemo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/waferProduct")
public class WaferProductController {
    @Autowired
    WaferProductService service;

    @RequestMapping(path = "/findProductsByWafer")
    public Result findProducts(@RequestBody Map<String, Object> requestPage) {
        if (requestPage.get("params")==null||((Map)requestPage.get("params")).get("wafer-nr") == null) {
            return new Result();
        }
        return Tools.getResult(requestPage, service);
    }

}
