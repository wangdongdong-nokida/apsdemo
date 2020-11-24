package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class ScheduleTaskData extends DataBase {

    private int durationTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date endDate;
    private int delayPlan;
    private int delayActually;
    private int indexOrder;
}
