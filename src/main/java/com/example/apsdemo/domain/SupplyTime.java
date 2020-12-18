package com.example.apsdemo.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SupplyTime {
    private Date supplyTime;
    private List<Long> ids;
}
