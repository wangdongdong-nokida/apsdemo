package com.example.apsdemo.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.LinkedList;
import java.util.List;

@Data
public class TestItemCreateParams {

    private String equipmentId = "";
    private int sliceNum = 0;
    private String waferNr;
    private String[] forecast = new String[]{};
    private String[] screen = new String[]{};
    private String[] assessment = new String[]{};
    private float forecastHours = 0;
    private float screenHours = 0;
    private float assessmentHours = 0;

    private List<String> secondOrder = new LinkedList<>();
    private List<Product> product = new LinkedList<>();
    private List<String> stock = new LinkedList<>();


    @Data
    public class Product {
        String modelNr;
        int forecastQuantity;
        int screenQuantity;
        int assessmentQuantity;
    }

}
