package com.example.apsdemo.dao.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;


@Data
@ToString
public class PickingItemDto {

    private String[] headerNameArray;
    private String[] headerKeyArray;
    private Map<String, Object> testItemParamsList;

    @Data
    @ToString
    public static class PickingItem{
        private String waferNr;
        private String sliceNr;
        private String modelNr;
        private String circuitNr;
        private String workFlowName;
        private String workStepName;
        private String equipmentName;
        private String bindCustomer;
        private String bindContract;
        private int durationTime;
        private int durationDelayTime;
        private Date startDate;
        private Date endDate;
        private String bindSalesOrder;
        private String salesOrderQuantities;
        private String itemBrief;
    }
}
