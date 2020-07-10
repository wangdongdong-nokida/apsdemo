package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class EquipmentCalendarData extends DataBase {

//    protected String equipmentName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date endTime;
    protected int repeatType;
//    protected boolean monday;
//    protected boolean tuesday;
//    protected boolean wednesday;
//    protected boolean thursday;
//    protected boolean friday;
//    protected boolean saturday;
//    protected boolean sunday;
    protected boolean blackName;
}
