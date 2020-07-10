package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class ClassifiedCalendarData extends DataBase {
    int repeatType;
}
