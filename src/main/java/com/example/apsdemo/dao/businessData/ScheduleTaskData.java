package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class ScheduleTaskData extends DataBase {

    private int DurationTime;
    @DateTimeFormat(pattern = "yyyy-MM-DD hh:mm")
    private Date StartDate;
    @DateTimeFormat(pattern = "yyyy-MM-DD hh:mm")
    private Date EndDate;
    private int DelayPlan;
    private int DelayActually;
    private int indexOrder;
}
