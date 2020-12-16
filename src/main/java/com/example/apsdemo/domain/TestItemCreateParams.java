package com.example.apsdemo.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.LinkedList;
import java.util.List;

@Data
public class TestItemCreateParams {

    private String equipmentId = "";
    private String testBrief="";
    private int sliceNum = 0;
    private String waferNr="";
    private String[] forecast = new String[]{};
    private String[] screen = new String[]{};
    private String[] assessment = new String[]{};
    private float forecastHours = 0;
    private float screenHours = 0;
    private float assessmentHours = 0;

    private String secondOrder ="";
    private List<Product> product = new LinkedList<>();
    private List<String> stock = new LinkedList<>();

    private String testSymbol="";
    private boolean testContainer;
    private String modelNr;


    @Data
    public static class Product {
        String modelNr;
        String circuitNr;
        int forecastQuantity;
        int screenQuantity;
        int assessmentQuantity;
        int forecast;
        int screen;
        int assessment;
    }

}
