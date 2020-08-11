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
    List<SecondOrder> secondOrders=new LinkedList<>();

    public TestScribingCenterResult(TestScribingCenter center, Collection<SecondOrder> secondOrders) {
        this.center = center;
        this.secondOrders.addAll(secondOrders);
    }
}
