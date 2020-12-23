package com.example.apsdemo.dao.dto;

import com.example.apsdemo.dao.businessObject.ScheduleTestItem;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class TestItemDto {

    private String[] headerNameArray;
    private String[] headerKeyArray;
    private Map<String, Object> testItemParamsList;

    @Data
    @ToString
    public static class TestItem{
        private String waferNr;
        private String sliceNr;
        private String productNr;
        private String circuitNr;
        private String testType;
        private String testParameter;
        private String quantity;
        private String operationStatus;
        private String name;
        private String itemBrief;
        private String testBrief;
        private String jdb;
        private Date rpsj;
        private String dpsj;
        private int arrivalDelay;
        private int durationTime;
        private Date startDate;
        private Date endDate;
        private Date planSupplyDate;
    }

}

