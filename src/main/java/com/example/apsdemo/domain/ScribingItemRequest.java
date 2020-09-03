package com.example.apsdemo.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScribingItemRequest {
    private String equipmentId;
    private double durationTime;
    private String brief;
    private List<Long> stocks=new ArrayList<>();
    private String sliceNr;
    private String waferNr;
    private String operationNr;
    private String responsiblePerson;
    private String applyPerson;
    private String applyDate;
}
