package com.example.apsdemo.dao.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;


@Data
@ToString
public class ScribingItemDto {

    private String[] headerNameArray;
    private String[] headerKeyArray;
    private Map<String, Object> testItemParamsList;

    @Data
    @ToString
    public static class ScribingItem{
        private String waferNr;
        private String sliceNr;
        private String ScribingType;
        private String operationStatus;
        private String responsiblePerson;
        private String brief;
        private int durationTime;
        private int durationDelayTime;
        private Date startDate;
        private Date endDate;
    }
}
