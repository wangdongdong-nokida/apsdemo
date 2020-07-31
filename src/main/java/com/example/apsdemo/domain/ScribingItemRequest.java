package com.example.apsdemo.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScribingItemRequest {
    private String equipmentId;
    private int durationTime;
    private String brief;
    private List<Long> stocks=new ArrayList<>();
}
