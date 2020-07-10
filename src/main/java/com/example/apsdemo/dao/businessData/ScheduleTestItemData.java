package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
public class ScheduleTestItemData extends DataBase {
    private String waferNr;
    private String sliceNr;
    private String productNr;
    private String circuitNr;
    private String testType;
    private String testParameter;
    private String  quantity;
    private String itemBrief;
    private String testBrief;
    private String arrivalProgress;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalUpdateTime;	//流片更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date warehousingTime;	//	入库时间
    private int ArrivalDelay;		//到片延误

}
