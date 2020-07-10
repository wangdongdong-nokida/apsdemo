package com.example.apsdemo.dao.businessData;

import com.example.apsdemo.Base.DataBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;


@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class TextLabelData extends DataBase {
    protected String type;
    protected String name;
}
