package com.example.apsdemo.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class BindSecondOrderParams {
    private String  secondOrder;
    private List<String> stock=new LinkedList<>();
}
