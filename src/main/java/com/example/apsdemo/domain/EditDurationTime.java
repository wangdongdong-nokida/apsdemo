package com.example.apsdemo.domain;

import lombok.Data;

import java.util.List;

@Data
public class EditDurationTime {
    private int durationTime;
    private List<Long> ids;
}
