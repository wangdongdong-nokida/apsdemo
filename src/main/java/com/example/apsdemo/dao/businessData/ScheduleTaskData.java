package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Calendar;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class ScheduleTaskData extends DataBase {

    private int durationTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date endDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date planSupplyDate;
    private int delayPlan;
    private int delayActually;
    private int indexOrder;
    @Column(nullable = false)
    private boolean finished;

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        if (endDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            if (planSupplyDate != null) {
                if (calendar.getTime().compareTo(planSupplyDate) != 0) {
                    this.planSupplyDate = calendar.getTime();
                }
            } else {
                this.planSupplyDate = calendar.getTime();
            }
        } else {
            this.planSupplyDate = null;
        }
    }
}
