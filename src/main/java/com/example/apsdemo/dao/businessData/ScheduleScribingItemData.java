package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class ScheduleScribingItemData extends DataBase {
    protected String waferNr;
    protected String sliceNr;
    protected String brief;
    protected Date sliceReceiveDate;
    protected String ScribingType;

    protected String responsiblePerson;
    private String operationNr;
    private String applyPerson;
    private String applyDate;

    private String reworkBrief;
}
