package com.example.apsdemo.domain;

import com.example.apsdemo.dao.businessObject.TestScribingCenter;
import com.example.apsdemo.dao.camstarObject.SecondOrder;
import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
public class TestScribingCenterResult {
    TestScribingCenter center;
    Collection<SecondOrder> secondOrders=new LinkedList<>();
    String productType;
    String orderType;

    public TestScribingCenterResult(TestScribingCenter center, Collection<SecondOrder> secondOrders, String productType, String orderType) {
        this.center = center;
        this.secondOrders = secondOrders;
        this.productType = productType;
        this.orderType = orderType;
    }
}
