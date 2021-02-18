package com.example.apsdemo.controller;

import com.example.apsdemo.dao.camstarObject.Occupy;
import com.example.apsdemo.dao.camstarObject.WaferProduct;
import com.example.apsdemo.domain.Result;
import com.example.apsdemo.service.WaferProductService;
import com.example.apsdemo.utils.Tools;
import org.apache.commons.collections4.ListUtils;
import org.apache.poi.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(path = "/waferProduct")
public class WaferProductController {
    @Autowired
    WaferProductService service;

    @RequestMapping(path = "/findProductsByWafer")
    public Result findProducts(@RequestBody Map<String, Object> requestPage) {
        if (requestPage.get("params")==null||((Map)requestPage.get("params")).get("==wafer-nr") == null) {
            return new Result();
        }
        requestPage.remove("current");
        requestPage.remove("pageSize");
        Result result =  Tools.getResult(requestPage, service);
        List<WaferProduct> data = new ArrayList(result.getData());
        if (data != null && data.size() > 0) {
            Collections.sort(data, new Comparator<WaferProduct>() {
                @Override
                public int compare(WaferProduct waferProduct1, WaferProduct waferProduct2) {
                    if (waferProduct1 != null && waferProduct2 != null) {
                        if (waferProduct1.getCircuitNo() != null && waferProduct2.getCircuitNo() != null) {
                            return Integer.valueOf(waferProduct1.getCircuitNo()).compareTo(Integer.valueOf(waferProduct2.getCircuitNo()));
                        } else {
                            return waferProduct1.getCircuitNo() == null && waferProduct2.getCircuitNo() == null ? 0 : (waferProduct1.getCircuitNo() == null ? 1 : -1);
                        }
                    } else {
                        return waferProduct1 == null && waferProduct2 == null ? 0 : (waferProduct1 == null ? 1 : -1);
                    }
                }
            });
        }
        result.setData(data);
        return result;
    }

}
