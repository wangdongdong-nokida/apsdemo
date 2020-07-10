package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import com.example.apsdemo.schedule.ScheduleTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class ScheduleTaskLineData extends DataBase {
    protected Date standardTime;
    protected Date lastTime;
}
