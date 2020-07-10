package com.example.apsdemo.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class TestItemCreateParams {

    private String equipmentId="";
    private int sliceNum=0;
    private String[] forecast=new String[]{};
    private String[] screen=new String[]{};
    private String[] assessment=new String[]{};
    private int forecastHours=0;
    private int screenHours=0;
    private int assessmentHours=0;

    private List<String> secondOrder=new LinkedList<>();
    private List<String> product=new LinkedList<>();
    private List<String> stock=new LinkedList<>();

}
